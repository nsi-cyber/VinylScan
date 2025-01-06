package com.nsicyber.vinylscan.data.remote

import com.nsicyber.vinylscan.common.Constants
import com.nsicyber.vinylscan.data.model.response.deezer.search.GetSearchResponse
import retrofit2.Response
import retrofit2.http.GET
 import retrofit2.http.Query


interface DeezerApiService {


    @GET(Constants.Endpoints.Deezer.SEARCH)
    suspend fun search(
        @Query("q") query: String?,
        @Query("limit") limit: Int = 1,
        ): Response<GetSearchResponse?>?




}