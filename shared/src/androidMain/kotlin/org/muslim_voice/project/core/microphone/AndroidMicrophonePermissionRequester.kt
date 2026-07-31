package org.muslim_voice.project.core.microphone;


import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.channels.Channel

class AndroidMicrophonePermissionRequester(activity: ComponentActivity) {

    private val resultChannel = Channel<Boolean>(capacity = 1)

    private val launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
    ) { granted ->
            resultChannel.trySend(granted)
    }

    suspend fun request(): Boolean {
        launcher.launch(Manifest.permission.RECORD_AUDIO)
        return resultChannel.receive()
    }
}