package com.nsicyber.vinylscan.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup


suspend fun openExternalMusicLinkFromGoogle(
    context: Context,
    query: String,
    type: ExternalWebsites
) {
    return withContext(Dispatchers.IO) {
        var url =
            "https://www.google.com/search?q=site:${type.toUrl()}+${query}&btnI=1"// Ağ işlemini IO iş parçacığında çalıştır
        try {
            val document =
                Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get()

            val fullLink =
                document.select("a[href]").firstOrNull()?.attr("href")?.substringAfter("q=")
                    ?.substringBefore("&")
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(fullLink)
                )
            )
        } catch (e: Exception) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
            )
        }
    }
}



