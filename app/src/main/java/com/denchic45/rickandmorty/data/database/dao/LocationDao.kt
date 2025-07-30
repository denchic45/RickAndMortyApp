package com.denchic45.rickandmorty.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.denchic45.rickandmorty.data.database.entity.LocationEntity
import com.denchic45.rickandmorty.data.database.entity.LocationWithResidentsEntity
import com.denchic45.rickandmorty.data.database.entity.ResidentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Upsert(entity = LocationEntity::class)
    suspend fun upsert(entity: LocationEntity): Long

    @Query("SELECT * FROM location WHERE location_id=:id")
    fun observeById(id: Int): Flow<LocationWithResidentsEntity>

    @Query("SELECT * FROM location WHERE location_id=:id")
    suspend fun getById(id: Int): LocationEntity

    @Upsert
    fun upsertResidents(entities: List<ResidentEntity>)
} 