package com.nsicyber.vinylscan.domain.useCases.favorite

import com.nsicyber.vinylscan.domain.model.FavoriteVinylModel
import com.nsicyber.vinylscan.domain.repository.DatabaseRepository
 import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: DatabaseRepository
) {
    operator fun invoke(): Flow<List<FavoriteVinylModel>> {
        return repository.getAllFavorites()
    }
} 