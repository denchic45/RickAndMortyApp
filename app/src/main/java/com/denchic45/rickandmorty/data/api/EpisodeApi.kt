package com.denchic45.rickandmorty.data.api

import com.denchic45.rickandmorty.data.api.model.EpisodeResponse
import com.denchic45.rickandmorty.data.api.util.safeApiCall
import io.ktor.client.*
import io.ktor.client.request.*

class EpisodeApi(private val client: HttpClient) {

    suspend fun getById(episodeId: Int): EpisodeResponse {
        return client.safeApiCall { get("episode/$episodeId") }
    }

    suspend fun getByIds(episodeIds: List<Int>): List<EpisodeResponse> {
        return client.safeApiCall { get("episode/${episodeIds.joinToString(separator = ",")}") }
    }
}