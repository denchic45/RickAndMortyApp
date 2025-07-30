package com.denchic45.rickandmorty.data.api.model

import com.denchic45.rickandmorty.data.api.util.getIdFromUrl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeResponse(
    val id: Int,
    val name: String,
    @SerialName("air_date")
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String,
) {
    val characterIds = characters.map(String::getIdFromUrl)
}