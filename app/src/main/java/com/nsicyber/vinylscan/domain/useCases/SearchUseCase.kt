package com.nsicyber.vinylscan.domain.useCases

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.model.response.deezer.search.GetSearchResponse
import com.nsicyber.vinylscan.domain.repository.DeezerNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SearchUseCase
@Inject
constructor(
    private val repo: DeezerNetworkRepository,
) {
    operator fun invoke(query: String?): Flow<ApiResult<GetSearchResponse?>?> =
        flow {
            try {
                repo.search(query = query).collect { result ->
                    emit(result)
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(message = e.message.toString()))
            }
        }
}
