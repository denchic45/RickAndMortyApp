package com.denchic45.rickandmorty.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "resident",
    primaryKeys = ["location_id", "character_id"],
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["location_id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["character_id"],
            childColumns = ["character_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ResidentEntity(
    @ColumnInfo("location_id")
    val locationId: Int,
    @ColumnInfo("character_id")
    val characterId: Int
)