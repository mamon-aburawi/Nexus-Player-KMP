package player.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import player.utils.Platform
import player.utils.getPlatform
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_exit
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun PlayerAppBar(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    title: String,
    backgroundColor: Color =  Color.Black.copy(alpha = 0.2f),
    onExit: () -> Unit
) {
    val isMobile = getPlatform() == Platform.ANDROID || getPlatform() == Platform.IOS

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        while (isMobile) {
            scrollState.animateScrollTo(
                value = scrollState.maxValue,
                animationSpec = tween(
                    durationMillis = title.length * 250,
                    easing = LinearEasing
                )
            )
            delay(1000)
            scrollState.scrollTo(0)
            delay(500)
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .systemBarsPadding()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (isMobile) {
                // Exit/Back Button
                IconButton(onClick = { onExit() }) {
                    Icon(
                        painterResource(Res.drawable.ic_exit), "Exit", Modifier.size(24.dp), Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(scrollState, enabled = false)
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            } else {

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
            }
        }
    }

}


@Preview
@Composable
private fun PlayerAppBarPreview(){
    PlayerAppBar(
        title = "Spider Man 3",
        onExit = {}
    )
}