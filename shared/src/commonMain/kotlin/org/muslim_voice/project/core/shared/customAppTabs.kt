package org.muslim_voice.project.core.shared

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import org.muslim_voice.project.core.utilities.sdp
import org.muslim_voice.project.core.utilities.ssp

data class TabItem<T>(
    val title: String,
    val value: T
)

@Composable
fun <T> CustomBaseTabs(
    tabs: List<TabItem<T>>,
    selectedTab: T,
    onTabSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    widthOfTab: Dp = 100.sdp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(24.sdp)
) {
    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = tab.value == selectedTab

                CustomTabButton(
                    title = tab.title,
                    selected = isSelected,
                    onClick = { onTabSelected(tab.value) },
                    modifier = Modifier,
                    widthOfTab = widthOfTab
                )
            }
        }
        Spacer(Modifier.height(8.sdp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.5.sdp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary .copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
@Composable
private fun CustomTabButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    widthOfTab: Dp = 100.sdp
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(.1f) else Color.Transparent
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) Color(0xFFFFFFFF) else Color(0xFFD2D4D9)
    )
    val fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal

    val borderBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00AE6B),
            Color(0xFF011A26)
        )
    )

    Box(
        modifier = modifier
            .run {
                if (selected) {
                    this.shadow(
                        elevation = 4.sdp,
                        shape = RoundedCornerShape(topStart = 24.sdp, topEnd = 24.sdp),
                        ambientColor = Color(0xFF00AE6B).copy(alpha = 0.11f),
                        spotColor = Color(0xFF00AE6B).copy(alpha = 0.11f)
                    )
                } else this
            }
            .clip(RoundedCornerShape(topStart = 24.sdp, topEnd = 24.sdp))
            .background(backgroundColor)
            .run {
                if (selected) {
                    this.border(
                        width = 0.5.sdp,
                        brush = borderBrush,
                        shape = RoundedCornerShape(topStart = 24.sdp, topEnd = 24.sdp)
                    )
                } else this
            }
            .clickable { onClick() }
            .padding(20.sdp)
            .width(widthOfTab),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 16.ssp,
            fontWeight = fontWeight,
            letterSpacing = (-0.18).sp,
            textAlign = TextAlign.Center
        )
    }
}