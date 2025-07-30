package com.denchic45.rickandmorty.ui.episodedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.denchic45.rickandmorty.domain.model.stateInResource
import com.denchic45.rickandmorty.domain.usecase.FindEpisodeByIdUseCase
import com.denchic45.rickandmorty.domain.usecase.UpdateCharacterToFavoriteUseCase
import com.denchic45.rickandmorty.ui.EpisodeDetails
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    findEpisodeByIdUseCase: FindEpisodeByIdUseCase,
    private val updateCharacterToFavoriteUseCase: UpdateCharacterToFavoriteUseCase
) : ViewModel() {
    private val args = savedStateHandle.toRoute<EpisodeDetails>()
    val episodeState = findEpisodeByIdUseCase(args.episodeId).stateInResource(viewModelScope)

    fun onFavoriteClick(characterId: Int, favorite: Boolean) {
        viewModelScope.launch {
            updateCharacterToFavoriteUseCase(characterId, favorite)
        }
    }
}