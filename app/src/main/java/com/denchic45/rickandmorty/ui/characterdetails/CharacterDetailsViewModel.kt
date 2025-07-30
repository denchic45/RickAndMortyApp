package com.denchic45.rickandmorty.ui.characterdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.denchic45.rickandmorty.domain.model.Resource
import com.denchic45.rickandmorty.domain.model.stateInResource
import com.denchic45.rickandmorty.domain.usecase.FindCharacterByIdUseCase
import com.denchic45.rickandmorty.domain.usecase.UpdateCharacterToFavoriteUseCase
import com.denchic45.rickandmorty.ui.CharacterDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    findCharacterByIdUseCase: FindCharacterByIdUseCase,
    private val updateCharacterToFavoriteUseCase: UpdateCharacterToFavoriteUseCase
) : ViewModel() {
    val args = savedStateHandle.toRoute<CharacterDetails>()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    private val updateTrigger = Channel<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _characterState = updateTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .flatMapLatest { findCharacterByIdUseCase(args.characterId) }
        .onEach { _isRefreshing.value = false }

    val characterState = _characterState.filter { it !is Resource.Loading }
        .stateInResource(viewModelScope)

    fun onFavoriteCLick(favorite: Boolean) {
        viewModelScope.launch {
            updateCharacterToFavoriteUseCase(args.characterId, favorite)
        }
    }

    fun onRetry() {
        viewModelScope.launch {
            updateTrigger.send(Unit)
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            updateTrigger.send(Unit)
        }
    }
}