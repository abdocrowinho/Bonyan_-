package org.muslim_voice.project.features.auth.register.component.prayerVoice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.constants.PrayerType
import org.muslim_voice.project.core.microphone.RecordingManger
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.features.auth.register.component.ElapsedTimerText
import org.muslim_voice.project.features.auth.register.component.LiveWaveform
import org.muslim_voice.project.features.auth.register.component.PlaybackPulseIndicator
import org.muslim_voice.project.features.auth.register.intent.RegisterIntent
import org.muslim_voice.project.features.auth.register.ui_Model.arabicLabel

@Composable
 fun PrayerVoiceRow(
    prayType: PrayerType,
    hasRecording: Boolean,
    isRecording: Boolean,
    isPlaying: Boolean,
    recordingManager: RecordingManger,
    onIntent: (RegisterIntent) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AppColors.Surface,
        tonalElevation = 1.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = prayType.arabicLabel(),
                        color = AppColors.OnSurface,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = when {
                            isRecording -> "جارٍ التسجيل..."
                            isPlaying -> "جارٍ الاستماع..."
                            hasRecording -> "تم تسجيل الصوت"
                            else -> "لم يتم التسجيل بعد"
                        },
                        color = AppColors.Subtle,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                if (isRecording || isPlaying) {
                    ElapsedTimerText(isActive = true, modifier = Modifier.padding(end = 8.dp))
                }

                IconButton(
                    onClick = {
                        if (isRecording) {
                            onIntent(RegisterIntent.OnStopRecording(prayType))
                        } else {
                            onIntent(RegisterIntent.OnStartRecording(prayType))
                        }
                    },
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
                        contentDescription = null,
                        tint = if (isRecording) AppColors.Error else AppColors.Primary,
                    )
                }

                IconButton(
                    onClick = { onIntent(RegisterIntent.OnPlayRecording(prayType)) },
                    enabled = hasRecording && !isPlaying,
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = if (hasRecording) AppColors.Primary else AppColors.Subtle,
                    )
                }
            }

            if (isRecording) {
                Spacer(modifier = Modifier.height(8.dp))
                val amplitude by recordingManager.getAmplitude().collectAsState()
                LiveWaveform(amplitude = amplitude)
            }

            if (isPlaying) {
                Spacer(modifier = Modifier.height(8.dp))
                PlaybackPulseIndicator()
            }
        }
    }
}