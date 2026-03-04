package player.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kdroidfilter.composemediaplayer.VideoMetadata
import player.utils.dommyQualityData
import player.utils.getSpeedLabel
import player.utils.hybridClick
import player.utils.playbackSpeedOptions
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_info
import io.github.mamon.nexus.resources.ic_settings
import io.github.mamon.nexus.resources.ic_speed
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import player.module.NexusVideoMetaData

@Composable
internal fun MainSettingsPage(
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White,
    qualityLabel: String?,
    currentSpeed: Float,
    isQualityAvailable: Boolean = false,
    onNavigateToQuality: () -> Unit,
    videoLoopEnabled: Boolean = false,
    onLoopVideo: (Boolean) -> Unit,
    onNavigateToPlaybackSpeed: () -> Unit,
    onNavigateToVideoInfo:() -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState())
    ) {



        ToggleText(
            title = "Loop Video",
            contentColor = contentColor,
            enabled = videoLoopEnabled,
            onCheckedChange = { isChecked ->
                onLoopVideo(isChecked)
            }
        )
        ActionItem(
            icon = Res.drawable.ic_settings,
            visible = isQualityAvailable,
            title = "Quality",
            value = qualityLabel,
            contentColor = contentColor,
            onClick = {
                if (!qualityLabel.isNullOrEmpty() && isQualityAvailable){
                    onNavigateToQuality()
                }
            }
        )


        ActionItem(
            icon = Res.drawable.ic_speed,
            title = "Playback speed",
            value = currentSpeed.getSpeedLabel(),
            contentColor = contentColor,
            onClick = onNavigateToPlaybackSpeed
        )
        ActionItem(
            icon = Res.drawable.ic_info,
            title = "Info",
            value = "",
            contentColor = contentColor,
            onClick = onNavigateToVideoInfo
        )
    }
}


@Composable
private fun ActionItem(
    icon: DrawableResource,
    title: String,
    value: String?,
    visible: Boolean = true,
    contentColor: Color = Color.White,
    onClick: () -> Unit
) {
    if (visible){
        Row(
            modifier = Modifier
                .hybridClick { onClick() }
                .padding(16.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End // RTL layout like the video
        ) {
            Text(value ?: "", color = Color.Gray, fontSize = 14.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text = title, color = contentColor, modifier = Modifier.weight(1f), textAlign = TextAlign.End
            )
            Spacer(Modifier.width(12.dp))
            Icon(
                painter = painterResource(icon), null, tint = contentColor, modifier = Modifier.size(24.dp))
        }
    }
}


@Preview
@Composable
private fun ConsolePlayerMenuPreview() {

    var currentSpeed by remember { mutableStateOf(playbackSpeedOptions.first()) }


    Box(
        modifier = Modifier
            .width(320.dp),
        contentAlignment = Alignment.Center
    ) {
        ConsolePlayerMenu(
            videoLoopEnabled = true,
            onRepeatToggle = {},
            onDismiss = { },
            qualities = dommyQualityData,
            currentQualityIndex = 0,
            onSelectQuality = {  },
            onPlaybackSpeed = { },
            playbackSpeedOption = emptyList(),
            currentSpeed = currentSpeed,
            videoMeta = NexusVideoMetaData(
                title = "Video 1",
                duration = 654257L
            )
        )
    }

}


