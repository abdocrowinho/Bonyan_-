package org.muslim_voice.project.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.ui.theme.AppColors

@Composable
fun AnimatedWaveform(
    modifier: Modifier = Modifier,
    color: Color = AppColors.Accent,
    barCount: Int = 5,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    Row(
        modifier = modifier.height(20.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(barCount) { index ->
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.35f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 500, delayMillis = index * 90, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "bar_$index",
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((20 * scale).dp)
                    .background(color, RoundedCornerShape(2.dp)),
            )
        }
    }
}
