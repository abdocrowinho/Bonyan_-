package org.muslim_voice.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.muslim_voice.project.core.microphone.AndroidMicrophonePermissionRequester
import org.muslim_voice.project.core.permission.AndroidPermissionsManager
import org.muslim_voice.project.core.utilities.ProvideAppScale
import org.muslim_voice.project.di.androidModule

// androidApp — MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val permissionRequester = AndroidMicrophonePermissionRequester(this)
        val permissionsManager = AndroidPermissionsManager(this)

        setContent {
            ProvideAppScale {
                App(platformModule = androidModule(applicationContext, permissionRequester,permissionsManager))
            }
        }
    }
}