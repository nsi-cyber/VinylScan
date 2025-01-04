package com.nsicyber.vinylscan.data.repository

import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.common.apiFlow
import com.nsicyber.vinylscan.data.model.response.deezer.getAlbumDetail.GetAlbumDetailResponse
import com.nsicyber.vinylscan.data.model.response.deezer.search.GetSearchResponse
import com.nsicyber.vinylscan.data.remote.DeezerApiService
import com.nsicyber.vinylscan.domain.repository.DeezerNetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DeezerNetworkRepositoryImpl @Inject constructor(
    private val api: DeezerApiService,
) : DeezerNetworkRepository {


    override fun search(
        query: String?,
    ): Flow<ApiResult<GetSearchResponse?>?> =
        apiFlow {
            api.search(query = query)
        }

    override fun getDetail(albumId: String?): Flow<ApiResult<GetAlbumDetailResponse?>?> = apiFlow {
        api.getAlbumDetail(albumId = albumId)
    }
}