package com.denchic45.rickandmorty.data.api.model

import com.denchic45.rickandmorty.data.api.util.getIdFromUrl
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val created: String,
) {
    val residentIds = residents.map {
        it.getIdFromUrl
    }
}