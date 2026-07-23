package org.muslim_voice.project.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class IosImagePickerController : ImagePickerController {
    override suspend fun pickFromGallery(): ByteArray? = null

    override suspend fun captureFromCamera(): ByteArray? = null
}

@Composable
actual fun rememberImagePickerController(): ImagePickerController {
    return remember { IosImagePickerController() }
}
