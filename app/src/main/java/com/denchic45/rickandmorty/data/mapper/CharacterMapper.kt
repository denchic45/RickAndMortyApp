package com.denchic45.rickandmorty.data.mapper

import com.denchic45.rickandmorty.data.api.model.CharacterResponse
import com.denchic45.rickandmorty.data.database.entity.CharacterEntity
import com.denchic45.rickandmorty.data.database.entity.PartialCharacterEntity
import com.denchic45.rickandmorty.domain.model.CharacterItem


fun List<CharacterResponse>.toCharacterEntities() = map { response ->
    response.toCharacterEntity()
}

fun CharacterResponse.toCharacterEntity() = PartialCharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    image = image,
    timestamp = System.currentTimeMillis()
)

fun List<CharacterEntity>.toCharacterItems() = map(CharacterEntity::toCharacterItem)

fun CharacterEntity.toCharacterItem() = CharacterItem(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    image = image,
    favorite = favorite ?: false
)