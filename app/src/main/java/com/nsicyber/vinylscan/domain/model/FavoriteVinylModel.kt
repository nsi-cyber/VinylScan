package com.nsicyber.vinylscan.domain.model

data class FavoriteVinylModel(
    val vinylId: Int,
    val title: String,
    val releaseDate: String,
    val image: String?
) 