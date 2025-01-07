package com.nsicyber.vinylscan.data.model.response.discogs.getMasterDetail

data class GetMasterDetailResponse(
    val artists: List<Artist?>?,
    val genres: List<String?>?,
    val id: Int?,
    val main_release: Int?,
    val styles: List<String?>?,
    val title: String?,
    val thumbnail: String?,
    val masterTracklist: List<MasterTracklist?>?,
    val year: Int?
)