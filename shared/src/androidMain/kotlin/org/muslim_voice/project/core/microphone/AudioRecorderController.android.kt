package org.muslim_voice.project.core.microphone

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.muslim_voice.project.core.permission.MicrophonePermissionController
import org.muslim_voice.project.core.permission.PermissionStatus
import org.muslim_voice.project.core.permission.rememberMicrophonePermissionController
import java.io.File

class AndroidAudioRecorderController(
    private val context: android.content.Context,
    private val permissionController: MicrophonePermissionController,
) : AudioRecorderController {

    private val _status = MutableStateFlow(RecordingStatus.IDLE)
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var outputFile: File? = null

    override fun getStatus(): StateFlow<RecordingStatus> = _status.asStateFlow()

    override suspend fun startRecording() {
        val permission = permissionController.getStatus()
        val granted = if (permission == PermissionStatus.GRANTED) {
            true
        } else {
            permissionController.requestPermission() == PermissionStatus.GRANTED
        }
        if (!granted) {
            _status.value = RecordingStatus.ERROR
            return
        }

        withContext(Dispatchers.IO) {
            stopPlaybackInternal()
            outputFile = File.createTempFile("prayer_voice_", ".m4a", context.cacheDir)
            mediaRecorder = createMediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile!!.absolutePath)
                prepare()
                start()
            }
            _status.value = RecordingStatus.RECORDING
        }
    }

    override suspend fun stopRecording(): ByteArray = withContext(Dispatchers.IO) {
        runCatching {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            val bytes = outputFile?.readBytes() ?: ByteArray(0)
            outputFile?.delete()
            outputFile = null
            _status.value = if (bytes.isNotEmpty()) RecordingStatus.RECORDED else RecordingStatus.IDLE
            bytes
        }.getOrElse {
            _status.value = RecordingStatus.ERROR
            ByteArray(0)
        }
    }

    override suspend fun play(audio: ByteArray) = withContext(Dispatchers.IO) {
        stopPlaybackInternal()
        val tempFile = File.createTempFile("playback_", ".m4a", context.cacheDir)
        tempFile.writeBytes(audio)
        mediaPlayer = MediaPlayer().apply {
            setDataSource(tempFile.absolutePath)
            setOnCompletionListener {
                _status.value = RecordingStatus.RECORDED
                tempFile.delete()
            }
            prepare()
            start()
        }
        _status.value = RecordingStatus.PLAYING
    }

    override suspend fun stopPlayback() = withContext(Dispatchers.IO) {
        stopPlaybackInternal()
        if (_status.value == RecordingStatus.PLAYING) {
            _status.value = RecordingStatus.RECORDED
        }
    }

    private fun stopPlaybackInternal() {
        mediaPlayer?.apply {
            runCatching {
                if (isPlaying) stop()
                release()
            }
        }
        mediaPlayer = null
    }

    private fun createMediaRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }
}

@Composable
actual fun rememberAudioRecorderController(): AudioRecorderController {
    val context = LocalContext.current
    val permissionController = rememberMicrophonePermissionController()
    return remember(context, permissionController) {
        AndroidAudioRecorderController(context, permissionController)
    }
}
