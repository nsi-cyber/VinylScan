package com.nsicyber.vinylscan.data.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nsicyber.vinylscan.data.database.entity.FavoriteVinylEntity
import com.nsicyber.vinylscan.data.database.entity.RecentlyViewedEntity

@Database(entities = [RecentlyViewedEntity::class, FavoriteVinylEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): VinylDao
}
