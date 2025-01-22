package com.nsicyber.vinylscan.domain.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.common.DaoResult
import com.nsicyber.vinylscan.data.database.entity.RecentlyViewedEntity
import com.nsicyber.vinylscan.data.model.response.discogs.getMasterDetail.GetMasterDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getSearch.GetSearchResponse
import com.nsicyber.vinylscan.domain.model.FavoriteVinylModel
import kotlinx.coroutines.flow.Flow


interface DatabaseRepository {

    suspend fun addToFavorites(vinyl: FavoriteVinylModel)
    suspend fun removeFromFavorites(vinylId: Int)
    fun getAllFavorites(): Flow<List<FavoriteVinylModel>>
    fun isFavorite(vinylId: Int): Flow<Boolean>

    suspend fun insertRecentlyViewed(item: RecentlyViewedEntity)

      fun getRecentlyViewed(): Flow<DaoResult<List<RecentlyViewedEntity>?>>

     suspend fun clearAllRecentlyViewed()

}