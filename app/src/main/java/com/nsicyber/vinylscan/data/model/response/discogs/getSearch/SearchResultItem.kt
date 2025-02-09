package com.nsicyber.vinylscan.data.model.response.discogs.getSearch

import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.Community
import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.Format

data class SearchResultItem(
    val cover_image: String?,
    val title: String?,
    val year: String?,
    val type: String?,
    val barcode: List<String?>?,
    val master_id: Int?,
    val community: Community?,
    val id: Int?,
    val formats: List<Format?>?,

    )