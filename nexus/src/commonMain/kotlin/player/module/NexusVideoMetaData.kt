package player.module

import io.github.kdroidfilter.composemediaplayer.VideoMetadata


data class NexusVideoMetaData(
    var title: String? = null,
    var duration: Long? = null, // Duration in milliseconds
    var width: Int? = null,
    var height: Int? = null,
    var bitrate: Long? = null, // Bitrate in bits per second
    var frameRate: Float? = null,
    var mimeType: String? = null,
    var audioChannels: Int? = null,
    var audioSampleRate: Int? = null,
)

internal fun VideoMetadata.toNexusMetaData(): NexusVideoMetaData{
    return NexusVideoMetaData(
        title = title,
        duration = duration,
        width = width,
        height = height,
        bitrate = bitrate,
        frameRate = frameRate,
        mimeType = mimeType,
        audioChannels = audioChannels,
        audioSampleRate = audioSampleRate
    )
}