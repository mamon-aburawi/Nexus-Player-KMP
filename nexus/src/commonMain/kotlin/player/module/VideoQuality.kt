package player.module

import player.utils.BYTES_IN_GB
import player.utils.BYTES_IN_KB
import player.utils.BYTES_IN_MB


data class VideoQuality(
    val url: String,
    val label: String? = null,      // e.g., "1080p60"
    private val tagValue: String? = null, // e.g., "HD"
    private val byteValue: Long? = null, // e.g., "1.2 GB" or "Estimated: 450 MB"
){
    val tag: VideoTag?
        get() = VideoTag.fromString(tagValue)


    val sizeInKb: Int
        get() = byteValue?.div(BYTES_IN_KB)?.toInt() ?: 0

    val sizeInMb: Int
        get() = byteValue?.div(BYTES_IN_MB)?.toInt() ?: 0

    val sizeInGb: Int
        get() = byteValue?.div(BYTES_IN_GB)?.toInt() ?: 0


}


sealed class VideoTag(val value: String) {
    object HD : VideoTag("HD")
    object FHD : VideoTag("FHD")
    object QHD : VideoTag("2K")
    object UHD8K : VideoTag("8K")
    object SDR : VideoTag("SDR")
    object SD : VideoTag("SD")
    object HDR : VideoTag("HDR")
    object UHD : VideoTag("4K")
    data class Unknown(val rawValue: String) : VideoTag(rawValue)

    companion object {
        fun fromString(tag: String?): VideoTag? {
            if (tag == null) return null
            return when (tag.uppercase()) {
                "SD" -> SD
                "HD" -> HD
                "FHD", "1080P" -> FHD
                "2K", "QHD" -> QHD
                "4K", "UHD" -> UHD
                "8K" -> UHD8K
                "HDR" -> HDR
                "SDR" -> SDR
                "4K", "UHD" -> UHD
                else -> Unknown(tag)
            }
        }
    }

    override fun toString(): String = value
}


data class StorageSize(
    val bytes: Long
) {
//    fun format(): String {
//        return when {
//            bytes >= BYTES_IN_GB -> "${(bytes.toDouble() / BYTES_IN_GB).round(1)} GB"
//            bytes >= BYTES_IN_MB -> "${bytes / BYTES_IN_MB} MB"
//            bytes >= BYTES_IN_KB -> "${bytes / BYTES_IN_KB} KB"
//            else -> "$bytes Bytes"
//        }
//    }
//
//

//    companion object {
//
//        fun fromKb(kb: Int) = StorageSize(kb * BYTES_IN_KB)
//
//        fun fromMb(mb: Int) = StorageSize(mb * BYTES_IN_MB)
//
//        fun fromGb(gb: Double) = StorageSize((gb * BYTES_IN_GB).toLong())
//    }
}