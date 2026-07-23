package org.muslim_voice.project.core.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.Channel

class AndroidMicrophonePermissionController(
    private val context: Context,
    private val launcher: androidx.activity.result.ActivityResultLauncher<String>,
    private val resultChannel: Channel<PermissionStatus>,
) : MicrophonePermissionController {

    override suspend fun getStatus(): PermissionStatus {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO,
        ) == PackageManager.PERMISSION_GRANTED
        return if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
    }

    override suspend fun requestPermission(): PermissionStatus {
        launcher.launch(Manifest.permission.RECORD_AUDIO)
        return resultChannel.receive()
    }

    override fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}

@Composable
actual fun rememberMicrophonePermissionController(): MicrophonePermissionController {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val resultChannel = remember { Channel<PermissionStatus>() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        val status = when {
            granted -> PermissionStatus.GRANTED
            activity != null && !ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.RECORD_AUDIO,
            ) -> PermissionStatus.PERMANENTLY_DENIED

            else -> PermissionStatus.DENIED
        }
        resultChannel.trySend(status)
    }

    return remember {
        AndroidMicrophonePermissionController(context, launcher, resultChannel)
    }
}
