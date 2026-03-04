package player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import io.github.kdroidfilter.composemediaplayer.InitialPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerError
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerSurface
import io.github.kdroidfilter.composemediaplayer.rememberVideoPlayerState
import player.component.AnimatedPlayButton
import player.component.BottomControls
import player.component.VolumeSliderMobile
import player.component.PlayerAppBar
import player.module.VideoModule
import player.module.VideoTag
import player.utils.ScreenOrientation
import player.utils.VideoPlayerOrientation
import player.utils.hybridClick
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_backword
import io.github.mamon.nexus.resources.ic_forword
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import player.module.NexusVideoMetaData
import player.module.toNexusMetaData
import player.utils.Platform
import player.utils.getPlatform


@Composable
fun VideoPlayer(
    data: VideoModule,
    currentVideoQualityIndex: Int = 0,
    repeatEnabled: Boolean = false,
    currentSpeed: Float = 1.0f,
    skipIntervalSeconds: Int = 5,
    sliderActiveColor: Color = Color.Red,
    inActiveTrackColor: Color = Color.White.copy(alpha = 0.24f),
    controlsFadeDurationSeconds: Int = 6,
    trackSegmentCount: Int = 0,
    onVideoComplete: () -> Unit = {},
    onExit: () -> Unit = {},
    onSeek: (value: Float) -> Unit = {}, // 0.0 to 1.0
    onDataLoaded:(NexusVideoMetaData) -> Unit = {},
    onError:(String) -> Unit = {},
    onProgress: @Composable () -> Unit = { },
    onSettings: @Composable (BoxScope.(onDismiss: () -> Unit) -> Unit)? = null
) {

    var isControllerDisplayed by remember { mutableStateOf(true) }
    var play1stTime by remember { mutableStateOf(true) }
    var isMenuDisplayed by remember { mutableStateOf(false) }
    var videoTitle by remember {mutableStateOf(data.title)}
    var currentProgress by remember { mutableFloatStateOf(0f) }
    val playerState = rememberVideoPlayerState()

    val videoUrl = data.quality[currentVideoQualityIndex].url

    val errorMessage by remember(playerState.error) {
        derivedStateOf {
            when (val it = playerState.error) {
                is VideoPlayerError.NetworkError -> "No Internet Connection"
                is VideoPlayerError.SourceError -> "Video Unavailable"
                is VideoPlayerError.CodecError -> "Unsupported Format"
                is VideoPlayerError.UnknownError -> it.message
                else -> ""
            }
        }
    }


// 1. Handle URL Changes
    LaunchedEffect(videoUrl) {
        val updatedSeek = playerState.sliderPos
        println("current Progress: $updatedSeek")

        if (!play1stTime) {
            playerState.stop()
            playerState.openUri(videoUrl, InitialPlayerState.PLAY)
            
        }
    }


    LaunchedEffect(playerState.hasMedia, videoUrl) {
        if (playerState.hasMedia) {
            while (playerState.isLoading || (playerState.metadata.duration ?: 0L) <= 0L) {
                delay(50)
            }
            playerState.sliderPos = currentProgress
            playerState.seekTo(playerState.sliderPos)

            val metaData = playerState.metadata
            if (data.title.isEmpty()){
                videoTitle = metaData.title ?: data.title
            }
            onDataLoaded(metaData.toNexusMetaData())
        }
    }


    LaunchedEffect(repeatEnabled){
        playerState.loop = repeatEnabled
    }


    LaunchedEffect(currentSpeed) { playerState.playbackSpeed = currentSpeed }

    LaunchedEffect(playerState.isPlaying, playerState.sliderPos, playerState.userDragging, playerState.error) {

        if (errorMessage.isNotEmpty()) onError(errorMessage)

        val progress = playerState.sliderPos / 1000f
        if (progress >= 0.99f && !playerState.loop && !play1stTime) {
            onVideoComplete()
        }
    }

    LaunchedEffect(isControllerDisplayed, playerState.isPlaying, isMenuDisplayed) {
        if (isControllerDisplayed && playerState.isPlaying && !isMenuDisplayed) {
            delay(controlsFadeDurationSeconds * 1000L)
            isControllerDisplayed = false
        }
    }

    VideoPlayerOrientation(
        orientation = if (playerState.isFullscreen) ScreenOrientation.LANDSCAPE else ScreenOrientation.PORTRAIT
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        if (play1stTime) {
            AnimatedPlayButton(
                modifier = Modifier
                    .align(Alignment.Center),
                isPlaying = playerState.isPlaying,
                onClick = {
                    playerState.openUri(videoUrl, InitialPlayerState.PLAY)
                    play1stTime = false
                }
            )
        }

        VideoPlayerSurface(
            playerState = playerState,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                if (!play1stTime) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { isControllerDisplayed = !isControllerDisplayed },
                    ) {


                        if (playerState.isLoading){
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){ onProgress() }
                        }


                        if (isMenuDisplayed && onSettings != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .hybridClick(
                                        hoverEnabled = false,
                                        rippleEnabled = false,
                                    ){ isMenuDisplayed = false }
                                    .padding(vertical = 80.dp, horizontal = 16.dp), // Adjust bottom padding to sit above the control bar
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                // We call the external composable here
                                onSettings { isMenuDisplayed = false }
                            }
                        }

                        val currentQuality = data.quality[currentVideoQualityIndex]

                        PlayerControls(
                            visible = isControllerDisplayed,
                            playerState = playerState,
                            activeTrackColor = sliderActiveColor,
                            inactiveTrackColor = inActiveTrackColor,
                            videoTag = currentQuality.tag,
                            videoTitle = videoTitle,
                            onExit = { onExit() },
                            trackSegmentCount = trackSegmentCount,
                            onSettingsClicked = {
                                isMenuDisplayed = !isMenuDisplayed
                            },
                            onSliderValueChange = { value ->   // 0.1 to 1.0
                                currentProgress = value
                                onSeek(currentProgress)
                            },
                            onForwardSkip = {
                                val skipIntervalMs = skipIntervalSeconds * 1000L
                                val totalTimeMs = playerState.metadata.duration ?: 0L

                                if (totalTimeMs > 0) {
                                    val skipRatio = skipIntervalMs.toFloat() / totalTimeMs.toFloat()
                                    val sliderIncrement = skipRatio * 1000f
                                    val currentSeek = playerState.sliderPos
                                    val updatedSeek = (currentSeek + sliderIncrement)

                                    playerState.sliderPos = updatedSeek
                                    playerState.seekTo(updatedSeek)
                                }
                            },
                            onBackwardSkip = {
                                val skipIntervalMs = skipIntervalSeconds * 1000L
                                val totalTimeMs = playerState.metadata.duration ?: 0L

                                if (totalTimeMs > 0) {
                                    val skipRatio = skipIntervalMs.toFloat() / totalTimeMs.toFloat()
                                    val sliderIncrement = skipRatio * 1000f
                                    val currentSeek = playerState.sliderPos
                                    val updatedSeek = (currentSeek - sliderIncrement)

                                    playerState.sliderPos = updatedSeek
                                    playerState.seekTo(updatedSeek)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun PlayerControls(
    visible: Boolean,
    videoTitle: String,
    videoTag: VideoTag?,
    playerState: VideoPlayerState,
    trackSegmentCount: Int,
    activeTrackColor: Color,
    inactiveTrackColor: Color,
    onExit: () -> Unit,
    onSettingsClicked: () -> Unit,
    onSliderValueChange: (Float) -> Unit,
    onForwardSkip: () -> Unit = {},
    onBackwardSkip: () -> Unit = {}
) {
    val isMobile = getPlatform() == Platform.ANDROID || getPlatform() == Platform.IOS

    Box(modifier = Modifier.fillMaxSize()) {

        PlayerAppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            title = videoTitle,
            visible = visible,
            onExit = {
                if (playerState.isFullscreen) playerState.toggleFullscreen()
                playerState.stop()
                onExit()
            }
        )


        if (isMobile) {
            VolumeSliderMobile(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 15.dp),
                volume = playerState.volume,
                onVolumeChange = { playerState.volume = it }
            )
        }

        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(if (isMobile) 10.dp else 30.dp)
        ) {
            ButtonIcon(
                visible = visible,
                icon = Res.drawable.ic_backword,
                onClick = { onBackwardSkip() }
            )
            AnimatedPlayButton(
                visible = visible,
                isPlaying = playerState.isPlaying,
                onClick = {
                    if (playerState.isPlaying) playerState.pause() else playerState.play()
                }
            )

            ButtonIcon(
                visible = visible,
                icon = Res.drawable.ic_forword,
                onClick = { onForwardSkip() }
            )
        }


        BottomControls(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = visible,
            playerState = playerState,
            videoTag = videoTag,
            activeTrackColor = activeTrackColor,
            inactiveTrackColor = inactiveTrackColor,
            trackSegmentCount = trackSegmentCount,
            onSettingsClicked = onSettingsClicked,
            onSliderValueChange = {onSliderValueChange(it)}
        )

    }
}


@Composable
private fun ButtonIcon(
    onClick:()-> Unit,
    visible: Boolean = true,
    color: Color = Color.White,
    icon: DrawableResource,
){
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f),
    ){
        IconButton(
            onClick = onClick,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}




@Preview(device = "spec:width=720dp,height=1280dp,dpi=420,isRound=false,orientation=landscape,cutout=none,navigation=gesture")
@Composable
private fun Desktop_Web_ControlsPreview(){
    PlayerControls(
        visible = true,
        videoTitle = "Spider Man 3",
        playerState = rememberVideoPlayerState(),
        trackSegmentCount = 5,
        activeTrackColor = Color.Red,
        inactiveTrackColor = Color.White.copy(alpha = 0.4f),
        onExit = {},
        onSettingsClicked = {},
        onSliderValueChange = {},
        videoTag = VideoTag.HD
    )
}





@Preview
@Composable
private fun MobileControlsPreview(){
    PlayerControls(
        visible = true,
        videoTitle = "Spider Man 3",
        playerState = rememberVideoPlayerState(),
        trackSegmentCount = 5,
        activeTrackColor = Color.Red,
        inactiveTrackColor = Color.White.copy(alpha = 0.4f),
        onExit = {},
        onSettingsClicked = {},
        onSliderValueChange = {},
        videoTag = VideoTag.SD
    )
}
