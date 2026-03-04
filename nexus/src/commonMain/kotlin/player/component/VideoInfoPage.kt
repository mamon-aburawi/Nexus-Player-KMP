package player.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kdroidfilter.composemediaplayer.VideoMetadata

import player.utils.formatTime
import player.utils.hybridClick
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@Composable
internal fun VideoInfoPage(
    modifier: Modifier = Modifier,
    videoMeta: VideoMetadata, // Assuming VideoMetadata is your data class
    contentColor: Color = Color.White,
    onBack: () -> Unit
) {
    Column(modifier = modifier.heightIn(max = 400.dp)) {
        // Sub-menu Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .hybridClick(
                    enabled = false,
                    onClick = {},
                    rippleEnabled = false,
                    hoverEnabled = false
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = "Info",
                color = contentColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            // Empty box to balance the back button for perfect centering
            Spacer(modifier = Modifier.size(48.dp))
        }

        HorizontalDivider(color = contentColor.copy(alpha = 0.1f))

        // Scrollable Content
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .hybridClick(
                    enabled = false,
                    onClick = {},
                    rippleEnabled = false,
                    hoverEnabled = false
                )
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val bitRate = if (videoMeta.bitrate != null) "${videoMeta.bitrate!! / 1000000} Mbps" else "N/N"
            val frameRate = if (videoMeta.frameRate != null) "${videoMeta.frameRate?.roundToInt()} fps" else "N/N"

            InfoRow("Title", videoMeta.title ?: "N/N", contentColor)
            InfoRow("Resolution", "${videoMeta.width} x ${videoMeta.height}", contentColor)
            InfoRow("Duration", formatTime(videoMeta.duration ?: 0L), contentColor)
            InfoRow("Bitrate", bitRate, contentColor)
            InfoRow("Frame Rate", frameRate, contentColor)
            InfoRow("Mime Type", videoMeta.mimeType ?: "N/N", contentColor)

            HorizontalDivider(color = contentColor.copy(alpha = 0.05f))

            InfoRow("Audio Channels", videoMeta.audioChannels.toString(), contentColor)
            InfoRow("Sample Rate", "${videoMeta.audioSampleRate} Hz", contentColor)
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    contentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = contentColor.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            color = contentColor,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )
    }
}



@Preview
@Composable
private fun VideoInfoPagePreview() {
    // 1. Mock Data
    val mockMetadata = object {
        val title = "Sample_Video_2026.mp4"
        val duration = 125000L // 2 minutes 5 seconds
        val height = 1080
        val width = 1920
        val bitrate = 5000000L // 5 Mbps
        val frameRate = 30f
        val audioChannels = 2
        val audioSampleRate = 44100
        val mimeType = "video/mp4"
    }

    // 2. Wrap in a Surface/Box to simulate a bottom sheet or container background
    Surface(
        color = Color(0xFF121212), // Dark background typical for video players
        modifier = Modifier.fillMaxWidth()
    ) {
        // We pass a dummy lambda for onBack
        VideoInfoPage(
            videoMeta = VideoMetadata(
                title = mockMetadata.title,
                duration = mockMetadata.duration,
                height = mockMetadata.height,
                width = mockMetadata.width,
                bitrate = mockMetadata.bitrate,
                frameRate = mockMetadata.frameRate,
                audioChannels = mockMetadata.audioChannels,
                audioSampleRate = mockMetadata.audioSampleRate,
                mimeType = mockMetadata.mimeType
            ),
            onBack = {}
        )
    }
}