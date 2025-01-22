package com.nsicyber.vinylscan.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recently_viewed")
data class RecentlyViewedEntity(
    @PrimaryKey
    val id: Int = 0,
    val title: String,
    val imageUrl: String,
    val releaseDate: String,
    val timestamp: Long=System.currentTimeMillis()
)
