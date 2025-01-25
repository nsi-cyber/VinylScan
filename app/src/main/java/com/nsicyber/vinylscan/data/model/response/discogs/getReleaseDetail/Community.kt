package com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail

data class Community(
    val contributors: List<Contributor?>?,
    val data_quality: String?,
    val have: Int? = 0,
    val rating: Rating?,
    val status: String?,
    val submitter: Submitter?,
    val want: Int? = 0
)