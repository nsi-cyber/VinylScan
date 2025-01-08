package com.nsicyber.vinylscan.common

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest

fun preloadImages(context: Context, imageUrls: List<String?>) {
    val imageLoader = ImageLoader(context)
    imageUrls.forEach { url ->
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        imageLoader.enqueue(request) // Resimleri önceden yükle
    }
}
