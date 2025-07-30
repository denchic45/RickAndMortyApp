package com.denchic45.rickandmorty.domain.model


data class CharacterDetails(
    val character: CharacterItem,
    val origin: LocationItem?,
    val location: LocationItem?,
    val episodes: List<EpisodeItem>
)
