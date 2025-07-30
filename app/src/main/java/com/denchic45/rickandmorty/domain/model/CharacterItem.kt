package com.denchic45.rickandmorty.domain.model

import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus

data class CharacterItem(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val image: String,
    val favorite: Boolean
)