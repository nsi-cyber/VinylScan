package com.nsicyber.vinylscan.common

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response


val gson = Gson()

fun <T> apiFlow(call: suspend () -> Response<T>?): Flow<ApiResult<T>> =
    flow {
        try {
            val c = call()
            c?.let {
                if (c.isSuccessful) {
                    emit(ApiResult.Success(c.body()))
                } else {
                    val errorBody = c.errorBody()?.string()
                    val errorResponse = gson.fromJson(errorBody, ApiErrorResponse::class.java)
                    emit(ApiResult.Error(errorResponse.clientMessage))
                }
            }
        } catch (e: Exception) {
            Log.e("ApiFlow", e.message ?: "An error occurred")
            emit(ApiResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)
