package org.muslim_voice.project.core.permission

import androidx.compose.runtime.Composable

interface MicrophonePermissionController {
    suspend fun getStatus(): PermissionStatus
    suspend fun requestPermission(): PermissionStatus
    fun openAppSettings()
}

@Composable
expect fun rememberMicrophonePermissionController(): MicrophonePermissionController
