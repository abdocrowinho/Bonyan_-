package org.muslim_voice.project.features.auth.register.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.muslim_voice.project.core.ui.theme.AppColors

@Composable
 fun ElapsedTimerText(isActive: Boolean, modifier: Modifier = Modifier) {
    var elapsedSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(isActive) {
        if (isActive) {
            elapsedSeconds = 0
            while (true) {
                delay(1000)
                elapsedSeconds++
            }
        } else {
            elapsedSeconds = 0
        }
    }

    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    Text(
        text = "$minutes:${seconds.toString().padStart(2, '0')}",
        style = MaterialTheme.typography.bodySmall,
        color = AppColors.Subtle,
        modifier = modifier,
    )
}

@Composable
 fun LiveWaveform(
    amplitude: Float,
    modifier: Modifier = Modifier,
    barCount: Int = 24,
) {
    val history = remember { mutableStateListOf<Float>().apply { repeat(barCount) { add(0f) } } }

    LaunchedEffect(amplitude) {
        history.removeAt(0)
        history.add(amplitude)
    }

    Row(
        modifier = modifier.fillMaxWidth().height(32.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        history.forEach { level ->
            val animatedHeight by animateDpAsState(
                targetValue = (4 + level * 28).dp,
                label = "waveformBar",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(animatedHeight).clip
                    (RoundedCornerShape(2.dp))
                    .background(AppColors.Primary),
            )
        }
    }
}

@Composable
 fun PlaybackPulseIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "playback")
    Row(
        modifier = modifier.height(32.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(5) { index ->
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(500, delayMillis = index * 90, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "pulseBar$index",
            )
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight(scale)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AppColors.Primary),
            )
        }
    }
}