package org.muslim_voice.project.core.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import org.muslim_voice.project.core.permission.AppPermission
import org.muslim_voice.project.core.permission.PermissionsManager
import kotlin.coroutines.resume

// androidMain — ImagePicker.android.kt
class AndroidImagePicker(
    private val context: Context,
    private val permissionsManager: PermissionsManager,
) : ImagePicker {

    var galleryLauncher: ActivityResultLauncher<PickVisualMediaRequest>? = null
    var cameraLauncher: ActivityResultLauncher<Void?>? = null

    private var galleryContinuation: CancellableContinuation<SharedImage?>? = null
    private var cameraContinuation: CancellableContinuation<SharedImage?>? = null

    override suspend fun getImageFromGallery(): SharedImage? =
        suspendCancellableCoroutine { cont ->
            galleryContinuation = cont
            galleryLauncher?.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

    override suspend fun getImageFromCamera(): SharedImage? {
        val granted = permissionsManager.hasPermission(AppPermission.CAMERA)
                || permissionsManager.requestPermission(AppPermission.CAMERA)
        if (!granted) return null

        return suspendCancellableCoroutine { cont ->
            cameraContinuation = cont
            cameraLauncher?.launch(null)
        }
    }

    fun onGalleryResult(uri: Uri?) {
        val bitmap = uri?.let {
            context.contentResolver.openInputStream(it)?.use(BitmapFactory::decodeStream)
        }
        galleryContinuation?.resume(bitmap?.let(::SharedImage))
        galleryContinuation = null
    }

    fun onCameraResult(bitmap: Bitmap?) {
        cameraContinuation?.resume(bitmap?.let(::SharedImage))
        cameraContinuation = null
    }
}

@Composable
actual fun rememberImagePicker(permissionsManager: PermissionsManager): ImagePicker {
    val context = LocalContext.current
    val picker = remember { AndroidImagePicker(context, permissionsManager) }

    picker.galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> picker.onGalleryResult(uri) }

    picker.cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap -> picker.onCameraResult(bitmap) }

    return picker
}