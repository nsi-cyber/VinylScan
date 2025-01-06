package com.nsicyber.vinylscan.presentation.detailScreen


sealed class DetailScreenEvent {


    data class LoadScreen(
        val query: String?
    ) : DetailScreenEvent()


    data object SetStateEmpty : DetailScreenEvent()


}