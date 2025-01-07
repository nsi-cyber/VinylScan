package com.nsicyber.vinylscan.domain.useCases

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.nsicyber.vinylscan.domain.repository.DiscogsNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetReleaseDetailUseCase
@Inject
constructor(
    private val repo: DiscogsNetworkRepository,
) {
    operator fun invoke(releaseId: Int?): Flow<ApiResult<GetReleaseDetailResponse?>?> =
        flow {
            try {
                repo.getReleaseDetail(releaseId = releaseId).collect { result ->
                    emit(result)
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(message = e.message.toString()))
            }
        }
}

