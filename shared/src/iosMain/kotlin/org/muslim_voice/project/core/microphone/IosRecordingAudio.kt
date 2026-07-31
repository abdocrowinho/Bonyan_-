package org.muslim_voice.project.core.microphone

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
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
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioPlayerDelegateProtocol
import platform.AVFAudio.AVAudioRecorder
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVFormatIDKey
import platform.AVFAudio.AVNumberOfChannelsKey
import platform.AVFAudio.AVSampleRateKey
import platform.CoreAudioTypes.kAudioFormatMPEG4AAC
import platform.Foundation.NSDate
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.timeIntervalSince1970
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class IosRecordingManager : RecordingManger {

    private var recorder: AVAudioRecorder? = null
    private var outputPath: String? = null
    private var startTime: Long = 0
    private var player: AVAudioPlayer? = null
    private var playerDelegate: PlaybackDelegate? = null

    private val _status = MutableStateFlow(RecordingStatus.IDLE)
    override fun getStatus(): StateFlow<RecordingStatus> = _status.asStateFlow()

    private val _amplitude = MutableStateFlow(0f)
    override fun getAmplitude(): StateFlow<Float> = _amplitude.asStateFlow()

    private val monitorScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var amplitudeJob: Job? = null

    private class PlaybackDelegate(
        private val onFinished: () -> Unit,
    ) : NSObject(), AVAudioPlayerDelegateProtocol {
        override fun audioPlayerDidFinishPlaying(player: AVAudioPlayer, successfully: Boolean) {
            onFinished()
        }
    }

    override suspend fun hasPermission(): Boolean =
        AVAudioSession.sharedInstance().recordPermission == AVAudioSessionRecordPermissionGranted

    override suspend fun requestPermission(): Boolean = suspendCoroutine { cont ->
        AVAudioSession.sharedInstance().requestRecordPermission { granted ->
            cont.resume(granted)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun startRecording() {
        val docs = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, true
        ).first() as String
        val path = "$docs/prayer_${NSDate().timeIntervalSince1970}.m4a"
        outputPath = path

        val settings: Map<Any?, Any> = mapOf(
            AVFormatIDKey to kAudioFormatMPEG4AAC,
            AVSampleRateKey to 44100.0,
            AVNumberOfChannelsKey to 1,
        )

        @Suppress("UNCHECKED_CAST")
        val noErrorPointer = null as CPointer<ObjCObjectVar<NSError?>>?

        recorder = AVAudioRecorder(
            uRL = NSURL.fileURLWithPath(path),
            settings = settings,
            error = noErrorPointer,
        )

        val prepared = recorder?.prepareToRecord() ?: false
        if (!prepared) {
            throw IllegalStateException("AVAudioRecorder failed to prepare recording at $path")
        }

        recorder?.meteringEnabled = true
        recorder?.record()
        startTime = (NSDate().timeIntervalSince1970 * 1000).toLong()
        _status.value = RecordingStatus.RECORDING
        startAmplitudeMonitoring()
    }

    override suspend fun stopRecording(): RecordedAudio {
        stopAmplitudeMonitoring()
        recorder?.stop()
        val now = (NSDate().timeIntervalSince1970 * 1000).toLong()
        _status.value = RecordingStatus.RECORDED
        return RecordedAudio(outputPath!!, now - startTime)
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun play(audioPath: String) {
        player?.stop()

        @Suppress("UNCHECKED_CAST")
        val noErrorPointer = null as CPointer<ObjCObjectVar<NSError?>>?

        playerDelegate = PlaybackDelegate { _status.value = RecordingStatus.RECORDED }

        player = AVAudioPlayer(
            contentsOfURL = NSURL.fileURLWithPath(audioPath),
            error = noErrorPointer,
        ).apply {
            delegate = playerDelegate
            play()
        }
        _status.value = RecordingStatus.PLAYING
    }

    override suspend fun stopPlayback() {
        player?.stop()
        player = null
        playerDelegate = null
        if (_status.value == RecordingStatus.PLAYING) {
            _status.value = RecordingStatus.RECORDED
        }
    }

    override fun isRecording(): Boolean = recorder?.isRecording() == true

    private fun startAmplitudeMonitoring() {
        amplitudeJob?.cancel()
        amplitudeJob = monitorScope.launch {
            while (isActive) {
                recorder?.updateMeters()
                val db = recorder?.averagePowerForChannel(0u) ?: -160f
                // Typical voice dynamic range is roughly -60dB (quiet) to 0dB (loud).
                val normalized = ((db + 60f) / 60f).coerceIn(0f, 1f)
                _amplitude.value = normalized
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