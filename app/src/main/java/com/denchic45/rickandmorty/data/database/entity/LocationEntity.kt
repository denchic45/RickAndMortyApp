package com.denchic45.rickandmorty.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey()
    @ColumnInfo("location_id")
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)