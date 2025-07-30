package com.denchic45.rickandmorty.data.repository

import androidx.room.withTransaction
import com.denchic45.rickandmorty.data.api.CharacterApi
import com.denchic45.rickandmorty.data.api.LocationApi
import com.denchic45.rickandmorty.data.database.AppDatabase
import com.denchic45.rickandmorty.data.database.dao.CharacterDao
import com.denchic45.rickandmorty.data.database.dao.LocationDao
import com.denchic45.rickandmorty.data.database.entity.ResidentEntity
import com.denchic45.rickandmorty.data.mapper.toCharacterEntities
import com.denchic45.rickandmorty.data.mapper.toCharacterItems
import com.denchic45.rickandmorty.data.mapper.toLocationEntity
import com.denchic45.rickandmorty.data.mapper.toLocationItem
import com.denchic45.rickandmorty.data.observeResource
import com.denchic45.rickandmorty.domain.model.LocationDetails
import com.denchic45.rickandmorty.domain.model.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepository(
    private val database: AppDatabase,
    private val characterDao: CharacterDao,
    private val locationDao: LocationDao,
    private val characterApi: CharacterApi,
    private val locationApi: LocationApi
) {

    fun findById(locationId: Int): Flow<Resource<LocationDetails>> {
        return observeResource(
            query = locationDao.observeById(locationId).map {
                LocationDetails(
                    it.location.toLocationItem(),
                    it.residents.toCharacterItems()
                )
            },
            fetch = {
                coroutineScope {
                    val locationResponse = locationApi.getById(locationId)
                    database.withTransaction {
                        characterDao.upsert(
                            characterApi.getByIds(
                                locationResponse.residentIds
                            ).toCharacterEntities()
                        )
                        locationDao.upsertResidents(locationResponse.residentIds.map {
                            ResidentEntity(
                                locationId,
                                it
                            )
                        })
                        locationDao.upsert(locationResponse.toLocationEntity())
                    }
                }
            }
        )
    }
}