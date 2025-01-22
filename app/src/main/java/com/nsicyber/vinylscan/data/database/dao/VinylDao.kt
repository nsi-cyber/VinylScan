package com.nsicyber.vinylscan.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nsicyber.vinylscan.data.database.entity.FavoriteVinylEntity
import com.nsicyber.vinylscan.data.database.entity.RecentlyViewedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VinylDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentlyViewed(item: RecentlyViewedEntity)

    @Query("SELECT * FROM recently_viewed ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentlyViewed(): List<RecentlyViewedEntity>

    @Query("DELETE FROM recently_viewed")
    suspend fun clearAllRecentlyViewed()



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteVinylEntity)

    @Query("DELETE FROM favorite_vinyls WHERE vinylId = :vinylId")
    suspend fun deleteFavorite(vinylId: Int)

    @Query("SELECT * FROM favorite_vinyls")
    fun getAllFavorites(): Flow<List<FavoriteVinylEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vinyls WHERE vinylId = :vinylId)")
    fun isFavorite(vinylId: Int): Flow<Boolean>
}
