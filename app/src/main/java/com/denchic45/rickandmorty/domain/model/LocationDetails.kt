package com.denchic45.rickandmorty.domain.model

data class LocationDetails(
    val location: LocationItem,
    val residents: List<CharacterItem>
)