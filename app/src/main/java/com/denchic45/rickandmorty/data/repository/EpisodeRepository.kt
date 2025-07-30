package com.denchic45.rickandmorty.data.repository

import androidx.room.withTransaction
import com.denchic45.rickandmorty.data.api.CharacterApi
import com.denchic45.rickandmorty.data.api.EpisodeApi
import com.denchic45.rickandmorty.data.database.AppDatabase
import com.denchic45.rickandmorty.data.database.dao.CharacterDao
import com.denchic45.rickandmorty.data.database.dao.EpisodeDao
import com.denchic45.rickandmorty.data.database.entity.EpisodesCharactersEntity
import com.denchic45.rickandmorty.data.mapper.toCharacterEntities
import com.denchic45.rickandmorty.data.mapper.toEpisodeDetailsItem
import com.denchic45.rickandmorty.data.mapper.toEpisodeEntity
import com.denchic45.rickandmorty.data.observeResource
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class EpisodeRepository(
    private val database: AppDatabase,
    private val episodeApi: EpisodeApi,
    private val characterApi: CharacterApi,
    private val episodeDao: EpisodeDao,
    private val characterDao: CharacterDao
) {
    fun findById(episodeId: Int) = observeResource(
        query = episodeDao.getById(episodeId)
            .filterNotNull()
            .map { it.toEpisodeDetailsItem() },
        fetch = {
            val episodeResponse = episodeApi.getById(episodeId)
            database.withTransaction {
                characterDao.upsert(
                    characterApi.getByIds(episodeResponse.characterIds)
                        .toCharacterEntities()
                )
                episodeDao.upsertEpisodesCharacters(
                    episodeResponse.characterIds
                        .map { EpisodesCharactersEntity(episodeId, it) }
                )
                episodeDao.upsert(episodeResponse.toEpisodeEntity())
            }
        }
    )
}