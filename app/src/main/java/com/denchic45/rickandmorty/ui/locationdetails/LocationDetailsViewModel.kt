package com.denchic45.rickandmorty.ui.locationdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.denchic45.rickandmorty.domain.model.stateInResource
import com.denchic45.rickandmorty.domain.usecase.FindLocationByIdUseCase
import com.denchic45.rickandmorty.domain.usecase.UpdateCharacterToFavoriteUseCase
import com.denchic45.rickandmorty.ui.LocationDetails
import kotlinx.coroutines.launch

class LocationDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    findLocationByIdUseCase: FindLocationByIdUseCase,
    private val updateCharacterToFavoriteUseCase: UpdateCharacterToFavoriteUseCase
) : ViewModel() {
    private val route = savedStateHandle.toRoute<LocationDetails>()
    val locationState = findLocationByIdUseCase(route.locationId).stateInResource(viewModelScope)

    fun onFavoriteClick(characterId: Int, favorite: Boolean) {
        viewModelScope.launch {
            updateCharacterToFavoriteUseCase(characterId, favorite)
        }
    }
}