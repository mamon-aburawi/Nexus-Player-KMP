package player.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import player.utils.hybridClick

@Composable
internal fun SelectionRow(
    label: String,
    tag: String?,
    contentColor: Color = Color.White,
    onClick: (String) -> Unit,
    isSelected: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .hybridClick { onClick(label) }
            .background(if (isSelected) Color.White.copy(alpha = 0.1f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.End
    ) {
        // Split label for the "HD/4K" tags
        if (!tag.isNullOrEmpty()){
            Text(tag, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(8.dp))
        Text(text = label, color = contentColor, fontSize = 16.sp)
    }
}

@Preview
@Composable
private fun SelectionRowPreview(){
    SelectionRow(
        label = "1080p",
        tag = "FHD",
        onClick = {},
        isSelected = true
    )
}