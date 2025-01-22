package com.nsicyber.vinylscan.data.repository

import com.nsicyber.vinylscan.common.DaoResult
import com.nsicyber.vinylscan.common.daoFlow
import com.nsicyber.vinylscan.data.database.dao.VinylDao
import com.nsicyber.vinylscan.data.database.entity.FavoriteVinylEntity
import com.nsicyber.vinylscan.data.database.entity.RecentlyViewedEntity
import com.nsicyber.vinylscan.domain.model.FavoriteVinylModel
import com.nsicyber.vinylscan.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DatabaseRepositoryImpl @Inject constructor(
    private val dao: VinylDao,
) : DatabaseRepository {
    override suspend fun addToFavorites(vinyl: FavoriteVinylModel) {
        dao.insertFavorite(vinyl.toEntity())
    }

    override suspend fun removeFromFavorites(vinylId: Int) {
        dao.deleteFavorite(vinylId)
    }

    override fun getAllFavorites(): Flow<List<FavoriteVinylModel>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun isFavorite(vinylId: Int): Flow<Boolean> {
        return dao.isFavorite(vinylId)
    }

    private fun FavoriteVinylModel.toEntity() = FavoriteVinylEntity(
         vinylId = vinylId,
        title = title,
        releaseDate = releaseDate,
        image = image
    )

    private fun FavoriteVinylEntity.toDomain() = FavoriteVinylModel(
         vinylId = vinylId,
        title = title,
        releaseDate = releaseDate,
        image = image
    )

    override suspend fun insertRecentlyViewed(item: RecentlyViewedEntity) {
        daoFlow {
            dao.insertRecentlyViewed(item)
        }.collect()
    }

    override   fun getRecentlyViewed(): Flow<DaoResult<List<RecentlyViewedEntity>?>> =
        daoFlow { dao.getRecentlyViewed() }
            .map { result ->
                mapDaoResult(result) { it }
            }


    override suspend fun clearAllRecentlyViewed() {
        daoFlow {
            dao.clearAllRecentlyViewed()
        }.collect()
    }


    private fun <T, R> mapDaoResult(
        result: DaoResult<T>,
        transform: (T) -> R?,
    ): DaoResult<R?> {
        return when (result) {
            is DaoResult.Success -> {
                val transformedData = result.data?.let { transform(it) }
                if (transformedData != null) {
                    DaoResult.Success(transformedData)
                } else {
                    DaoResult.Error("No data found")
                }
            }

            is DaoResult.Error -> DaoResult.Error(result.message)
        }
    }


}

