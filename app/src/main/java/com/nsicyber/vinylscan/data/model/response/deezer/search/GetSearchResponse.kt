package com.nsicyber.vinylscan.data.model.response.deezer.search

data class GetSearchResponse(
    val `data`: List<Data>,
    val next: String,
    val total: Int
)