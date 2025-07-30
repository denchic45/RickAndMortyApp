package com.denchic45.rickandmorty.di.module

import com.denchic45.rickandmorty.data.repository.CharacterRepository
import com.denchic45.rickandmorty.data.repository.EpisodeRepository
import com.denchic45.rickandmorty.data.repository.LocationRepository
import org.koin.dsl.module

val dataModule = module {
    includes(apiModule, databaseModule)
    single {
        CharacterRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        LocationRepository(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        EpisodeRepository(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}