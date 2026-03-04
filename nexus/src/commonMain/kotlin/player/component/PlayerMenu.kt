package player.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.kdroidfilter.composemediaplayer.VideoMetadata
import player.module.NexusVideoMetaData
import player.utils.Platform
import player.utils.getPlatform
import player.module.VideoQuality
import player.utils.dommyQualityData
import player.utils.playbackSpeedOptions


@Composable
fun PlayerMenu(
    modifier: Modifier = Modifier,
    background: Color = Color.Black.copy(alpha = 0.85f),
    contentColor: Color = Color.White,
    videoMetadata: NexusVideoMetaData? = null,
    repeatEnabled: Boolean = false,
    playbackSpeedOption: List<Float> = playbackSpeedOptions,
    onDismiss: () -> Unit,
    qualities: List<VideoQuality>,
    currentQualityIndex: Int = 0,
    onSelectQuality: (VideoQuality) -> Unit = {},
    currentSpeed: Float = 1.0f,
    onRepeatToggle: (Boolean) -> Unit = {},
    onPlaybackSpeed: (Float) -> Unit = {}
){
    val isMobile = Platform.IOS == getPlatform() || Platform.ANDROID == getPlatform()

    if (isMobile){
        MobilePlayerMenu(
            modifier = modifier,
            containerColor = background,
            onDismiss = onDismiss,
            qualities = qualities,
            currentQualityIndex = currentQualityIndex,
            onSelectQuality = onSelectQuality,
            contentColor = contentColor,
            onPlaybackSpeed = onPlaybackSpeed,
            playbackSpeedOption = playbackSpeedOption,
            onRepeatToggle = { onRepeatToggle(it) },
            repeatEnabled = repeatEnabled,
            currentSpeed = currentSpeed,
            videoMeta = videoMetadata
        )
    }else{
        ConsolePlayerMenu(
            modifier = modifier,
            background = background,
            onDismiss = onDismiss,
            qualities = qualities,
            currentQualityIndex = currentQualityIndex,
            onSelectQuality = onSelectQuality,
            contentColor = contentColor,
            onPlaybackSpeed = onPlaybackSpeed,
            playbackSpeedOption = playbackSpeedOption,
            onRepeatToggle = { onRepeatToggle(it) },
            videoLoopEnabled = repeatEnabled,
            currentSpeed = currentSpeed,
            videoMeta = videoMetadata
        )
    }
}


@Preview()
@Composable
private fun ConsolePlayerMenuPreview() {

    ConsolePlayerMenu(
        videoLoopEnabled = false,
        onRepeatToggle = {},
        onDismiss = {},
        qualities = dommyQualityData,
        currentQualityIndex = 0,
        onSelectQuality = {},
        onPlaybackSpeed = {},
        playbackSpeedOption = emptyList(),
        currentSpeed = 1.0f,
        videoMeta = NexusVideoMetaData()
    )
}



@Preview
@Composable
private fun MobilePlayerMenuPreview() {
    MobilePlayerMenu(
        onDismiss = {},
        onRepeatToggle = {},
        repeatEnabled = true,
        qualities = dommyQualityData,
        currentQualityIndex = 0,
        onSelectQuality = {},
        onPlaybackSpeed = {},
        playbackSpeedOption = emptyList(),
        currentSpeed = 1.0f,
        videoMeta = NexusVideoMetaData()
    )
}