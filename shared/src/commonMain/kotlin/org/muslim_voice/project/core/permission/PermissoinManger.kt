package org.muslim_voice.project.core.permission

enum class AppPermission {
    MICROPHONE,
    CAMERA,
    GALLERY,
    LOCATION
}

interface PermissionsManager {
    suspend fun hasPermission(permission: AppPermission): Boolean
    suspend fun requestPermission(permission: AppPermission): Boolean
}