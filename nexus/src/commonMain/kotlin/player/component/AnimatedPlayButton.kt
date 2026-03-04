package player.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_pause
import io.github.mamon.nexus.resources.ic_play
import org.jetbrains.compose.resources.painterResource




@Composable
internal fun AnimatedPlayButton(
    visible: Boolean = true,
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = isPlaying, label = "PlayPauseTransition")

    // Bouncy pop effect when state changes
    val scale by transition.animateFloat(
        label = "Scale",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        }
    ) { state -> if (state) 1.15f else 1.0f }

    // Smooth rotation from -45 to 0 degrees
    val rotation by transition.animateFloat(
        label = "Rotation",
        transitionSpec = { tween(durationMillis = 400, easing = FastOutSlowInEasing) }
    ) { state -> if (state) 0f else -120f }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f),
        modifier = modifier
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = rotation
            }
        ) {
            Crossfade(
                targetState = isPlaying,
                animationSpec = tween(250),
                label = "IconCrossfade"
            ) { playing ->
                Icon(
                    painter = painterResource(if (playing) Res.drawable.ic_pause else Res.drawable.ic_play),
                    contentDescription = if (playing) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

}


@Preview
@Composable
private fun PlayButtonPreview() {
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // Interactive Toggle to test the actual spring and rotation
        var isPlaying by remember { mutableStateOf(false) }
        AnimatedPlayButton(
            isPlaying = isPlaying,
            onClick = { isPlaying = !isPlaying }
        )
        Text(if (isPlaying) "Tap to Pause" else "Tap to Play", color = Color.White, fontSize = 12.sp)

    }
}