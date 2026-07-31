package org.muslim_voice.project.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.suspendCancellableCoroutine
import org.muslim_voice.project.core.permission.AppPermission
import org.muslim_voice.project.core.permission.PermissionsManager
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import kotlin.coroutines.resume

// iosMain — ImagePicker.ios.kt
class IosImagePicker(
    private val permissionsManager: PermissionsManager,
) : ImagePicker {

    // kept as properties so ARC doesn't release them mid-flow
    private var galleryDelegate: PHPickerViewControllerDelegateProtocol? = null
    private var cameraDelegate: UIImagePickerControllerDelegateProtocol? = null

    override suspend fun getImageFromGallery(): SharedImage? =
        suspendCancellableCoroutine { cont ->
            val config = PHPickerConfiguration().apply { filter = PHPickerFilter.imagesFilter }
            val picker = PHPickerViewController(configuration = config)

            galleryDelegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
                override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                    picker.dismissViewControllerAnimated(true, completion = null)

                    val provider = (didFinishPicking.firstOrNull() as? PHPickerResult)?.itemProvider

                    if (provider == null) {
                        cont.resume(null)
                        return
                    }

                    provider.loadDataRepresentationForTypeIdentifier("public.image") { data, error ->
                        if (data != null && error == null) {
                            val image = UIImage.imageWithData(data)
                            cont.resume(SharedImage(image))
                        } else {
                            cont.resume(null)
                        }
                    }
                }
            }
            picker.delegate = galleryDelegate
            currentViewController()?.presentViewController(picker, true, null)
        }

    override suspend fun getImageFromCamera(): SharedImage? {
        val granted = permissionsManager.hasPermission(AppPermission.CAMERA)
                || permissionsManager.requestPermission(AppPermission.CAMERA)
        if (!granted) return null

        return suspendCancellableCoroutine { cont ->
            val picker = UIImagePickerController()
            picker.sourceType =
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera

            cameraDelegate = object : NSObject(),
                UIImagePickerControllerDelegateProtocol,
                UINavigationControllerDelegateProtocol {

                override fun imagePickerController(
                    picker: UIImagePickerController,
                    didFinishPickingMediaWithInfo: Map<Any?, *>,
                ) {
                    val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
                    picker.dismissViewControllerAnimated(true, null)
                    cont.resume(SharedImage(image))
                }

                override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                    picker.dismissViewControllerAnimated(true, null)
                    cont.resume(null)
                }
            }
            picker.delegate = cameraDelegate as UINavigationControllerDelegateProtocol?
            currentViewController()?.presentViewController(picker, true, null)
        }
    }

    private fun currentViewController(): UIViewController? =
        UIApplication.sharedApplication.keyWindow?.rootViewController
}

@Composable
actual fun rememberImagePicker(permissionsManager: PermissionsManager): ImagePicker =
    remember { IosImagePicker(permissionsManager) }