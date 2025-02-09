package com.nsicyber.vinylscan.domain.model

data class VinylModel(
    val id: Int?,
    val title: String?,
    val vinylQuantity: Int? = 1,
    val artistName: String?,
    val genres: String?,
    val formatType: String?,
    val styles: String?,
    val releaseDate: String?,
    val year: String?,
    val totalTime: String?,
    val barcode: String?,
    val catalogNo: String?,
    val catalogLabel: String?,
    val minPrice: String?,
    val images: List<String?>?,
    val tracks: List<VinylTrackModel?>?
)
