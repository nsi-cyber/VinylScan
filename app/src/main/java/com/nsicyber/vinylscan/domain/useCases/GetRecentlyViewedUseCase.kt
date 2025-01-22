package com.nsicyber.vinylscan.domain.useCases

import com.nsicyber.vinylscan.common.DaoResult
import com.nsicyber.vinylscan.data.database.entity.RecentlyViewedEntity
import com.nsicyber.vinylscan.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecentlyViewedUseCase
@Inject
constructor(
    private val repo: DatabaseRepository,
) {
    operator fun invoke(): Flow<DaoResult<List<RecentlyViewedEntity?>?>> =
        flow {
            try {
                repo.getRecentlyViewed().collect { result ->
                    emit(result)
                }
            } catch (e: Exception) {
                emit(DaoResult.Error(message = e.message.toString()))
            }
        }
}