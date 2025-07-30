package com.denchic45.rickandmorty.data.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class LocationWithResidentsEntity(
    @Embedded
    val location: LocationEntity,
    @Relation(
        parentColumn = "location_id",
        entityColumn = "character_id",
        associateBy = Junction(ResidentEntity::class)
    )
    val residents: List<CharacterEntity>
)