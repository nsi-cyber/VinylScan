package com.nsicyber.vinylscan.presentation.favoriteScreen

import com.nsicyber.vinylscan.presentation.searchScreen.SearchScreenEvent

sealed class FavoriteScreenEvent {
    data object LoadScreen : FavoriteScreenEvent()
    data object SetStateEmpty : FavoriteScreenEvent()
    data object DetailOpened : FavoriteScreenEvent()

    data class OpenDetail(val id: Int) : FavoriteScreenEvent()
} 