package com.nsicyber.vinylscan.domain.repository

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.model.response.discogs.getDetail.GetDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getSearch.GetSearchResponse
import kotlinx.coroutines.flow.Flow


interface DiscogsNetworkRepository {

    fun searchBarcode(
        barcode: String?,
    ): Flow<ApiResult<GetSearchResponse?>?>

    fun searchVinyl(
        query: String?,
    ): Flow<ApiResult<GetSearchResponse?>?>

    fun getDetail(
        masterId: Int?,
    ): Flow<ApiResult<GetDetailResponse?>?>

}