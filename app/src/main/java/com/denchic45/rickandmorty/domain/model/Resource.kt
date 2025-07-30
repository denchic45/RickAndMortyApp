package com.denchic45.rickandmorty.domain.model

import com.denchic45.rickandmorty.data.api.util.ApiException
import kotlinx.io.IOException


sealed interface Failure

data object NoConnection : Failure

data class ApiError(
    val code: Int,
    val body: String
) : Failure

data class ThrowableError(
    val throwable: Throwable
) : Failure

fun Throwable.asFailure(): Failure = when (this) {
    is ApiException -> ApiError(
        this.code,
        this.message.orEmpty()
    )

    is IOException -> NoConnection
    else -> ThrowableError(this)
}