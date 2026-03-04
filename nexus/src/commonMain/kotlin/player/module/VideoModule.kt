package player.module


data class VideoModule(
    val id: String,
    val title: String,
    val quality: List<VideoQuality> = emptyList(),
    val thumbnail: String = ""
)

