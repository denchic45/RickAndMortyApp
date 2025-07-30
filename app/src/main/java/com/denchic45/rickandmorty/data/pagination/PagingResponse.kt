package com.denchic45.rickandmorty.data.pagination

import kotlinx.serialization.Serializable

@Serializable
class PagingResponse<T>(
    val info: PagingInfo,
    val results: List<T>
)

@Serializable
data class PagingInfo(
    val count: Int,
    val pages: Int,
    val prev: String?,
    val next: String?
)