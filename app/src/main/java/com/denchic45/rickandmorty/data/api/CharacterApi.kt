package com.denchic45.rickandmorty.data.api

import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterResponse
import com.denchic45.rickandmorty.data.api.model.CharacterStatus
import com.denchic45.rickandmorty.data.api.util.safeApiCall
import com.denchic45.rickandmorty.data.pagination.PagingResponse
import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.collections.joinToString

class CharacterApi(private val client: HttpClient) {
    suspend fun query(
        page: Int,
        name: String? = null,
        status: CharacterStatus? = null,
        species: String? = null,
        type: String? = null,
        gender: CharacterGender? = null
    ): PagingResponse<CharacterResponse> {
        return client.safeApiCall {
            get("character") {
                parameter("page", page)
                name?.let { parameter("name", it) }
                status?.let { parameter("status", it) }
                species?.let { parameter("species", it) }
                type?.let { parameter("type", it) }
                gender?.let { parameter("gender", it) }
            }
        }
    }

    suspend fun getById(characterId: Int): CharacterResponse {
        return client.safeApiCall { get("character/$characterId") }
    }

    suspend fun getByIds(characterIds: List<Int>): List<CharacterResponse> {
        return client.safeApiCall { get("character/${characterIds.joinToString(separator = ",")}") }
    }
}