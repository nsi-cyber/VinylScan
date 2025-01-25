package com.nsicyber.vinylscan.domain.useCases.favorite

import com.nsicyber.vinylscan.domain.repository.DatabaseRepository
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
    private val repository: DatabaseRepository
) {
    suspend operator fun invoke(vinylId: Int) {
        repository.removeFromFavorites(vinylId)
    }
} 