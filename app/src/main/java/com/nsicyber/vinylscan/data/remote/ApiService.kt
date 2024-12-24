package com.nsicyber.vinylscan.data.remote

import com.nsicyber.vinylscan.BuildConfig
import com.nsicyber.vinylscan.common.Constants
import com.nsicyber.vinylscan.data.model.response.getDetail.GetDetailResponse
import com.nsicyber.vinylscan.data.model.response.getSearch.GetSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {


    @GET(Constants.Endpoints.SEARCH)
    suspend fun searchBarcode(
        @Query("barcode") barcode: String?,
        @Query("per_page") per_page: Int=1,
        @Query("page") page: Int=1,
        @Query("token") token: String= BuildConfig.API_KEY,

        ): Response<GetSearchResponse?>?

    @GET(Constants.Endpoints.GET_DETAIL)
    suspend fun getDetail(
        @Path("masterId") masterId: Int?,
    ): Response<GetDetailResponse?>?


}