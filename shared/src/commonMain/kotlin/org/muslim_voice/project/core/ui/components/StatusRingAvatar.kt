package org.muslim_voice.project.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.core.ui.theme.AppShapes

enum class StatusRingColor {
    GREEN, YELLOW, RED;

    fun toColor(): Color = when (this) {
        GREEN -> AppColors.StatusGreen
        YELLOW -> AppColors.StatusYellow
        RED -> AppColors.StatusRed
    }
}

@Composable
fun StatusRingAvatar(
    emoji: String,
    ringColor: StatusRingColor,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    ringWidth: Dp = 3.dp,
) {
    Box(
        modifier = modifier
            .size(size + ringWidth * 2)
            .border(width = ringWidth, color = ringColor.toColor(), shape = AppShapes.Full)
            .clip(CircleShape)
            .background(AppColors.PrimaryLight),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = emoji, modifier = Modifier.size(size))
    }
}
