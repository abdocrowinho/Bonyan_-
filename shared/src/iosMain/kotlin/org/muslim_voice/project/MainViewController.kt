package org.muslim_voice.project

import androidx.compose.ui.window.ComposeUIViewController
import org.muslim_voice.project.di.iosModule

fun MainViewController() = ComposeUIViewController { App(iosModule) }