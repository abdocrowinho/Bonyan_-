package org.muslim_voice.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.muslim_voice.project.core.platform.AndroidContextHolder
import org.muslim_voice.project.core.utiltis.ProvideAppScale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        AndroidContextHolder.context = applicationContext
        setContent {
            ProvideAppScale{
                App()
            }
        }
    }
}
