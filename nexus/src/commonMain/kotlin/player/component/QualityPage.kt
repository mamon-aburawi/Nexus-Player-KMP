package player.component


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import player.module.VideoQuality
import player.utils.dommyQualityData

import player.utils.hybridClick
import io.github.mamon.nexus.resources.Res
import io.github.mamon.nexus.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun QualityPage(
    modifier: Modifier = Modifier,
    currentQuality: VideoQuality,
    contentColor: Color = Color.White,
    qualities: List<VideoQuality>,
    onBack: () -> Unit,
    onSelectQuality: (VideoQuality) -> Unit
) {
    Column(modifier = modifier.heightIn(max = 400.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {onBack()}) {
                Icon(painterResource(Res.drawable.ic_arrow_back), null, tint = contentColor, modifier = Modifier.size(20.dp))
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .hybridClick(
                        enabled = false,
                        rippleEnabled = false,
                        hoverEnabled = false
                    ){},
                text = "Quality", color = contentColor, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

        // Scrollable List
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollState)) {

            qualities.forEach { quality ->
                val isSelected = currentQuality.label == quality.label
                SelectionRow(
                    isSelected = isSelected,
                    label = quality.label ?: "",
                    tag = quality.tag.toString(),
                    onClick = {
                        val quality = qualities.find { it.label == quality.label }
                        if (quality != null) {
                            onSelectQuality(quality)
                        }
                    })
            }
        }
    }
}





@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
private fun QualityPreview(){
    QualityPage(
        modifier = Modifier.width(320.dp),
        qualities = dommyQualityData,
        onBack = {},
        onSelectQuality = {},
        currentQuality =  dommyQualityData[1]
    )
}
