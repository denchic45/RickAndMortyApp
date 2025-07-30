package com.denchic45.rickandmorty.ui

import kotlinx.serialization.Serializable

@Serializable
class Navigation

@Serializable
object Characters

@Serializable
object CharactersFilters

@Serializable
object Favorites

@Serializable
data class CharacterDetails(val characterId: Int)

@Serializable
data class EpisodeDetails(val episodeId: Int)

@Serializable
data class LocationDetails(val locationId: Int)