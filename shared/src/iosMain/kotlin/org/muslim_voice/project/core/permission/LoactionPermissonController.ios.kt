package org.muslim_voice.project.core.permission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.*
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import kotlin.coroutines.resume

class IosLocationPermissionController : LocationPermissionController {

    private val locationManager = CLLocationManager()

    override suspend fun getStatus(): PermissionStatus {
        return mapStatus(CLLocationManager.authorizationStatus())
    }

    override suspend fun requestPermission(): PermissionStatus =
        suspendCancellableCoroutine { continuation ->
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
                    if (continuation.isActive) {
                        continuation.resume(mapStatus(manager.authorizationStatus))
                    }
                }
            }
            locationManager.delegate = delegate
            locationManager.requestWhenInUseAuthorization()
        }

    override fun openAppSettings() {
        val url = NSURL.URLWithString(platform.UIKit.UIApplicationOpenSettingsURLString)
        if (url != null) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    private fun mapStatus(status: CLAuthorizationStatus): PermissionStatus = when (status) {
        kCLAuthorizationStatusAuthorizedAlways,
        kCLAuthorizationStatusAuthorizedWhenInUse -> PermissionStatus.GRANTED
        kCLAuthorizationStatusDenied -> PermissionStatus.PERMANENTLY_DENIED
        else -> PermissionStatus.DENIED
    }
}

@Composable
actual fun rememberLocationPermissionController(): LocationPermissionController {
    return remember { IosLocationPermissionController() }
}