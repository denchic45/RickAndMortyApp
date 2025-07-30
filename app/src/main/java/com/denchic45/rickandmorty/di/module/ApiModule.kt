package com.denchic45.rickandmorty.di.module

import com.denchic45.rickandmorty.data.api.CharacterApi
import com.denchic45.rickandmorty.data.api.EpisodeApi
import com.denchic45.rickandmorty.data.api.LocationApi
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val apiModule = module {
    single {
        HttpClient(Android) {
            defaultRequest {
                url("https://rickandmortyapi.com/api/")
                headers.append(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging)
        }
    }
    single { CharacterApi(get()) }
    single { LocationApi(get()) }
    single { EpisodeApi(get()) }
}