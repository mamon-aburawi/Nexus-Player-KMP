package player.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
internal fun Modifier.hybridClick(
    enabled: Boolean = true,
    rippleEnabled: Boolean = true,
    hoverEnabled: Boolean = true, // New Parameter
    rippleColor: Color = Color.White.copy(alpha = 0.1f),
    hoverColor: Color = Color.White.copy(alpha = 0.1f),
    cornerRadius: Dp = 8.dp,
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val platform = getPlatform()
    val isMobile = platform == Platform.IOS || platform == Platform.ANDROID

    // 1. Hover Logic: Background color only changes if:
    //    - Not on mobile
    //    - Hover is enabled
    //    - Item is actually being hovered
    val backgroundColor by animateColorAsState(
        targetValue = if (!isMobile && hoverEnabled && isHovered) hoverColor else Color.Transparent,
        animationSpec = tween(durationMillis = 200)
    )

    return this
        .background(backgroundColor, RoundedCornerShape(cornerRadius))
        .hoverable(enabled = hoverEnabled, interactionSource = interactionSource)
        .clickable(
            interactionSource = interactionSource,
            // 2. Ripple Logic: Shows only on Mobile when rippleEnabled is true
            indication = if (isMobile && rippleEnabled) ripple(color = rippleColor) else null,
            enabled = enabled,
            onClick = onClick
        )
}

//@Composable
//fun Modifier.onClick(
//    showRipple: Boolean = true,
//    rippleColor: Color = Color.White.copy(alpha = 0.1f), // Default color
//    enabled: Boolean = true,
//    onClick: () -> Unit
//): Modifier {
//    val interactionSource = remember { MutableInteractionSource() }
//
//    return this.clickable(
//        interactionSource = interactionSource,
//        // Using the M3 ripple API
//        indication = if (showRipple) ripple(
//            color = rippleColor,
//            bounded = true // Keeps ripple inside the Row boundaries
//        ) else null,
//        enabled = enabled,
//        onClick = onClick
//    )
//}