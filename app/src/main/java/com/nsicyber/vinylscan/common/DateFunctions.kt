package com.nsicyber.vinylscan.common

import java.text.SimpleDateFormat
import java.util.Locale


fun formatDate(inputDate: String?): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMMM, yyyy", Locale.getDefault())

    return try {
        val date = inputFormat.parse(inputDate)
        date?.let { outputFormat.format(it) } ?: ""
    } catch (e: Exception) {
        ""
    }
}

fun formatDuration(input: String?): String {
    return try {
        val totalSeconds = input?.toInt()
        val minutes = totalSeconds?.div(60)
        val seconds = totalSeconds?.rem(60)
        "$minutes:$seconds"
    } catch (e: NumberFormatException) {
        ""
    }
}