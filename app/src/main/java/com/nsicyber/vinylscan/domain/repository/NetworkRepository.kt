package com.nsicyber.vinylscan.domain.repository

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.common.Constants
import com.nsicyber.vinylscan.data.model.response.getDetail.GetDetailResponse
import com.nsicyber.vinylscan.data.model.response.getSearch.GetSearchResponse
import kotlinx.coroutines.flow.Flow


interface NetworkRepository {

     fun searchBarcode(
     barcode: String?,
    ): Flow<ApiResult<GetSearchResponse?>?>

     fun getDetail(
      masterId: Int?,
    ): Flow<ApiResult<GetDetailResponse?>?>

}