package com.nsicyber.vinylscan.domain.mapFunc

import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.GetReleaseDetailResponse
import com.nsicyber.vinylscan.data.model.response.discogs.getReleaseDetail.ReleaseTracklist
import com.nsicyber.vinylscan.domain.model.VinylModel
import com.nsicyber.vinylscan.domain.model.VinylTrackModel


fun GetReleaseDetailResponse?.toVinylModel(): VinylModel {
    return VinylModel(
        title = this?.title.orEmpty(),
        vinylQuantity = this?.format_quantity,
        releaseDate = this?.released,
        artistName = this?.artists_sort.orEmpty(),
        genres = this?.genres?.takeIf { !it.isNullOrEmpty() }?.joinToString(", "),
        styles = this?.styles?.takeIf { !it.isNullOrEmpty() }?.joinToString(", "),
        tracks = this?.tracklist?.filter { it?.type_ == "track" }?.map {
            it?.toVinylTrackModel(
                artistName = this?.artists?.firstOrNull()?.name,
                albumName = this?.title
            )
        },
        barcode = this?.identifiers?.firstOrNull { it?.type == "Barcode" }?.value?.replace(
            "\\s".toRegex(),
            ""
        )?.replace("-".toRegex(), ""),
        totalTime = calculateTotalTime(this?.tracklist?.map { it?.duration }),
        images = this?.images?.map { it?.uri },
        catalog = this?.labels?.firstOrNull { !it?.catno.isNullOrEmpty() }?.catno,
        minPrice = this?.lowest_price.toString()
    )
}


fun ReleaseTracklist?.toVinylTrackModel(albumName: String?, artistName: String?): VinylTrackModel {
    return VinylTrackModel(
        title = this?.title.orEmpty(),
        position = this?.position.orEmpty(),
        duration = this?.duration.orEmpty(),
        albumName = albumName,
        artistName = artistName
    )
}

fun getBarcodeFromList(strings: List<String?>?): String? {
    return strings
        ?.map { it?.replace("\\s".toRegex(), "") }?.map { it?.replace("-".toRegex(), "") }
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
            hours == 0 && seconds == 0 && minutes == 0 -> ""
            hours > 0 -> "$hours saat $minutes dakika $seconds saniye"
            else -> "$minutes dakika $seconds saniye"
        }
    } catch (e: Exception) {
        return ""
    }

}