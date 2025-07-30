package com.denchic45.rickandmorty.di.module

import com.denchic45.rickandmorty.domain.usecase.FindCharacterByIdUseCase
import com.denchic45.rickandmorty.domain.usecase.FindEpisodeByIdUseCase
import com.denchic45.rickandmorty.domain.usecase.FindFavoritesCharactersUseCase
import com.denchic45.rickandmorty.domain.usecase.FindLocationByIdUseCase
import com.denchic45.rickandmorty.domain.usecase.QueryCharactersUseCase
import com.denchic45.rickandmorty.domain.usecase.UpdateCharacterToFavoriteUseCase
import org.koin.dsl.module

val domainModule = module {
    includes(dataModule)
    single { QueryCharactersUseCase(get()) }
    single { FindCharacterByIdUseCase(get()) }
    single { UpdateCharacterToFavoriteUseCase(get()) }
    single { FindEpisodeByIdUseCase(get()) }
    single { FindEpisodeByIdUseCase(get()) }
    single { FindLocationByIdUseCase(get()) }
    single { FindFavoritesCharactersUseCase(get()) }
}