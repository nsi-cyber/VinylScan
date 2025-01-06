package com.nsicyber.vinylscan.domain.mapFunc

import com.nsicyber.vinylscan.data.model.response.discogs.getDetail.GetDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getDetail.Tracklist
import com.nsicyber.vinylscan.domain.model.VinylModel
import com.nsicyber.vinylscan.domain.model.VinylTrackModel


fun GetDetailResponse?.toVinylModel(thumbnail: String?,barcode:List<String?>?): VinylModel {
    return VinylModel(
        cover = thumbnail.orEmpty(),
        title = this?.title.orEmpty(),
        releaseDate = this?.year.toString(),
        artistName = this?.artists?.map { it?.name }?.joinToString (", ")?: "",
        genres = this?.genres?.takeIf { !it.isNullOrEmpty()  }?.joinToString (", ")  ,
        styles = this?.styles?.takeIf { !it.isNullOrEmpty()  }?.joinToString (", "),
        tracks = this?.tracklist?.filter { it?.type_=="track" }?.map {
            it?.toVinylTrackModel(
                artistName = this?.artists?.firstOrNull()?.name,
                albumName = this?.title
            )
        },
        barcode = getBarcodeFromList(barcode),
        totalTime = calculateTotalTime(this?.tracklist?.map { it?.duration })
    )
}


fun Tracklist?.toVinylTrackModel(albumName: String?, artistName: String?): VinylTrackModel {
    return VinylTrackModel(
        title = this?.title.orEmpty(),
        duration = this?.duration.orEmpty(),
        albumName = albumName,
        artistName = artistName
    )
}
fun getBarcodeFromList(strings: List<String?>?): String? {
    return strings
       ?.map { it?.replace("\\s".toRegex(), "") }
        ?.firstOrNull { it?.all { char -> char.isDigit() } == true }
}

fun calculateTotalTime(times: List<String?>?): String {
    try {
        var totalSeconds = 0
        times?.forEach { time ->
            time?.split(":")?.let { parts ->
                if (parts.size == 2) {
                    val minutes = parts[0].toIntOrNull() ?: 0
                    val seconds = parts[1].toIntOrNull() ?: 0
                    totalSeconds += (minutes * 60) + seconds
                }
            }
        }
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return when {
            hours > 0 -> "$hours saat $minutes dakika $seconds saniye"
            else -> "$minutes dakika $seconds saniye"
        }
    } catch (e: Exception) {
        return ""
    }

}