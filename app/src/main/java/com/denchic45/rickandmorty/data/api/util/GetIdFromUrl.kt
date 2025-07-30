package com.denchic45.rickandmorty.data.api.util

import io.ktor.http.Url

val String.getIdFromUrl: Int
    get() = Url(this).segments.last().toInt()