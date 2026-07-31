package org.muslim_voice.project.core.media

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy

actual class SharedImage(private val image: UIImage?) {
    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? {
        val data = image?.let { UIImageJPEGRepresentation(it, 0.95) } ?: return null
        return ByteArray(data.length.toInt()).apply {
            usePinned { pinned -> memcpy(pinned.addressOf(0), data.bytes, data.length) }
        }
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val bytes = toByteArray() ?: return null
        return org.jetbrains.skia.Image.makeFromEncoded(bytes).toComposeImageBitmap()
    }
}
