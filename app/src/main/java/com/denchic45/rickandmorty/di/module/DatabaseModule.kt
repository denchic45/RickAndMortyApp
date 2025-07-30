package com.denchic45.rickandmorty.di.module

import android.content.Context
import androidx.room.Room
import com.denchic45.rickandmorty.data.database.AppDatabase
import org.koin.dsl.module


val databaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>().applicationContext,
            AppDatabase::class.java,
            "database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<AppDatabase>().characterDao }
    single { get<AppDatabase>().locationDao }
    single { get<AppDatabase>().episodeDao }
}