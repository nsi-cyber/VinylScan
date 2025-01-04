package com.nsicyber.vinylscan.data.model.response.discogs.getDetail

data class GetDetailResponse(
    val artists: List<Artist?>?,
    val genres: List<String?>?,
    val id: Int?,

    val main_release: Int?,

    val styles: List<String?>?,
    val title: String?,
    val thumbnail: String?,
    val tracklist: List<Tracklist?>?,
    val year: Int?
)