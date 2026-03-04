package player.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import player.utils.getSpeedLabel
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun SpeedPage(
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White,
    currentSpeed: Float,
    speeds: List<Float>,
    onBack: () -> Unit,
    onSelectSpeed: (Float) -> Unit
) {

    Column(
        modifier = modifier
            .heightIn(max = 400.dp)
    ) {
        // Sub-menu Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = "Playback speed",
                modifier = Modifier.weight(1f),
                color = contentColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Box(Modifier.size(48.dp))
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

        val scrollState = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            speeds.forEach { speed ->
                val isSelected = speed == currentSpeed
                SelectionRow(
                    isSelected = isSelected,
                    label = speed.getSpeedLabel(),
                    // We pass null for tag here as speeds usually don't have "HD" style tags
                    tag = null,
                    onClick = { it ->
                        onSelectSpeed(speed)
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
private fun SpeedPagePreview() {
    val mockSpeeds = listOf(0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
    var selectedSpeed by remember { mutableStateOf(1.5f) }

    SpeedPage(
        currentSpeed = selectedSpeed,
        speeds = mockSpeeds,
        onBack = { /* No-op for preview */ },
        onSelectSpeed = { selectedSpeed = it }
    )
}

