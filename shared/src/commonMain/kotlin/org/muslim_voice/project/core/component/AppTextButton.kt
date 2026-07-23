package org.muslim_voice.project.core.component

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.core.ui.theme.AppTypography

@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = AppColors.Primary,
    enabled: Boolean = true,
    fontSize: TextUnit = 14.sp,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(
            text = text,
            style = AppTypography.bodyMedium.copy(
                fontSize = fontSize,
                color = if (enabled) color else color.copy(alpha = 0.5f),
            ),
        )
    }
}
