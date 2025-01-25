package com.nsicyber.vinylscan.domain.useCases.favorite

import com.nsicyber.vinylscan.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: DatabaseRepository
) {
    operator fun invoke(vinylId: Int): Flow<Boolean> {
        return repository.isFavorite(vinylId)
    }
} 