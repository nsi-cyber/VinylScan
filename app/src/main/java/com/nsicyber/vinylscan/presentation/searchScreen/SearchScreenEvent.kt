package com.nsicyber.vinylscan.presentation.searchScreen

sealed class SearchScreenEvent {

    data object LoadScreen : SearchScreenEvent()
    data object SetStateEmpty : SearchScreenEvent()
    data object DetailOpened : SearchScreenEvent()
    data class Search(val query: String) : SearchScreenEvent()
    data class OpenDetail(val id: Int) : SearchScreenEvent()

}
