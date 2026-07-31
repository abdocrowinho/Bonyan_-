package org.muslim_voice.project.core.microphone

import kotlinx.coroutines.flow.StateFlow

enum class RecordingStatus {
    IDLE,
    RECORDING,
    RECORDED,
    PLAYING,
    ERROR,
}

interface RecordingManger {
    suspend fun hasPermission(): Boolean
    suspend fun requestPermission(): Boolean
    suspend fun startRecording()
    suspend fun stopRecording(): RecordedAudio
    suspend fun play(audioPath: String)
    suspend fun stopPlayback()
    fun isRecording(): Boolean
    fun getStatus(): StateFlow<RecordingStatus>
    fun getAmplitude(): StateFlow<Float>   // normalized 0f–1f, live mic level while RECORDING
}

data class RecordedAudio(
    val filePath: String,
    val durationMillis: Long,
)