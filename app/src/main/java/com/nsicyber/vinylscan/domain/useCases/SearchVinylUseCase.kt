package com.nsicyber.vinylscan.domain.useCases

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.model.response.discogs.getSearch.GetSearchResponse
import com.nsicyber.vinylscan.domain.repository.DiscogsNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SearchVinylUseCase
@Inject
constructor(
    private val repo: DiscogsNetworkRepository,
) {
    operator fun invoke(query: String?): Flow<ApiResult<GetSearchResponse?>?> =
        flow {
            try {
                repo.searchVinyl(query).collect { result ->
                    emit(result)
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(message = e.message.toString()))
            }
        }
}
