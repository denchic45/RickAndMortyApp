package com.denchic45.rickandmorty.data.api.util

class ApiException(val code: Int, message: String) : Exception(message)