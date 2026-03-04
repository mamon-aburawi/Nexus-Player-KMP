package player.component


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import player.utils.hybridClick


@Composable
internal fun ToggleText(
    title: String,
    enabled: Boolean,
    contentColor: Color = Color.White,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    var checked by remember { mutableStateOf(enabled) }

    // Animate the thumb position (0f to 1f)
    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 200)
    )

    // Animate the track color to match the "active" feel in the video
    val trackColor by animateColorAsState(
        targetValue = if (checked) contentColor.copy(alpha = 0.4f) else contentColor.copy(alpha = 0.1f)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .hybridClick(
                rippleEnabled = false
            ){
                checked = !checked
                onCheckedChange(checked)
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End // Matches the RTL layout in the video
    ) {
        // The Toggle Switch
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(20.dp)
                .clip(CircleShape)
                .background(trackColor),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = (thumbOffset * 16).dp) // Slides 16dp to the right
                    .padding(2.dp)
                    .size(16.dp)
                    .background(Color.White, CircleShape)
            )
        }

        Spacer(Modifier.width(16.dp))

        // Title text
        Text(
            text = title,
            color = contentColor,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}



@Preview
@Composable
private fun TogglePreview() {
    Column(modifier = Modifier
        .background(Color.Black)
        .width(320.dp)
        .padding(10.dp)) {
        ToggleText(title = "Fixed volume level", enabled = true)
        ToggleText(title = "Audio enhancement", enabled = false)
    }
}