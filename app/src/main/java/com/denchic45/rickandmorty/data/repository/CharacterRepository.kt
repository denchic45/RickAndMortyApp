package com.denchic45.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.denchic45.rickandmorty.data.api.CharacterApi
import com.denchic45.rickandmorty.data.api.EpisodeApi
import com.denchic45.rickandmorty.data.api.LocationApi
import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus
import com.denchic45.rickandmorty.data.database.AppDatabase
import com.denchic45.rickandmorty.data.database.dao.CharacterDao
import com.denchic45.rickandmorty.data.database.dao.EpisodeDao
import com.denchic45.rickandmorty.data.database.dao.LocationDao
import com.denchic45.rickandmorty.data.database.entity.CharacterEntity
import com.denchic45.rickandmorty.data.database.entity.EpisodesCharactersEntity
import com.denchic45.rickandmorty.data.mapper.toCharacterItem
import com.denchic45.rickandmorty.data.mapper.toCharacterItems
import com.denchic45.rickandmorty.data.mapper.toEpisodeEntities
import com.denchic45.rickandmorty.data.mapper.toEpisodeEntity
import com.denchic45.rickandmorty.data.mapper.toEpisodeItems
import com.denchic45.rickandmorty.data.mapper.toLocationEntity
import com.denchic45.rickandmorty.data.mapper.toLocationItem
import com.denchic45.rickandmorty.data.observeResource
import com.denchic45.rickandmorty.data.pagination.MoviesRemoteMediator
import com.denchic45.rickandmorty.domain.model.CharacterDetails
import com.denchic45.rickandmorty.domain.model.CharacterItem
import com.denchic45.rickandmorty.domain.model.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class CharacterRepository(
    private val database: AppDatabase,
    private val characterDao: CharacterDao,
    private val locationDao: LocationDao,
    private val episodeDao: EpisodeDao,
    private val characterApi: CharacterApi,
    private val locationApi: LocationApi,
    private val episodeApi: EpisodeApi
) {

    @OptIn(ExperimentalPagingApi::class)
    fun query(
        name: String?,
        status: CharacterStatus?,
        species: String?,
        type: String?,
        gender: CharacterGender?
    ): Flow<PagingData<CharacterItem>> = Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 20),
        remoteMediator = MoviesRemoteMediator(
            characterApi,
            database,
            name,
            status,
            species,
            type,
            gender
        ),
        pagingSourceFactory = {
            characterDao.getBy(name, status, species, type, gender)
        }
    ).flow.map { it.map(CharacterEntity::toCharacterItem) }

    fun findById(characterId: Int): Flow<Resource<CharacterDetails>> = observeResource(
        query = characterDao.observeById(characterId)
            .filterNotNull()
            .map {
                CharacterDetails(
                    character = it.character.toCharacterItem(),
                    origin = it.origin?.let { origin ->
                        locationDao.getById(origin.id).toLocationItem()
                    },
                    location = it.location?.let { location ->
                        locationDao.getById(location.id).toLocationItem()
                    },
                    episodes = episodeDao.getByCharacterId(characterId).toEpisodeItems()
                )
            },
        fetch = {
            coroutineScope {
                val characterResponse = characterApi.getById(characterId)

                val locationAsync = async {
                    characterResponse.location.id?.let {
                        locationApi.getById(it).toLocationEntity()
                    }
                }
                val originAsync = async {
                    characterResponse.origin.id?.let {
                        locationApi.getById(it).toLocationEntity()
                    }
                }
                val episodesAsync = async {
                    when (characterResponse.episodeIds.size) {
                        0 -> emptyList()
                        1 -> listOf(
                            episodeApi.getById(
                                characterResponse.episodeIds.single()
                            ).toEpisodeEntity()
                        )

                        else -> episodeApi.getByIds(characterResponse.episodeIds)
                            .toEpisodeEntities()
                    }
                }

                val location = locationAsync.await()
                val origin = originAsync.await()
                val episodes = episodesAsync.await()
                database.withTransaction {
                    location?.let { locationDao.upsert(it) }
                    origin?.let { locationDao.upsert(it) }

                    episodeDao.upsert(episodes)
                    episodeDao.upsertEpisodesCharacters(
                        characterResponse.episodeIds.map {
                            EpisodesCharactersEntity(it, characterId)
                        })

                    characterDao.updateForeignKeys(
                        characterId,
                        characterResponse.origin.id,
                        characterResponse.location.id
                    )
                }
            }
        }
    )

    suspend fun updateFavorite(characterId: Int, favorite: Boolean) {
        database.withTransaction {
            characterDao.updateFavorite(characterId, favorite)
        }
    }

    fun findFavorites(): Flow<List<CharacterItem>> {
        return characterDao.getFavorites().map { it.toCharacterItems() }
    }
}