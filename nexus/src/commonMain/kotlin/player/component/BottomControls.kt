package player.component


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.rememberVideoPlayerState
import player.module.VideoTag
import player.utils.Platform
import player.utils.getPlatform
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_full_size
import io.github.mamon.nexus.resources.ic_minimize
import io.github.mamon.nexus.resources.ic_pause
import io.github.mamon.nexus.resources.ic_play
import io.github.mamon.nexus.resources.ic_settings
import org.jetbrains.compose.resources.painterResource


@Composable
internal fun BottomControls(
    visible: Boolean = true,
    videoTag: VideoTag?,
    modifier: Modifier = Modifier,
    playerState: VideoPlayerState,
    activeTrackColor: Color,
    inactiveTrackColor: Color,
    trackSegmentCount: Int,
    onSettingsClicked: () -> Unit,
    onSliderValueChange: (Float) -> Unit
) {
    val isMobile = getPlatform() == Platform.ANDROID || getPlatform() == Platform.IOS

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            VideoPlayerSlider(
                playerState = playerState,
                activeColor = activeTrackColor,
                inactiveColor = inactiveTrackColor,
                segmentCount = trackSegmentCount,
                onValueChangeFinished = { onSliderValueChange(it) }
            )


            Spacer(modifier = Modifier.height(8.dp))

            ControlActionRow(
                playerState = playerState,
                isMobile = isMobile,
                videoTag = videoTag,
                onSettingsClicked = onSettingsClicked
            )
        }
    }

}

@Composable
private fun ControlActionRow(
    playerState: VideoPlayerState,
    isMobile: Boolean,
    videoTag: VideoTag?,
    onSettingsClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        AdaptiveIconButton(
            isChecked = playerState.isPlaying,
            iconChecked = Res.drawable.ic_pause,
            iconUnchecked = Res.drawable.ic_play,
            onClick = { if (playerState.isPlaying) playerState.pause() else playerState.play() }
        )

        if (!isMobile) {
            VolumeSliderConsole(
                volume = playerState.volume,
                onVolumeChange = { playerState.volume = it }
            )
        }

        OptionContainer(horizontalPadding = 8.dp) {
            Text(
                text = "${playerState.positionText} / ${playerState.durationText}",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

       OptionContainer(
           horizontalPadding = 4.dp
       ) {
           Spacer(modifier = Modifier.width(8.dp))


            SettingsIconButton(
                videoTag = videoTag,
                onClick = onSettingsClicked
            )

           AdaptiveIconButton(
               isChecked = playerState.isFullscreen,
               iconChecked = Res.drawable.ic_minimize,
               iconUnchecked = Res.drawable.ic_full_size,
               onClick = {
                   playerState.toggleFullscreen()
               }
           )

        }
    }
}



@Composable
private fun SettingsIconButton(
    videoTag: VideoTag?,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Box {
            Icon(
                painterResource(Res.drawable.ic_settings),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            val isHighQuality = videoTag != VideoTag.SD && videoTag != VideoTag.SDR

            AnimatedVisibility (isHighQuality && videoTag != null){
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .background(Color.Red, RoundedCornerShape(2.dp))
                        .padding(horizontal = 2.dp)
                ) {
                    Text(videoTag?.value ?: "", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }

        }
    }
}



@Composable
private fun OptionContainer(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 4.dp,
    backgroundColor: Color = Color.Black.copy(alpha = 0.4f),
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        modifier = modifier
            .height(38.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
        ) {
            content()
        }
    }
}


@Preview(name = "Desktop/Web", device = "spec:width=720dp,height=1280dp,dpi=420,isRound=false,orientation=landscape,cutout=none,navigation=gesture")
@Composable
private fun PreviewBottomControls() {

    val mockPlayerState = rememberVideoPlayerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .padding(8.dp)
    ) {
        BottomControls(
            playerState = mockPlayerState,
            activeTrackColor = Color.Red,
            inactiveTrackColor = Color.White.copy(alpha = 0.3f),
            trackSegmentCount = 3,
            onSettingsClicked = { /* No-op for preview */ },
            onSliderValueChange = {},
            videoTag = VideoTag.SD
        )
    }
}

@Preview(name = "Mobile", showBackground = true)
@Composable
private fun PreviewBottomControlsMobile() {
    val mockPlayerState = rememberVideoPlayerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .padding(8.dp)
    ) {
        BottomControls(
            visible = true,
            playerState = mockPlayerState,
            activeTrackColor = Color.Red,
            inactiveTrackColor = Color.Gray,
            trackSegmentCount = 1,
            onSettingsClicked = {},
            onSliderValueChange = {},
            videoTag = VideoTag.UHD
        )
    }
}



