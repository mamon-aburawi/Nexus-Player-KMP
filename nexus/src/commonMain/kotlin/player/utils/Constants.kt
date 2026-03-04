package player.utils

import player.module.VideoQuality


internal val playbackSpeedOptions = listOf(0.25f,0.50f,0.75f,1.0f,1.25f,1.50f,1.75f,2.0f, 2.25f,2.50f, 2.75f, 3.0f)

internal const val BYTES_IN_KB = 1024L
internal const val BYTES_IN_MB = 1024L * 1024L
internal const val BYTES_IN_GB = 1024L * 1024L * 1024L

internal const val VIDEO_URL_TEST_1 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
internal const val VIDEO_URL_TEST_2 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
internal const val VIDEO_URL_TEST_3 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"


internal fun Float.getSpeedLabel(): String {
    return when (this) {
        1.0f -> "Normal"
        else -> "${this}x"
    }
}


internal val dommyQualityData = listOf(
    VideoQuality(label = "1080p60", url = VIDEO_URL_TEST_1, tagValue = "FHD", byteValue = 1200),
    VideoQuality(label = "720p",url = VIDEO_URL_TEST_2, tagValue = "HD", byteValue = 450),
    VideoQuality(label = "480p",url = VIDEO_URL_TEST_3, byteValue = 200),
    VideoQuality(label = "360p60",url = VIDEO_URL_TEST_1, tagValue = "SD", byteValue = 400),
    VideoQuality(label = "240p60",url = VIDEO_URL_TEST_2, tagValue = "SD", byteValue = 200),
    VideoQuality(label = "144p60", url = VIDEO_URL_TEST_3,tagValue = "SD", byteValue = 100)
)


