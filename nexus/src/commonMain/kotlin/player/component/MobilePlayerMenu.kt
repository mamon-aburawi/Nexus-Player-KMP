@file:OptIn(ExperimentalMaterial3Api::class)

package player.component
import androidx.compose.runtime.Composable
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import player.module.NexusVideoMetaData
import player.module.VideoQuality
import player.utils.SettingsMenuPage
import player.utils.dommyQualityData


/** this compose menu for android & ios taget **/
@Composable
internal fun MobilePlayerMenu(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    videoMeta: NexusVideoMetaData?,
    onRepeatToggle: (Boolean) -> Unit,
    repeatEnabled: Boolean,
    contentColor: Color = Color.White,
    containerColor: Color = Color.Black.copy(alpha = 0.85f),
    qualities: List<VideoQuality>,
    currentQualityIndex: Int,
    onSelectQuality: (VideoQuality) -> Unit,
    currentSpeed: Float,
    onPlaybackSpeed: (Float) -> Unit,
    playbackSpeedOption: List<Float>
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var currentPage by remember { mutableStateOf(SettingsMenuPage.MAIN) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = containerColor,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        dragHandle = {
            // Standard M3 Drag Handle
            BottomSheetDefaults.DragHandle(
                color = Color.White.copy(alpha = 0.4f)
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp) // Extra padding for aesthetic
        ) {
            AnimatedContent(
                targetState = currentPage,
                label = "SettingsPageTransition",
                transitionSpec = {
                    if (targetState == SettingsMenuPage.QUALITY) {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    } else {
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
                        videoLoopEnabled = repeatEnabled,
                        onLoopVideo = { onRepeatToggle(it) },
                        currentSpeed = currentSpeed,
                        onNavigateToVideoInfo = {
                            if (videoMeta != null) {
                                currentPage = SettingsMenuPage.INFO
                            }
                        }
                    )
                    SettingsMenuPage.QUALITY -> {
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
}




@Preview
@Composable
private fun MobilePlayerMenuPreview() {
    MobilePlayerMenu(
        onDismiss = {},
        onRepeatToggle = {},
        repeatEnabled = true,
        qualities = dommyQualityData,
        currentQualityIndex = 1,
        onSelectQuality = {},
        onPlaybackSpeed = {},
        playbackSpeedOption = emptyList(),
        currentSpeed = 1.0f,
        videoMeta = NexusVideoMetaData()
    )
}