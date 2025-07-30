package com.denchic45.rickandmorty.domain.usecase

import com.denchic45.rickandmorty.data.repository.CharacterRepository

class UpdateCharacterToFavoriteUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(characterId: Int, isFavorite: Boolean) = repository.updateFavorite(characterId,isFavorite)
}