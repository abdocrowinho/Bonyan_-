package org.muslim_voice.project.core.media

import androidx.compose.runtime.Composable
import org.muslim_voice.project.core.permission.PermissionsManager

interface ImagePicker {
    suspend fun getImageFromGallery(): SharedImage?
    suspend fun getImageFromCamera(): SharedImage?
}

@Composable
expect fun rememberImagePicker(permissionsManager: PermissionsManager): ImagePicker