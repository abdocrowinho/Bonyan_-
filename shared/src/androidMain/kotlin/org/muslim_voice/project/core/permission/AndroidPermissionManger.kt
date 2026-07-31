package org.muslim_voice.project.core.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.Channel

// androidMain
class AndroidPermissionsManager(
    private val activity: ComponentActivity,
) : PermissionsManager {

    private val resultChannels = mutableMapOf<String, Channel<Boolean>>()
    private val launchers = mutableMapOf<String, ActivityResultLauncher<String>>()

    init {
        for (permission in manifestPermissionsUsed()) {
            launchers[permission] = activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                resultChannels[permission]?.trySend(granted)
            }
        }
    }

    override suspend fun hasPermission(permission: AppPermission): Boolean {
        val manifestPermission = permission.toManifestPermission() ?: return true // e.g. gallery needs none
        return ContextCompat.checkSelfPermission(activity, manifestPermission) ==
                PackageManager.PERMISSION_GRANTED
    }

    override suspend fun requestPermission(permission: AppPermission): Boolean {
        val manifestPermission = permission.toManifestPermission() ?: return true
        val channel = Channel<Boolean>(capacity = 1)
        resultChannels[manifestPermission] = channel
        launchers[manifestPermission]?.launch(manifestPermission)
        return channel.receive()
    }

    private fun AppPermission.toManifestPermission(): String? = when (this) {
        AppPermission.MICROPHONE -> Manifest.permission.RECORD_AUDIO
        AppPermission.CAMERA -> Manifest.permission.CAMERA
        AppPermission.LOCATION -> Manifest.permission.LOCATION_HARDWARE
        AppPermission.GALLERY -> null // modern picker needs no permission
    }

    private fun manifestPermissionsUsed() = listOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
    )
}