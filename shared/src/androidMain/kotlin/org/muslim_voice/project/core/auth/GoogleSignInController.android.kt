package org.muslim_voice.project.core.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberGoogleSignInController(): GoogleSignInController {
    return remember { createGoogleSignInController() }
}
