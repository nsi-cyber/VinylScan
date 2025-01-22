package com.nsicyber.vinylscan.domain.useCases.favorite

import com.nsicyber.vinylscan.domain.model.FavoriteVinylModel
import com.nsicyber.vinylscan.domain.repository.DatabaseRepository
 import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
    private val repository: DatabaseRepository
) {
    suspend operator fun invoke(vinyl: FavoriteVinylModel) {
        repository.addToFavorites(vinyl)
    }
} 