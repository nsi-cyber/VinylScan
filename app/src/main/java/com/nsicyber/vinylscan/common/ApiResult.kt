package com.nsicyber.vinylscan.common

sealed class ApiResult<out T> {
    data class Success<out R>(val data: R?) : ApiResult<R>()

    data class Error(val message: String) : ApiResult<Nothing>()
}

data class ApiErrorResponse(
    val clientMessage: String,
)

sealed class DaoResult<out T> {
    data class Success<out T>(val data: T?) : DaoResult<T>()

    data class Error(val message: String) : DaoResult<Nothing>()
}