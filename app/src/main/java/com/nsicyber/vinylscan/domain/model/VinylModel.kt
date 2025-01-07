package com.nsicyber.vinylscan.domain.model

data class VinylModel(
    val title:String?,
    val vinylQuantity:Int?=1,
    val artistName:String?,
    val genres:String?,
    val styles:String?,
    val releaseDate:String?,
    val totalTime:String?,
    val barcode:String?,
    val catalog:String?,
    val minPrice:String?,
    val images:List<String?>?,
    val tracks:List<VinylTrackModel?>?
)
