package com.nsicyber.vinylscan.data.remote

import com.nsicyber.vinylscan.common.Constants
import com.nsicyber.vinylscan.data.model.response.deezer.getAlbumDetail.GetAlbumDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DeezerApiService {


    @GET(Constants.Endpoints.Deezer.SEARCH)
    suspend fun search(
        @Query("q") query: String?,
        @Query("limit") limit: Int = 1,

        ): Response<com.nsicyber.vinylscan.data.model.response.deezer.search.GetSearchResponse?>?

    @GET(Constants.Endpoints.Deezer.GET_ALBUM_DETAIL)
    suspend fun getAlbumDetail(
        @Path("albumId") albumId: String?,
    ): Response<GetAlbumDetailResponse?>?


}