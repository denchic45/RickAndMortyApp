package com.denchic45.rickandmorty.data.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EpisodeWithCharactersEntity(
    @Embedded
    val episode: EpisodeEntity,
    @Relation(
        parentColumn = "episode_id",
        entityColumn = "character_id",
        associateBy = Junction(EpisodesCharactersEntity::class)
    )
    val characters: List<CharacterEntity>
)