package org.muslim_voice.project.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    primaryContainer = AppColors.PrimaryLight,
    secondary = AppColors.Accent,
    onSecondary = AppColors.OnPrimary,
    secondaryContainer = AppColors.AccentLight,
    background = AppColors.Background,
    onBackground = AppColors.OnBackground,
    surface = AppColors.Surface,
    onSurface = AppColors.OnSurface,
    error = AppColors.Error,
    outline = AppColors.Divider,
)

@Composable
fun MuslimVoiceTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = AppTypography,
            content = content,
        )
    }
}
