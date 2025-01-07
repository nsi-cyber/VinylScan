package com.nsicyber.vinylscan.domain.model

import kotlin.time.Duration


data class VinylTrackModel(
    val title: String?,
    val position: String?,
    val duration: String?,
    val artistName:String?,
    val albumName:String?
)