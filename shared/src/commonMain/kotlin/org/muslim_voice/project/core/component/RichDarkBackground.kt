package org.muslim_voice.project.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import org.muslim_voice.project.core.theme.VoiceOfMuslimColors

@Composable
fun RichDarkBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        VoiceOfMuslimColors.BackgroundDeep,
                        VoiceOfMuslimColors.BackgroundElevated,
                        VoiceOfMuslimColors.BackgroundDeep,
                    ),
                ),
            ),
    ) {
        content()
    }
}
