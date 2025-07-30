package com.denchic45.rickandmorty.domain.usecase

import com.denchic45.rickandmorty.data.repository.CharacterRepository
import com.denchic45.rickandmorty.domain.model.CharacterItem
import kotlinx.coroutines.flow.Flow

class FindFavoritesCharactersUseCase(private val repository: CharacterRepository) {
    operator fun invoke(): Flow<List<CharacterItem>> {
        return repository.findFavorites()
    }
}