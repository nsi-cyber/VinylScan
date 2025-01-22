package com.nsicyber.vinylscan.presentation.detailScreen

import com.nsicyber.vinylscan.domain.model.VinylModel


sealed class DetailScreenEvent {


    data class LoadTrack(
        val query: String?,
    ) : DetailScreenEvent()

    data class LoadScreen(
        val vinylId:Int?
    ) : DetailScreenEvent()


    data class ToggleFavorite(val vinyl: VinylModel) : DetailScreenEvent()

    data object SetStateEmpty : DetailScreenEvent()


}