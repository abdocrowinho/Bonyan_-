package org.muslim_voice.project.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val ArabicFamily = FontFamily.Serif
private val LatinFamily = FontFamily.SansSerif

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = ArabicFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = AppColors.OnBackground,
    ),
    titleLarge = TextStyle(
        fontFamily = ArabicFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        color = AppColors.OnBackground,
    ),
    titleMedium = TextStyle(
        fontFamily = ArabicFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        color = AppColors.OnSurface,
    ),
    bodyLarge = TextStyle(
        fontFamily = ArabicFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = AppColors.OnSurface,
    ),
    bodyMedium = TextStyle(
        fontFamily = ArabicFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = AppColors.OnSurface,
    ),
    labelSmall = TextStyle(
        fontFamily = ArabicFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        color = AppColors.Subtle,
    ),
)

val LatinNumericStyle = TextStyle(
    fontFamily = LatinFamily,
    fontWeight = FontWeight.Medium,
)
