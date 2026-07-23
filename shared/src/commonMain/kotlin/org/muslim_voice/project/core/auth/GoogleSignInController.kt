package org.muslim_voice.project.core.auth

import androidx.compose.runtime.Composable

data class GoogleAccountInfo(
    val idToken: String,
    val email: String,
    val displayName: String?,
)

interface GoogleSignInController {
    suspend fun signIn(): GoogleAccountInfo?
    suspend fun signOut()
}

@Composable
expect fun rememberGoogleSignInController(): GoogleSignInController
