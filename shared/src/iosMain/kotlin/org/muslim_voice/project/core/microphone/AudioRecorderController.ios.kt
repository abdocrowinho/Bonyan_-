package org.muslim_voice.project.core.microphone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IosAudioRecorderController : AudioRecorderController {
    private val _status = MutableStateFlow(RecordingStatus.IDLE)

    override fun getStatus(): StateFlow<RecordingStatus> = _status.asStateFlow()

    override suspend fun startRecording() {
        _status.value = RecordingStatus.ERROR
    }

    override suspend fun stopRecording(): ByteArray {
        _status.value = RecordingStatus.IDLE
        return ByteArray(0)
    }

    override suspend fun play(audio: ByteArray) = Unit

    override suspend fun stopPlayback() = Unit
}

@Composable
actual fun rememberAudioRecorderController(): AudioRecorderController {
    return remember { IosAudioRecorderController() }
}
