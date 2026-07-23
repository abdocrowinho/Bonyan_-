package org.muslim_voice.project.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.ui.theme.AppColors

enum class DayDotState { DONE, MISS, TODAY, FUTURE }

@Composable
fun DayDot(
    label: String,
    state: DayDotState,
    modifier: Modifier = Modifier,
) {
    val background = when (state) {
        DayDotState.DONE -> AppColors.StatusGreen
        DayDotState.MISS -> AppColors.StatusRed
        DayDotState.TODAY -> AppColors.Primary
        DayDotState.FUTURE -> AppColors.Divider
    }
    val borderModifier = if (state == DayDotState.TODAY) {
        Modifier.border(2.dp, AppColors.Accent, CircleShape)
    } else {
        Modifier
    }
    Box(
        modifier = modifier.size(30.dp).then(borderModifier).clip(CircleShape).background(background),
        contentAlignment = Alignment.Center,
    ) {
        when (state) {
            DayDotState.DONE -> Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = AppColors.OnPrimary,
                modifier = Modifier.size(16.dp),
            )
            else -> Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (state == DayDotState.FUTURE) AppColors.Subtle else AppColors.OnPrimary,
            )
        }
    }
}
