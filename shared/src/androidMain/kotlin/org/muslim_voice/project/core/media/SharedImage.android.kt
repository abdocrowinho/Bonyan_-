package org.muslim_voice.project.core.media

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream
actual class SharedImage(private val bitmap: Bitmap?) {
    actual fun toByteArray(): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 95, stream)
        return stream.toByteArray()
    }
    actual fun toImageBitmap(): ImageBitmap? = bitmap?.asImageBitmap()
}
