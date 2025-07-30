package com.denchic45.rickandmorty.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("episode")
data class EpisodeEntity(
    @PrimaryKey()
    @ColumnInfo("episode_id")
    val id: Int,
    val name: String,
    @ColumnInfo("air_date")
    val airDate: String,
    val episode: String
)