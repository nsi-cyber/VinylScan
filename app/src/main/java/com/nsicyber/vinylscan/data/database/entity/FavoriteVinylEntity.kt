package com.nsicyber.vinylscan.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vinyls")
data class FavoriteVinylEntity(
    @PrimaryKey
    val vinylId: Int,
    val title: String,
    val releaseDate: String,
    val image: String?
) 