package com.nsicyber.vinylscan.domain.useCases

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.model.response.deezer.getAlbumDetail.GetAlbumDetailResponse
import com.nsicyber.vinylscan.domain.repository.DeezerNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetAlbumDetailUseCase
@Inject
constructor(
    private val repo: DeezerNetworkRepository,
) {
    operator fun invoke(albumId: String?): Flow<ApiResult<GetAlbumDetailResponse?>?> =
        flow {
            try {
                repo.getDetail(albumId = albumId).collect { result ->
                    emit(result)
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(message = e.message.toString()))
            }
        }
}
