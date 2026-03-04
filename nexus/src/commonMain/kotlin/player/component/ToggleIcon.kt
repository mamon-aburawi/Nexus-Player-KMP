package player.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_pause
import io.github.mamon.nexus.resources.ic_play
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource



@Composable
internal fun ToggleIcon(
    checked: Boolean,
    iconChecked: DrawableResource,
    iconUnChecked: DrawableResource,
    onCheckedChange: (Boolean) -> Unit
) {

    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        modifier = Modifier
            .width(38.dp)
            .height(22.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f))
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = (thumbOffset * 16).dp)
                .padding(2.dp)
                .size(18.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    if (checked) iconUnChecked else iconChecked
                ),
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(10.dp)
            )
        }
    }
}

@Preview
@Composable
fun ToggleIconPreview(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        ToggleIcon(
            checked = true,
            iconChecked = Res.drawable.ic_play,
            iconUnChecked = Res.drawable.ic_pause,
            onCheckedChange = {},
        )

        ToggleIcon(
            checked = false,
            iconChecked = Res.drawable.ic_play,
            iconUnChecked = Res.drawable.ic_pause,
            onCheckedChange = {},
        )
    }
}