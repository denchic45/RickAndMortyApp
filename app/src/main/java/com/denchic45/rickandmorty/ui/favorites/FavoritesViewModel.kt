package com.denchic45.rickandmorty.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denchic45.rickandmorty.domain.usecase.FindFavoritesCharactersUseCase
import com.denchic45.rickandmorty.domain.usecase.UpdateCharacterToFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    findFavoritesCharactersUseCase: FindFavoritesCharactersUseCase,
    private val updateCharacterToFavoriteUseCase: UpdateCharacterToFavoriteUseCase
) : ViewModel() {

    val favoritesState = findFavoritesCharactersUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onFavoriteClick(characterId: Int, favorite: Boolean) {
        viewModelScope.launch {
            updateCharacterToFavoriteUseCase(characterId, favorite)
        }
    }
}