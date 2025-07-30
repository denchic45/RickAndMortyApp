package com.denchic45.rickandmorty.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.SET_NULL
import androidx.room.PrimaryKey
import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus

@Entity(
    tableName = "character",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["location_id"],
            childColumns = ["origin_id"],
            onDelete = SET_NULL,
            onUpdate = SET_NULL
        ),
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["location_id"],
            childColumns = ["location_id"],
            onDelete = SET_NULL,
            onUpdate = SET_NULL
        )
    ]
)
data class CharacterEntity(
    @PrimaryKey()
    @ColumnInfo("character_id")
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val image: String,
    val timestamp: Long,
    @ColumnInfo("origin_id")
    val originId: Int?,
    @ColumnInfo("location_id")
    val locationId: Int?,
    @ColumnInfo("favorite", defaultValue = "0")
    val favorite: Boolean? = null
)

data class PartialCharacterEntity(
    @PrimaryKey()
    @ColumnInfo("character_id")
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val image: String,
    val timestamp: Long
)