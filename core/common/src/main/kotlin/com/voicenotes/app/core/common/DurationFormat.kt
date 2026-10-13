package com.voicenotes.app.core.common

/** Formats a duration in milliseconds as `m:ss` or `h:mm:ss`, for UI display and a11y labels. */
fun formatDurationMillis(durationMillis: Long): String {
    val totalSeconds = durationMillis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%d:%02d".format(minutes, seconds)
    }
}

/** Spoken-friendly duration for screen readers, e.g. "2 minutes 5 seconds". */
fun formatDurationForAccessibility(durationMillis: Long): String {
    val totalSeconds = durationMillis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val parts = mutableListOf<String>()
    if (hours > 0) parts += "$hours hour${if (hours != 1L) "s" else ""}"
    if (minutes > 0) parts += "$minutes minute${if (minutes != 1L) "s" else ""}"
    if (seconds > 0 || parts.isEmpty()) parts += "$seconds second${if (seconds != 1L) "s" else ""}"
    return parts.joinToString(" ")
}
