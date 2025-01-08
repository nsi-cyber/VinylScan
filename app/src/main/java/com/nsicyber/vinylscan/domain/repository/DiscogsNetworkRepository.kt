package com.nsicyber.vinylscan.domain.repository

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.model.response.discogs.getMasterDetail.GetMasterDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getSearch.GetSearchResponse
import kotlinx.coroutines.flow.Flow


interface DiscogsNetworkRepository {

    fun searchBarcode(
        barcode: String?,
    ): Flow<ApiResult<GetSearchResponse?>?>

    fun searchVinyl(
        query: String?,
    ): Flow<ApiResult<GetSearchResponse?>?>

    fun getMasterDetail(
        masterId: Int?,
    ): Flow<ApiResult<GetMasterDetailResponse?>?>

    fun getReleaseDetail(
        releaseId: Int?,
    ): Flow<ApiResult<GetReleaseDetailResponse?>?>

}