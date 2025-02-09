package com.nsicyber.vinylscan.presentation.detailScreen

data class DetailScreenState(
    val onSuccess: Boolean = false,
    val isPageLoading: Boolean = false,
    val onBottomSheetError: Boolean = false,
    val previewTrackModel: PreviewTrackModel? = null,

    )

data class PreviewTrackModel(
    val artistName: String?,
    val title: String?,
    val album: String?,
    val preview: String?,
    val cover: String?,
)