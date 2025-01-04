package com.nsicyber.vinylscan.domain.model

data class AlbumModel(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val year: Int,
    val artistName: String,
    val genres: List<String> = listOf(),
    val styles: List<String> = listOf(),
    val tracks: List<TrackModel> = listOf(),

    )

data class TrackModel(
    val position: String,
    val title: String,
    val duration: String
)