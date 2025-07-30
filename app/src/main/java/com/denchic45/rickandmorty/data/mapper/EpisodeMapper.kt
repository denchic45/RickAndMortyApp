package com.denchic45.rickandmorty.data.mapper

import com.denchic45.rickandmorty.data.api.model.EpisodeResponse
import com.denchic45.rickandmorty.data.database.entity.EpisodeEntity
import com.denchic45.rickandmorty.data.database.entity.EpisodeWithCharactersEntity
import com.denchic45.rickandmorty.domain.model.EpisodeDetails
import com.denchic45.rickandmorty.domain.model.EpisodeItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun List<EpisodeResponse>.toEpisodeEntities() = map(EpisodeResponse::toEpisodeEntity)

fun EpisodeResponse.toEpisodeEntity() = EpisodeEntity(
    id = id,
    name = name,
    airDate = airDate,
    episode = episode
)

fun List<EpisodeEntity>.toEpisodeItems() = map(EpisodeEntity::toEpisodeItem)

fun EpisodeEntity.toEpisodeItem() = EpisodeItem(
    id = id,
    name = name,
    airDate = LocalDate.parse(airDate, DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US)),
    episode = episode
)

fun EpisodeWithCharactersEntity.toEpisodeDetailsItem() = EpisodeDetails(
    episode = episode.toEpisodeItem(),
    characters = characters.toCharacterItems()
)