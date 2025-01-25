package com.nsicyber.vinylscan.data.remote

import com.nsicyber.vinylscan.BuildConfig
import com.nsicyber.vinylscan.common.Constants
import com.nsicyber.vinylscan.data.model.response.discogs.getMasterDetail.GetMasterDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getSearch.GetSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DiscogsApiService {


    @GET(Constants.Endpoints.Discogs.SEARCH)
    suspend fun searchBarcode(
        @Query("barcode") barcode: String?,
        @Query("per_page") per_page: Int = 1,
        @Query("page") page: Int = 1,
        @Query("token") token: String = BuildConfig.API_KEY,
    ): Response<GetSearchResponse?>?

    @GET(Constants.Endpoints.Discogs.SEARCH)
    suspend fun searchVinyl(
        @Query("query") query: String?,
        @Query("type") type: String="release",
        @Query("per_page") per_page: Int = 100,
        @Query("page") page: Int = 1,
        @Query("token") token: String = BuildConfig.API_KEY,
    ): Response<GetSearchResponse?>?

    @GET(Constants.Endpoints.Discogs.GET_MASTER_DETAIL)
    suspend fun getMasterDetail(
        @Path("masterId") masterId: Int?,
        @Query("token") token: String = BuildConfig.API_KEY,
    ): Response<GetMasterDetailResponse?>?

    @GET(Constants.Endpoints.Discogs.GET_RELEASE_DETAIL)
    suspend fun getReleaseDetail(
        @Path("releaseId") releaseId: Int?,
        @Query("token") token: String = BuildConfig.API_KEY,
    ): Response<GetReleaseDetailResponse?>?


}