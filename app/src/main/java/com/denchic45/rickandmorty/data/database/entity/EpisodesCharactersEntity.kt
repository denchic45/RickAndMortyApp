package com.denchic45.rickandmorty.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "episode_character",
    primaryKeys = ["episode_id", "character_id"],
    foreignKeys = [
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = ["episode_id"],
            childColumns = ["episode_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["character_id"],
            childColumns = ["character_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class EpisodesCharactersEntity(
    @ColumnInfo("episode_id")
    val episodeId: Int,
    @ColumnInfo("character_id")
    val characterId: Int
)
