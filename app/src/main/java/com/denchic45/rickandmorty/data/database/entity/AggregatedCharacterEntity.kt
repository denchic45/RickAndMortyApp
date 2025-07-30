package com.denchic45.rickandmorty.data.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class AggregatedCharacterEntity(
    @Embedded
    val character: CharacterEntity,
    @Relation(
        parentColumn = "origin_id",
        entityColumn = "location_id"
    )
    val origin: LocationEntity?,
    @Relation(
        parentColumn = "location_id",
        entityColumn = "location_id"
    )
    val location: LocationEntity?,
    @Relation(
        parentColumn = "character_id",
        entityColumn = "episode_id",
        associateBy = Junction(EpisodesCharactersEntity::class)
    )
    val episodes: List<EpisodeEntity>
)