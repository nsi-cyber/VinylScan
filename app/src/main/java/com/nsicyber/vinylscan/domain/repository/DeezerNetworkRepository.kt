package com.nsicyber.vinylscan.domain.repository

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.model.response.deezer.getAlbumDetail.GetAlbumDetailResponse
import com.nsicyber.vinylscan.data.model.response.deezer.search.GetSearchResponse
import kotlinx.coroutines.flow.Flow


interface DeezerNetworkRepository {

    fun search(
        query: String?,
    ): Flow<ApiResult<GetSearchResponse?>?>

    fun getDetail(
        albumId: String?,
    ): Flow<ApiResult<GetAlbumDetailResponse?>?>

}

