package com.nsicyber.vinylscan.domain.model

data class VinylModel(
    val title:String?,
    val artistName:String?,
    val cover:String?,
    val genres:String?,
    val styles:String?,
    val releaseDate:String?,
    val totalTime:String?,
    val barcode:String?,
    val tracks:List<VinylTrackModel?>?
)
