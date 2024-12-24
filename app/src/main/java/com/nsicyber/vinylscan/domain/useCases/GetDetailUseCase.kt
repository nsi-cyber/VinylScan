package com.nsicyber.vinylscan.domain.useCases

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.model.response.getDetail.GetDetailResponse
import com.nsicyber.vinylscan.data.model.response.getSearch.GetSearchResponse
import com.nsicyber.vinylscan.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetDetailUseCase
@Inject
constructor(
    private val repo: NetworkRepository,
) {
    operator fun invoke(masterId: Int?): Flow<ApiResult<GetDetailResponse?>?> =
        flow {
            try {
                repo.getDetail(masterId).collect { result ->
                    emit(result)
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(message = e.message.toString()))
            }
        }
}
