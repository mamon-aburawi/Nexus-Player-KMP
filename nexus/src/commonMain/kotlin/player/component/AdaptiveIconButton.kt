package player.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_full_size
import io.github.mamon.nexus.resources.ic_minimize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun AdaptiveIconButton(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    iconChecked: DrawableResource,
    iconUnchecked: DrawableResource,
    tint: Color = Color.White,
    iconSize: Dp = 24.dp,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(
                if (isChecked) iconChecked
                else iconUnchecked
            ),
            contentDescription = if (isChecked) "Checked" else "Unchecked",
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Preview
@Composable
private fun AdaptiveIconButtonPreview(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(30.dp)
    ){
        AdaptiveIconButton(
            isChecked = true,
            iconChecked = Res.drawable.ic_minimize,
            iconUnchecked = Res.drawable.ic_full_size,
            onClick = {  }
        )
        AdaptiveIconButton(
            isChecked = false,
            iconChecked = Res.drawable.ic_minimize,
            iconUnchecked = Res.drawable.ic_full_size,
            onClick = {  }
        )
    }
}