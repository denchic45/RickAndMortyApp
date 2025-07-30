package com.denchic45.rickandmorty.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.denchic45.rickandmorty.data.database.entity.EpisodeEntity
import com.denchic45.rickandmorty.data.database.entity.EpisodeWithCharactersEntity
import com.denchic45.rickandmorty.data.database.entity.EpisodesCharactersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Upsert(entity = EpisodeEntity::class)
    suspend fun upsert(entity: EpisodeEntity)

    @Upsert(entity = EpisodeEntity::class)
    suspend fun upsert(entities: List<EpisodeEntity>)

    @Upsert
    suspend fun upsertEpisodesCharacters(entities: List<EpisodesCharactersEntity>)

    @Query("SELECT * FROM episode WHERE episode_id=:id")
    fun getById(id: Int): Flow<EpisodeWithCharactersEntity?>

    @Query("SELECT e.* FROM episode e INNER JOIN episode_character ec ON e.episode_id = ec.episode_id  WHERE ec.character_id=:characterId")
    suspend fun getByCharacterId(characterId: Int): List<EpisodeEntity>
} 