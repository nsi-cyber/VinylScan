package com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail

data class ReleaseTracklist(
    val duration: String?,
    val extraartists: List<Extraartist?>?,
    val position: String?,
    val title: String?,
    val type_: String?
)