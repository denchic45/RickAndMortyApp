package com.denchic45.rickandmorty.domain.model


data class EpisodeDetails(
    val episode: EpisodeItem,
    val characters: List<CharacterItem>
)