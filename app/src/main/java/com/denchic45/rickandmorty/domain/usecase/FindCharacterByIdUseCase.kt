package com.denchic45.rickandmorty.domain.usecase

import com.denchic45.rickandmorty.data.repository.CharacterRepository
import com.denchic45.rickandmorty.domain.model.CharacterDetails
import com.denchic45.rickandmorty.domain.model.Resource
import kotlinx.coroutines.flow.Flow

class FindCharacterByIdUseCase(private val repository: CharacterRepository) {
    operator fun invoke(
        characterId: Int
    ): Flow<Resource<CharacterDetails>> {
        return repository.findById(characterId)
    }
}