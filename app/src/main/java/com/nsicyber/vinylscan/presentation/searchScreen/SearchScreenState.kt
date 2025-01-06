package com.nsicyber.vinylscan.presentation.searchScreen

import com.nsicyber.vinylscan.data.model.response.discogs.getSearch.SearchResultItem
import com.nsicyber.vinylscan.domain.model.VinylModel


data class SearchScreenState(
    val searchQuery: String = "",
    val isPageLoading: Boolean = false,
    val searchSearchResultItem: List<SearchResultItem?>? = null,
    val vinylModel: VinylModel? = null,
    val onSuccess: Boolean = false,

    )
