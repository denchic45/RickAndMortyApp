package com.denchic45.rickandmorty.di.module

import com.denchic45.rickandmorty.ui.characterdetails.CharacterDetailsViewModel
import com.denchic45.rickandmorty.ui.characters.CharactersViewModel
import com.denchic45.rickandmorty.ui.episodedetails.EpisodeDetailsViewModel
import com.denchic45.rickandmorty.ui.favorites.FavoritesViewModel
import com.denchic45.rickandmorty.ui.locationdetails.LocationDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::CharactersViewModel)
    viewModelOf(::CharacterDetailsViewModel)
    viewModelOf(::EpisodeDetailsViewModel)
    viewModelOf(::LocationDetailsViewModel)
    viewModelOf(::FavoritesViewModel)
}