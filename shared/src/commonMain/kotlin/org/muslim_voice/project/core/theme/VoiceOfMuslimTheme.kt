package org.muslim_voice.project.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val PremiumDarkScheme = darkColorScheme(
    primary = VoiceOfMuslimColors.GoldPrimary,
    onPrimary = VoiceOfMuslimColors.BackgroundDeep,
    secondary = VoiceOfMuslimColors.GreenBright,
    onSecondary = VoiceOfMuslimColors.TextPrimary,
    background = VoiceOfMuslimColors.BackgroundDeep,
    onBackground = VoiceOfMuslimColors.TextPrimary,
    surface = VoiceOfMuslimColors.BackgroundElevated,
    onSurface = VoiceOfMuslimColors.TextPrimary,
    error = VoiceOfMuslimColors.ErrorRed,
)

val AmiriFontFamily = FontFamily.Serif

@Composable
fun VoiceOfMuslimTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = PremiumDarkScheme,
        typography = MaterialTheme.typography.copy(
            headlineLarge = TextStyle(
                fontFamily = AmiriFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            ),
            titleLarge = TextStyle(
                fontFamily = AmiriFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
            ),
            bodyLarge = TextStyle(
                fontFamily = AmiriFontFamily,
                fontSize = 18.sp,
            ),
            bodyMedium = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 16.sp,
            ),
        ),
        content = content,
    )
}
