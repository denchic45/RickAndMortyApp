package com.denchic45.rickandmorty.domain.usecase

import com.denchic45.rickandmorty.data.repository.EpisodeRepository

class FindEpisodeByIdUseCase(private val episodeRepository: EpisodeRepository) {
    operator fun invoke(episodeId: Int) = episodeRepository.findById(episodeId)
}