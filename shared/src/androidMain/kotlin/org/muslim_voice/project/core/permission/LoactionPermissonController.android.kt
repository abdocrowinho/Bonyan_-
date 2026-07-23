package org.muslim_voice.project.core.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

class AndroidLocationPermissionController(
    private val context: Context,
    private val launcher: androidx.activity.result.ActivityResultLauncher<Array<String>>,
    private val resultChannel: Channel<PermissionStatus>,
) : LocationPermissionController {

    override suspend fun getStatus(): PermissionStatus {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
    }

    override suspend fun requestPermission(): PermissionStatus {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
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
actual fun rememberLocationPermissionController(): LocationPermissionController {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val resultChannel = remember { Channel<PermissionStatus>() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grantedMap ->
        val fineGranted = grantedMap[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = grantedMap[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        val status = when {
            fineGranted || coarseGranted -> PermissionStatus.GRANTED
            activity != null && !ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) -> PermissionStatus.PERMANENTLY_DENIED
            else -> PermissionStatus.DENIED
        }
        resultChannel.trySend(status)
    }

    return remember {
        AndroidLocationPermissionController(context, launcher, resultChannel)
    }
}