package org.muslim_voice.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "muslimVoice",
    ) {
        App()
    }
}