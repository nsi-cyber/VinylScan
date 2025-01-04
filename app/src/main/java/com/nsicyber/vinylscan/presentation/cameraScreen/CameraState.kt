package com.nsicyber.vinylscan.presentation.cameraScreen

import com.nsicyber.vinylscan.data.model.response.deezer.getAlbumDetail.GetAlbumDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getDetail.GetDetailResponse


data class CameraState(
    val barcode: String = "",
    val onSuccess: Boolean = false,
    val isPageLoading: Boolean = false,
    val deezerAlbumDetail: GetAlbumDetailResponse? = null,
    val discogsAlbumDetail: GetDetailResponse? = null,

    )