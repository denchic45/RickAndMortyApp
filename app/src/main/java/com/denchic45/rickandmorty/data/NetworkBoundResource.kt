package com.denchic45.rickandmorty.data

import com.denchic45.rickandmorty.domain.model.Resource
import com.denchic45.rickandmorty.domain.model.asFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


fun <T> observeResource(
    query: Flow<T>,
    fetch: suspend () -> Unit,
    shouldFetch: (T) -> Boolean = { true },
): Flow<Resource<T>> = flow {
    val first = query.first()
    if (shouldFetch(first)) {
        emit(Resource.Loading)
        val result = runCatching { fetch() }
        result
            .onFailure { throwable ->
                emit(
                    Resource.Failed(
                        throwable.asFailure(), first
                    )
                )
            }

        emitAll(query.map {
            result.exceptionOrNull()?.let { throwable ->
                Resource.Failed(throwable.asFailure(), it)
            } ?: Resource.Success(it)
        })
    } else emitAll(query.map { Resource.Success(it) })
}