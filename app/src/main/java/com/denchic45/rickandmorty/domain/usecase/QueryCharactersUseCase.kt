package com.denchic45.rickandmorty.domain.usecase

import androidx.paging.PagingData
import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus
import com.denchic45.rickandmorty.data.repository.CharacterRepository
import com.denchic45.rickandmorty.domain.model.CharacterItem
import kotlinx.coroutines.flow.Flow

class QueryCharactersUseCase(private val repository: CharacterRepository) {
    operator fun invoke(
        name: String? = null,
        statuses: CharacterStatus? =null,
        species: String? = null,
        type: String? = null,
        gender: CharacterGender? = null
    ): Flow<PagingData<CharacterItem>> {
        return repository.query(name, statuses, species, type, gender)
    }
}