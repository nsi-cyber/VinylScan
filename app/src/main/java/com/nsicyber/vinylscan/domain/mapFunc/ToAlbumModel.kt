package com.nsicyber.vinylscan.domain.mapFunc

import com.nsicyber.vinylscan.data.model.response.discogs.getDetail.GetDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getDetail.Tracklist
import com.nsicyber.vinylscan.domain.model.AlbumModel
import com.nsicyber.vinylscan.domain.model.TrackModel

fun GetDetailResponse?.toAlbumModel(thumbnail: String?): AlbumModel {
    return AlbumModel(
        id = this?.id ?: 0,
        imageUrl = thumbnail.orEmpty(),
        name = this?.title ?: "",
        year = this?.year ?: 0,
        artistName = this?.artists?.firstOrNull()?.name ?: "",
        genres = this?.genres?.map { it.toString() } ?: listOf(),
        styles = this?.styles?.map { it.toString() } ?: listOf(),
        tracks = this?.tracklist?.map { it?.toTrackModel() ?: TrackModel("", "", "") } ?: listOf())
}


fun Tracklist?.toTrackModel(): TrackModel {
    return TrackModel(
        position = this?.position.orEmpty(),
        title = this?.title.orEmpty(),
        duration = this?.duration.orEmpty()
    )
}