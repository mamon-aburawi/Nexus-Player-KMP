@file:OptIn(ExperimentalMaterial3Api::class)

package player.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import player.utils.hybridClick
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_volume_off
import io.github.mamon.nexus.resources.ic_volume_on
import org.jetbrains.compose.resources.painterResource



@Composable
internal fun VolumeSliderConsole(
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var lastVol by remember { mutableStateOf(if (volume > 0f) volume else 0.5f) }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.Black.copy(0.4f),
        modifier = modifier
            .height(38.dp)
            .hoverable(interactionSource)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, end = if (isHovered) 10.dp else 8.dp)
        ) {
            Icon(
                painter = painterResource(if (volume == 0f) Res.drawable.ic_volume_off else Res.drawable.ic_volume_on),
                contentDescription = "Mute/Unmute",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .hybridClick(
                        rippleEnabled = false,
                        hoverEnabled = false,
                        onClick = {
                            if (volume > 0f) {
                                lastVol = volume
                                onVolumeChange(0f)
                            } else {
                                onVolumeChange(lastVol)
                            }
                        }
                    )

            )

            AnimatedVisibility(
                visible = isHovered,
                enter = expandHorizontally() + fadeIn(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                Slider(
                    value = volume,
                    onValueChange = onVolumeChange,
                    modifier = Modifier
                        .width(90.dp)
                        .padding(start = 8.dp),
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color.White, CircleShape)
                        )
                    },
                    track = { sliderState ->
                        SliderDefaults.Track(
                            sliderState = sliderState,
                            modifier = Modifier.height(2.dp),
                            thumbTrackGapSize = 0.dp,
                            trackInsideCornerSize = 0.dp,
                            colors = SliderDefaults.colors(
                                activeTrackColor = Color.White,
                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                            )
                        )
                    }
                )
            }
        }
    }
}


//@Composable
//internal fun VolumeSlider(
//    volume: Float,
//    onVolumeChange: (Float) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val interactionSource = remember { MutableInteractionSource() }
//    val isHovered by interactionSource.collectIsHoveredAsState()
//    var lastVol by remember { mutableStateOf(if (volume > 0f) volume else 0.5f) }
//
//    Surface(
//        shape = RoundedCornerShape(24.dp),
//        color = Color.Black.copy(alpha = 0.4f),
//        modifier = modifier.height(38.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(start = 8.dp, end = if (isHovered) 10.dp else 8.dp)
//        ) {
//            Icon(
//                painter = painterResource(if (volume == 0f) Res.drawable.ic_volume_off else Res.drawable.ic_volume_on),
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier
//                    .size(20.dp)
//                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
//                        if (volume > 0f) { lastVol = volume; onVolumeChange(0f) } else onVolumeChange(lastVol)
//                    }
//            )
//
//            AnimatedVisibility(
//                visible = isHovered,
//                enter = expandHorizontally() + fadeIn(),
//                exit = shrinkHorizontally() + fadeOut()
//            ) {
//                Slider(
//                    value = volume,
//                    onValueChange = onVolumeChange,
//                    modifier = Modifier
//                        .width(90.dp)
//                        .padding(start = 8.dp),
//                    // Custom Circular Thumb
//                    thumb = {
//                        Box(
//                            modifier = Modifier
//                                .size(12.dp)
//                                .background(Color.White, CircleShape)
//                        )
//                    },
//                    // Custom Thin Track
//                    track = { sliderState ->
//                        SliderDefaults.Track(
//                            sliderState = sliderState,
//                            modifier = Modifier.height(2.dp), // Extremely thin track
//                            thumbTrackGapSize = 0.dp,
//                            trackInsideCornerSize = 0.dp,
//                            colors = SliderDefaults.colors(
//                                activeTrackColor = Color.White,
//                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
//                            )
//                        )
//                    }
//                )
//            }
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
private fun PreviewVolumeSliderConsole() {
    var volume by remember { mutableStateOf(0.5f) }

    Box(
        modifier = Modifier
            .background(Color.DarkGray) // Dark background to see the white icons
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // The actual component
            VolumeSliderConsole(
                volume = volume,
                onVolumeChange = { volume = it }
            )

            Spacer(Modifier.height(16.dp))
            Text("Current Volume: ${(volume * 100).toInt()}%", color = Color.White)
        }
    }
}