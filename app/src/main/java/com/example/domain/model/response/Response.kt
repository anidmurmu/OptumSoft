package com.example.domain.model.response

sealed class Response<out T> {
    data class Failure<T>(val error: Throwable) : Response<T>()
    data class Success<T>(val data: T) : Response<T>()
}