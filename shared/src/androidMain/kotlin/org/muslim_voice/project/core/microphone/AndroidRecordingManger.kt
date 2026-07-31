package org.muslim_voice.project.core.microphone

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AndroidRecordingManager(
    private val context: Context,
    private val permissionRequester: AndroidMicrophonePermissionRequester,
) : RecordingManger {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var startTime: Long = 0
    private var player: MediaPlayer? = null

    private val _status = MutableStateFlow(RecordingStatus.IDLE)
    override fun getStatus(): StateFlow<RecordingStatus> = _status.asStateFlow()

    private val _amplitude = MutableStateFlow(0f)
    override fun getAmplitude(): StateFlow<Float> = _amplitude.asStateFlow()

    private val monitorScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var amplitudeJob: Job? = null

    override suspend fun hasPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED

    override suspend fun requestPermission(): Boolean = permissionRequester.request()

    override suspend fun startRecording() {
        outputFile = File(context.cacheDir, "prayer_${System.currentTimeMillis()}.m4a")

        val newRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        recorder = newRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile!!.absolutePath)
            prepare()
            start()
        }
        startTime = System.currentTimeMillis()
        _status.value = RecordingStatus.RECORDING
        startAmplitudeMonitoring()
    }

    override suspend fun stopRecording(): RecordedAudio {
        stopAmplitudeMonitoring()
        recorder?.apply { stop(); release() }
        recorder = null
        _status.value = RecordingStatus.RECORDED
        return RecordedAudio(
            filePath = outputFile!!.absolutePath,
            durationMillis = System.currentTimeMillis() - startTime,
        )
    }

    override suspend fun play(audioPath: String) = withContext(Dispatchers.IO) {
        player?.release()
        player = MediaPlayer().apply {
            setDataSource(audioPath)
            setOnCompletionListener { _status.value = RecordingStatus.RECORDED }
            prepare()
            start()
        }
        _status.value = RecordingStatus.PLAYING
    }

    override suspend fun stopPlayback() = withContext(Dispatchers.IO) {
        player?.apply { stop(); release() }
        player = null
        if (_status.value == RecordingStatus.PLAYING) {
            _status.value = RecordingStatus.RECORDED
        }
    }

    override fun isRecording(): Boolean = recorder != null

    private fun startAmplitudeMonitoring() {
        amplitudeJob?.cancel()
        amplitudeJob = monitorScope.launch {
            while (isActive) {
                val level = runCatching { recorder?.maxAmplitude ?: 0 }.getOrDefault(0)
                _amplitude.value = (level / 32767f).coerceIn(0f, 1f)
                delay(100)
            }
        }
    }

    private fun stopAmplitudeMonitoring() {
        amplitudeJob?.cancel()
        amplitudeJob = null
        _amplitude.value = 0f
    }
}