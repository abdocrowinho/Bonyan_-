package org.muslim_voice.project.core.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionRecordPermissionDenied
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVAudioSessionRecordPermissionUndetermined
import platform.AVFAudio.recordPermission
import platform.AVFAudio.requestRecordPermission
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import kotlin.coroutines.resume

class IosMicrophonePermissionController : MicrophonePermissionController {

    private val audioSession = AVAudioSession.sharedInstance()

    override suspend fun getStatus(): PermissionStatus {
        return mapStatus(audioSession.recordPermission())
    }

    override suspend fun requestPermission(): PermissionStatus =
        suspendCancellableCoroutine { continuation ->
            audioSession.requestRecordPermission { granted ->
                if (continuation.isActive) {
                    continuation.resume(
                        if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED,
                    )
                }
            }
        }

    override fun openAppSettings() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        if (url != null) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    private fun mapStatus(status: Long): PermissionStatus = when (status) {
        AVAudioSessionRecordPermissionGranted -> PermissionStatus.GRANTED
        AVAudioSessionRecordPermissionDenied -> PermissionStatus.PERMANENTLY_DENIED
        AVAudioSessionRecordPermissionUndetermined -> PermissionStatus.DENIED
        else -> PermissionStatus.DENIED
    }
}

@Composable
actual fun rememberMicrophonePermissionController(): MicrophonePermissionController {
    return remember { IosMicrophonePermissionController() }
}
