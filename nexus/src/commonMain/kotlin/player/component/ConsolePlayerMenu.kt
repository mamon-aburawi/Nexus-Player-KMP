package player.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kdroidfilter.composemediaplayer.VideoMetadata
import player.module.NexusVideoMetaData
import player.module.VideoQuality
import player.utils.SettingsMenuPage
import player.utils.dommyQualityData


/** this compose menu for web & desktop taget **/

@Composable
internal fun ConsolePlayerMenu(
    modifier: Modifier = Modifier,
    background: Color = Color.Black.copy(alpha = 0.85f),
    contentColor: Color = Color.White,
    videoLoopEnabled: Boolean,
    videoMeta: NexusVideoMetaData?,
    onRepeatToggle: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    qualities: List<VideoQuality>,
    currentQualityIndex: Int,
    onSelectQuality: (VideoQuality) -> Unit,
    currentSpeed: Float,
    onPlaybackSpeed: (Float) -> Unit,
    playbackSpeedOption: List<Float>
) {
    var currentPage by remember { mutableStateOf(SettingsMenuPage.MAIN) }

    Box(
        modifier = modifier
            .width(320.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(vertical = 8.dp)
    ) {
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                if (targetState == SettingsMenuPage.QUALITY) {
                    // Slide in from right for sub-menu
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                } else {
                    // Slide in from left when going back
                    slideInHorizontally { -it } + fadeIn() togetherWith
                            slideOutHorizontally { it } + fadeOut()
                }
            }
        ) { page ->
            when (page) {
                SettingsMenuPage.MAIN -> MainSettingsPage(
                    qualityLabel = qualities[currentQualityIndex].label,
                    contentColor = contentColor,
                    isQualityAvailable = qualities.isNotEmpty(),
                    onNavigateToQuality = { currentPage = SettingsMenuPage.QUALITY },
                    onNavigateToPlaybackSpeed = { currentPage = SettingsMenuPage.SPEED },
                    videoLoopEnabled = videoLoopEnabled,
                    onLoopVideo = { onRepeatToggle(it) },
                    currentSpeed = currentSpeed,
                    onNavigateToVideoInfo = {
                        if (videoMeta != null){
                            currentPage = SettingsMenuPage.INFO
                        }
                    }
                )
                SettingsMenuPage.QUALITY ->{
                    if (qualities.isNotEmpty()){
                        QualityPage(
                            qualities = qualities,
                            contentColor = contentColor,
                            onBack = { currentPage = SettingsMenuPage.MAIN },
                            currentQuality = qualities[currentQualityIndex],
                            onSelectQuality = {
                                onSelectQuality(it)
                                onDismiss()
                            }
                        )
                    }
                }

                SettingsMenuPage.SPEED -> {
                    SpeedPage(
                        modifier = Modifier.fillMaxWidth(),
                        speeds = playbackSpeedOption,
                        contentColor = contentColor,
                        currentSpeed = currentSpeed,
                        onBack = {
                            currentPage = SettingsMenuPage.MAIN
                        },
                        onSelectSpeed = {
                            onPlaybackSpeed(it)
                            onDismiss()
                        }
                    )
                }

                SettingsMenuPage.INFO -> {
                    if (videoMeta != null){
                        VideoInfoPage(
                            videoMeta = videoMeta,
                            onBack = {
                                currentPage = SettingsMenuPage.MAIN
                            }
                        )
                    }
                }
            }
        }
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
        currentQualityIndex = 1,
        onSelectQuality = {},
        onPlaybackSpeed = {},
        playbackSpeedOption = emptyList(),
        currentSpeed = 1.0f,
        videoMeta = NexusVideoMetaData(),
    )
}











