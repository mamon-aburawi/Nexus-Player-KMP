@file:OptIn(ExperimentalMaterial3Api::class)

package player.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.rememberVideoPlayerState


@Composable
internal fun VideoPlayerSlider(
    playerState: VideoPlayerState,
    activeColor: Color,
    inactiveColor: Color,
    segmentCount: Int,
    onValueChangeFinished: (Float) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Slider(
            value = playerState.sliderPos,
            onValueChange = {
                playerState.sliderPos = it
                playerState.userDragging = true
            },
            onValueChangeFinished = {
                playerState.userDragging = false
                onValueChangeFinished(playerState.sliderPos)
                playerState.seekTo(playerState.sliderPos)
            },
            valueRange = 0f..1000f,
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxWidth(),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(activeColor, CircleShape)
                )
            },
            track = { sliderState ->
                // Custom Track with Segments
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                ) {
                    val trackWidth = size.width
                    val progress = (playerState.sliderPos / 1000f).coerceIn(0f, 1f)

                    drawRoundRect(
                        color = inactiveColor.copy(alpha = 0.3f),
                        size = size,
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                    )

                    drawRoundRect(
                        color = activeColor,
                        size = size.copy(width = trackWidth * progress),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                    )

                    for (i in 1..segmentCount) {
                        val xPos = (trackWidth / (segmentCount + 1)) * i
                        drawLine(
                            color = Color.Black.copy(alpha = 0.5f),
                            start = Offset(xPos, 0f),
                            end = Offset(xPos, size.height),
                            strokeWidth = 2.dp.toPx() // The gap width
                        )
                    }
                }
            }
        )
    }
}


@Preview
@Composable
private fun VideoPlayerSliderPreview(){
    VideoPlayerSlider(
        playerState = rememberVideoPlayerState(),
        activeColor = Color.Red,
        inactiveColor = Color.White,
        segmentCount = 3
    )
}