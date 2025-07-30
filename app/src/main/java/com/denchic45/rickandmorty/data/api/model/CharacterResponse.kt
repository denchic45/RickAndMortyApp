package com.denchic45.rickandmorty.data.api.model

import com.denchic45.rickandmorty.data.api.util.getIdFromUrl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val origin: Origin,
    val location: CharacterLocation,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
) {
    val episodeIds = episode.map { it.getIdFromUrl }
}

@Serializable
data class Origin(
    val name: String,
    val url: String,
) {
    val id = url.takeIf(String::isNotEmpty)?.getIdFromUrl
}

@Serializable
data class CharacterLocation(
    val name: String,
    val url: String,
) {
    val id = url.takeIf(String::isNotEmpty)?.getIdFromUrl
}

enum class CharacterStatus {
    @SerialName("Alive")
    ALIVE,

    @SerialName("Dead")
    DEAD,

    @SerialName("unknown")
    UNKNOWN
}

enum class CharacterGender {
    @SerialName("Female")
    FEMALE,

    @SerialName("Male")
    MALE,

    @SerialName("Genderless")
    GENDERLESS,

    @SerialName("unknown")
    UNKNOWN
}