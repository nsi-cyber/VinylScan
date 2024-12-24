package com.nsicyber.vinylscan.data.repository

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.common.apiFlow
import com.nsicyber.vinylscan.data.model.response.getDetail.GetDetailResponse
import com.nsicyber.vinylscan.data.model.response.getSearch.GetSearchResponse
import com.nsicyber.vinylscan.data.remote.ApiService
import com.nsicyber.vinylscan.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class NetworkRepositoryImpl @Inject constructor(
    private val api: ApiService,
) : NetworkRepository {


    override fun searchBarcode(
        barcode: String?,
    ): Flow<ApiResult<GetSearchResponse?>?> = apiFlow {
        api.searchBarcode(barcode = barcode)
    }

    override fun getDetail(masterId: Int?): Flow<ApiResult<GetDetailResponse?>?> = apiFlow {
        api.getDetail(masterId = masterId)
    }
}