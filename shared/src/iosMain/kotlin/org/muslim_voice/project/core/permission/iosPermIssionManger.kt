package org.muslim_voice.project.core.permission

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import kotlin.coroutines.resume

// iosMain
class IosPermissionsManager : PermissionsManager {

    override suspend fun hasPermission(permission: AppPermission): Boolean = when (permission) {
        AppPermission.MICROPHONE -> {
            AVAudioSession.sharedInstance().recordPermission == AVAudioSessionRecordPermissionGranted
        }
        AppPermission.CAMERA -> {
            AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) ==
                    AVAuthorizationStatusAuthorized
        }
        AppPermission.LOCATION -> {false} // must add logic there there
        AppPermission.GALLERY -> true // PHPickerViewController needs no permission
    }

    override suspend fun requestPermission(permission: AppPermission): Boolean = when (permission) {
        AppPermission.MICROPHONE -> requestMicrophonePermission()
        AppPermission.CAMERA -> requestCameraPermission()
        AppPermission.LOCATION -> {false}

        AppPermission.GALLERY -> true
    }

    private suspend fun requestMicrophonePermission(): Boolean =
        suspendCancellableCoroutine { cont ->
            AVAudioSession.sharedInstance().requestRecordPermission { granted ->
                cont.resume(granted)
            }
        }

    private suspend fun requestCameraPermission(): Boolean =
        suspendCancellableCoroutine { cont ->
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                cont.resume(granted)
            }
        }
}