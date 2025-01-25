package com.nsicyber.vinylscan.presentation.favoriteScreen

import com.nsicyber.vinylscan.domain.model.FavoriteVinylModel
import com.nsicyber.vinylscan.domain.model.VinylModel

data class FavoriteScreenState(
    val isPageLoading: Boolean = false,
    val favoriteVinyls: List<FavoriteVinylModel>? = null,
    val onSuccess: Boolean = false,
    val vinylModel: VinylModel? = null
)