package com.nsicyber.vinylscan.data.model.response.discogs.getSearch

data class SearchResultItem(

    val cover_image: String?,
    val title: String?,
    val year: String?,
    val barcode: List<String?>?,


    val master_id: Int?,
)