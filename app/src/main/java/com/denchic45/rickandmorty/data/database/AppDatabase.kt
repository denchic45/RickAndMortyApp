package com.denchic45.rickandmorty.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.denchic45.rickandmorty.data.database.dao.CharacterDao
import com.denchic45.rickandmorty.data.database.dao.EpisodeDao
import com.denchic45.rickandmorty.data.database.dao.LocationDao
import com.denchic45.rickandmorty.data.database.entity.*

@Database(
    version = 1,
    entities = [CharacterEntity::class, LocationEntity::class, ResidentEntity::class, EpisodeEntity::class, EpisodesCharactersEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val characterDao: CharacterDao
    abstract val locationDao: LocationDao
    abstract val episodeDao: EpisodeDao
}