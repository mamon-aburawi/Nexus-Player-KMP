@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package player.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalSlider
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_volume_off
import io.github.mamon.nexus.resources.ic_volume_on
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


@Composable
internal fun VolumeSliderMobile(
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    activeTrackColor: Color = Color.Red,
    inactiveTrackColor: Color = Color.Black.copy(alpha = 0.3f),
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    var autoHideJob by remember { mutableStateOf<Job?>(null) }

    // Remember the last volume level before muting (defaulting to 0.5f)
    var lastVolume by remember { mutableStateOf(if (volume > 0f) volume else 0.5f) }

    val scope = rememberCoroutineScope()

    val sliderState = rememberSliderState(
        value = volume,
        valueRange = 0f..1f
    )

    LaunchedEffect(volume) {
        if (sliderState.value != volume) {
            sliderState.value = volume
        }
        // Update lastVolume whenever volume changes to a non-zero value
        if (volume > 0f) {
            lastVolume = volume
        }
    }

    val triggerVisibility = {
        isVisible = true
        autoHideJob?.cancel()
        autoHideJob = scope.launch {
            delay(800)
            isVisible = false
        }
    }

    Box(
        modifier = modifier
            .height(200.dp)
            .width(70.dp)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                        triggerVisibility()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f))
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "${(sliderState.value * 100).toInt()}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )

                Spacer(Modifier.height(8.dp))

                VerticalSlider(
                    state = sliderState,
                    reverseDirection = true,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        activeTrackColor = activeTrackColor,
                        inactiveTrackColor = inactiveTrackColor,
                        thumbColor = activeTrackColor
                    )
                )

                LaunchedEffect(sliderState.value) {
                    if (sliderState.value != volume) {
                        onVolumeChange(sliderState.value)
                    }
                }

                Spacer(Modifier.height(8.dp))

                Icon(
                    painter = painterResource(
                        if (volume <= 0f) Res.drawable.ic_volume_off else Res.drawable.ic_volume_on
                    ),
                    contentDescription = "Toggle Mute",
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            triggerVisibility() // Keep UI visible when interacting
                            if (volume > 0f) {
                                // Mute: save current volume and set to 0
                                lastVolume = volume
                                onVolumeChange(0f)
                            } else {
                                // Unmute: restore the last saved volume
                                onVolumeChange(lastVolume)
                            }
                        }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun VolumeSliderPreviewMobile() {

    var previewVolume by remember { mutableStateOf(0.5f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C)),
        contentAlignment = Alignment.CenterEnd
    ) {

        Text(
            text = "Drag vertically on the right edge",
            color = Color.White.copy(alpha = 0.3f),
            modifier = Modifier.align(Alignment.Center).padding(end = 80.dp)
        )

        VolumeSliderMobile(
            volume = previewVolume,
            onVolumeChange = { previewVolume = it },
            modifier = Modifier
                .padding(end = 16.dp)
                .height(250.dp)
        )
    }
}

