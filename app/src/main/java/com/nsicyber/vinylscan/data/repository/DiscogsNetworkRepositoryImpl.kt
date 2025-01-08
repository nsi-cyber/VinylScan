package com.nsicyber.vinylscan.data.repository

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.common.apiFlow
import com.nsicyber.vinylscan.data.model.response.discogs.getMasterDetail.GetMasterDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getSearch.GetSearchResponse
import com.nsicyber.vinylscan.data.remote.DiscogsApiService
import com.nsicyber.vinylscan.domain.repository.DiscogsNetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DiscogsNetworkRepositoryImpl @Inject constructor(
    private val api: DiscogsApiService,
) : DiscogsNetworkRepository {


    override fun searchBarcode(
        barcode: String?,
    ): Flow<ApiResult<GetSearchResponse?>?> = apiFlow {
        api.searchBarcode(barcode = barcode)
    }

    override fun searchVinyl(
        query: String?,
    ): Flow<ApiResult<GetSearchResponse?>?> = apiFlow {
        api.searchVinyl(query = query)
    }

    override fun getMasterDetail(masterId: Int?): Flow<ApiResult<GetMasterDetailResponse?>?> =
        apiFlow {
            api.getMasterDetail(masterId = masterId)
        }

    override fun getReleaseDetail(releaseId: Int?): Flow<ApiResult<GetReleaseDetailResponse?>?> =
        apiFlow {
            api.getReleaseDetail(releaseId = releaseId)
        }
}