package com.nsicyber.vinylscan.presentation.favoriteScreen

sealed class FavoriteScreenEvent {
    data object LoadScreen : FavoriteScreenEvent()
    data object SetStateEmpty : FavoriteScreenEvent()
    data object DetailOpened : FavoriteScreenEvent()

    data class OpenDetail(val id: Int) : FavoriteScreenEvent()
} 