package com.denchic45.rickandmorty.data.api

import com.denchic45.rickandmorty.data.api.model.LocationResponse
import com.denchic45.rickandmorty.data.api.util.safeApiCall
import io.ktor.client.*
import io.ktor.client.request.*

class LocationApi(private val client: HttpClient) {

    suspend fun getById(locationId: Int): LocationResponse {
        return client.safeApiCall { get("location/$locationId") }
    }

    suspend fun getByIds(locationIds: List<Int>): List<LocationResponse> {
        return client.safeApiCall { get("location/${locationIds.joinToString(separator = ",")}") }
    }
}