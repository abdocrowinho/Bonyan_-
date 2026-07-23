package org.muslim_voice.project.core.media

import androidx.compose.runtime.Composable

interface ImagePickerController {
    suspend fun pickFromGallery(): ByteArray?
    suspend fun captureFromCamera(): ByteArray?
}

@Composable
expect fun rememberImagePickerController(): ImagePickerController
