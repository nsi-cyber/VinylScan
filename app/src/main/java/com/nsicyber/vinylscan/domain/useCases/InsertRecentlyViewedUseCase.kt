package com.nsicyber.vinylscan.domain.useCases

import com.nsicyber.vinylscan.common.DaoResult
import com.nsicyber.vinylscan.data.database.entity.RecentlyViewedEntity
import com.nsicyber.vinylscan.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class InsertRecentlyViewedUseCase
@Inject
constructor(
    private val repo: DatabaseRepository,
) {
    operator fun invoke(item: RecentlyViewedEntity): Flow<DaoResult<List<RecentlyViewedEntity?>?>> =
        flow {
            try {
                repo.insertRecentlyViewed(item)
                emit(DaoResult.Success(null))

            } catch (e: Exception) {
                emit(DaoResult.Error(message = e.message.toString()))
            }
        }
}