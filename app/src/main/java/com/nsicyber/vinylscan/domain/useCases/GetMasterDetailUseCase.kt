package com.nsicyber.vinylscan.domain.useCases

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.model.response.discogs.getMasterDetail.GetMasterDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.nsicyber.vinylscan.domain.repository.DiscogsNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetMasterDetailUseCase
@Inject
constructor(
    private val repo: DiscogsNetworkRepository,
) {
    operator fun invoke(masterId: Int?): Flow<ApiResult<GetMasterDetailResponse?>?> =
        flow {
            try {
                repo.getMasterDetail(masterId).collect { result ->
                    emit(result)
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(message = e.message.toString()))
            }
        }
}

