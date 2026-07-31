package org.muslim_voice.project.features.auth.register.component.prayerVoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.component.AppButton
import org.muslim_voice.project.core.constants.PrayerType
import org.muslim_voice.project.core.microphone.RecordingManger
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.features.auth.register.intent.RegisterIntent
import org.muslim_voice.project.features.auth.register.state.RegisterState

@Composable
 fun PrayerVoicesStep(
    state: RegisterState,
    recordingManager: RecordingManger,
    onIntent: (RegisterIntent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = "ادخل الصوت الذي سيسمعه اصدقائك عند كل صلاة",
            style = MaterialTheme.typography.bodyLarge,
            color = AppColors.OnSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        PrayerType.entries.forEach { prayType ->
            PrayerVoiceRow(
                prayType = prayType,
                hasRecording = state.registerInfo. recordings.containsKey(prayType),
                isRecording = state.currentlyRecording == prayType,
                isPlaying = state.currentlyPlaying == prayType,
                recordingManager = recordingManager,
                onIntent = onIntent,
            )
        }

        AppButton(
            text = "التالي",
            onClick = { onIntent(RegisterIntent.OnStep2NextClicked) },
            enabled = PrayerType.entries.all { state.registerInfo .recordings.containsKey(it) },
        )
    }
}
