package com.denchic45.rickandmorty.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class EpisodeItem(
    val id: Int,
    val name: String,
    val airDate: LocalDate,
    val episode: String
) {
    val airDateText: String = airDate.format(
        DateTimeFormatter
            .ofPattern("MMMM d, yyyy", Locale.US)
    )
}