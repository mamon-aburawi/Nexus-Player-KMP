package player.utils

internal fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60


    val minStr = minutes.toString().padStart(2, '0')
    val secStr = seconds.toString().padStart(2, '0')

    return "$minStr:$secStr"
}


/**
 * Converts a time string in the format "mm:ss" or "hh:mm:ss" to milliseconds.
 */
internal fun String.toTimeMs(): Long {
    val parts = this.split(":")
    return when (parts.size) {
        2 -> {
            // Format: "mm:ss"
            val minutes = parts[0].toLongOrNull() ?: 0
            val seconds = parts[1].toLongOrNull() ?: 0
            (minutes * 60 + seconds) * 1000
        }
        3 -> {
            // Format: "hh:mm:ss"
            val hours = parts[0].toLongOrNull() ?: 0
            val minutes = parts[1].toLongOrNull() ?: 0
            val seconds = parts[2].toLongOrNull() ?: 0
            (hours * 3600 + minutes * 60 + seconds) * 1000
        }
        else -> 0
    }
}


internal enum class Platform{
    ANDROID, IOS, WEB, DESKTOP
}


internal expect fun getPlatform(): Platform
