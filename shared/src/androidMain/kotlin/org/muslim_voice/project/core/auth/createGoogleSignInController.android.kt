package org.muslim_voice.project.core.auth

class AndroidGoogleSignInController : GoogleSignInController {
    override suspend fun signIn(): GoogleAccountInfo? = null

    override suspend fun signOut() = Unit
}

actual fun createGoogleSignInController(): GoogleSignInController = AndroidGoogleSignInController()
