package com.denchic45.rickandmorty.domain.usecase

import com.denchic45.rickandmorty.data.repository.LocationRepository

class FindLocationByIdUseCase(private val locationRepository: LocationRepository) {
    operator fun invoke(locationId: Int) = locationRepository.findById(locationId)
}