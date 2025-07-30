package com.denchic45.rickandmorty.ui.characters

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus
import com.denchic45.rickandmorty.domain.usecase.QueryCharactersUseCase
import com.denchic45.rickandmorty.domain.usecase.UpdateCharacterToFavoriteUseCase
import com.denchic45.rickandmorty.util.Field
import com.denchic45.rickandmorty.util.FieldEditor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val queryCharactersUseCase: QueryCharactersUseCase,
    private val updateCharacterToFavoriteUseCase: UpdateCharacterToFavoriteUseCase
) : ViewModel() {

    private val updateTrigger = Channel<Unit>()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    val searchState = SearchState()

    private val filterEditor = FieldEditor(
        mapOf(
            "name" to Field(searchState::name),
            "species" to Field (searchState::species),
            "type" to Field (searchState::type),
            "status" to Field (searchState::status ),
            "gender" to Field( searchState::gender)
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val charactersState = updateTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .flatMapLatest {
            queryCharactersUseCase(
                searchState.name.trim().takeIf(String::isNotEmpty),
                searchState.status,
                searchState.species.trim().takeIf(String::isNotEmpty),
                searchState.type.trim().takeIf(String::isNotEmpty),
                searchState.gender
            )
        }
        .onEach { _isRefreshing.value = false }
        .cachedIn(viewModelScope)


    fun onSearchRequest() {
        viewModelScope.launch { updateTrigger.send(Unit) }
    }

    fun onRefreshingChange(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }

    fun onClearSearchClick() {
        searchState.name = ""
        viewModelScope.launch { updateTrigger.send(Unit) }
    }

    fun onFavoriteClick(characterId: Int, favorite: Boolean) {
        viewModelScope.launch {
            updateCharacterToFavoriteUseCase(characterId, favorite)
        }
    }

    fun onFiltersClick() {
        filterEditor.updateOldValues()
    }

    fun onFiltersClose() {
        if (filterEditor.hasChanges()) onSearchRequest()
    }

    fun onStatusFilterChange(status: CharacterStatus) {
        searchState.status = if (searchState.status != status) status else null
    }

    fun onGenderFilterChange(gender: CharacterGender) {
        searchState.gender = if (searchState.gender != gender) gender else null
    }

    fun onSpeciesFilterChange(text: String) {
        searchState.species = text
    }

    fun onTypeFilterChange(text: String) {
        searchState.type = text
    }

    fun onFiltersClear() {
        with(searchState) {
            species = ""
            type = ""
            status = null
            gender = null
        }
        onSearchRequest()
    }
}

@Stable
class SearchState {
    var name by mutableStateOf("")
    var species by mutableStateOf("")
    var type by mutableStateOf("")
    var status: CharacterStatus? by mutableStateOf(null)
    var gender: CharacterGender? by mutableStateOf(null)

    val hasFilters: Boolean
        get() = species.isNotEmpty()
                || type.isNotEmpty()
                || status != null
                || gender != null
}