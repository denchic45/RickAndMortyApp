package com.denchic45.rickandmorty.data.mapper

import com.denchic45.rickandmorty.data.api.model.LocationResponse
import com.denchic45.rickandmorty.data.database.entity.LocationEntity
import com.denchic45.rickandmorty.domain.model.LocationItem

fun LocationResponse.toLocationEntity() = LocationEntity(
    id = id,
    name = name,
    type = type,
    dimension = dimension
)

fun LocationEntity.toLocationItem() = LocationItem(
    id = id,
    name = name,
    type = type,
    dimension = dimension
)