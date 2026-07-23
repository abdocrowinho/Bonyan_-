package org.muslim_voice.project.core.microphone

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

enum class RecordingStatus {
    IDLE,
    RECORDING,
    RECORDED,
    PLAYING,
    ERROR,
}

interface AudioRecorderController {
    suspend fun startRecording()
    suspend fun stopRecording(): ByteArray
    suspend fun play(audio: ByteArray)
    suspend fun stopPlayback()
    fun getStatus(): StateFlow<RecordingStatus>
}

@Composable
expect fun rememberAudioRecorderController(): AudioRecorderController
