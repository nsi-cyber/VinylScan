package com.nsicyber.vinylscan.domain.model

import com.nsicyber.vinylscan.data.database.entity.RecentlyViewedEntity

data class RecentlyViewedModel(
    val id: Int?,
    val title: String?,
    val format: String?,
    val releaseDate: String?,
    val image: String?,
)

data class FavoritesModel(
    val id: Int?,
    val title: String?,
    val releaseDate: String?,
    val image: String?,
)


fun RecentlyViewedEntity?.toModel(): RecentlyViewedModel {
    return RecentlyViewedModel(
        id = this?.id,
        title = this?.title,
        releaseDate = this?.releaseDate,
        image = this?.imageUrl,
        format = this?.format
    )
}

