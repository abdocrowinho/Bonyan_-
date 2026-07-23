package org.muslim_voice.project.core.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PulsingRingEffect(
    modifier: Modifier = Modifier,
    color: Color,
    cornerRadius: Dp = 16.dp,
    enabled: Boolean = true,
) {
    if (!enabled) return
    val transition = rememberInfiniteTransition(label = "pulse_ring")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(animation = tween(900), repeatMode = RepeatMode.Reverse),
        label = "pulse_scale",
    )
    val alpha by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(animation = tween(900), repeatMode = RepeatMode.Reverse),
        label = "pulse_alpha",
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .border(
                width = (2 * scale).dp,
                color = color.copy(alpha = alpha),
                shape = RoundedCornerShape(cornerRadius * scale),
            ),
    )
}
