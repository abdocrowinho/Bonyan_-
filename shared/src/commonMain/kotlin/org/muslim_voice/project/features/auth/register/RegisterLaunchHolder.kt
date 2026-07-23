package org.muslim_voice.project.features.auth.register

import org.muslim_voice.project.core.auth.GoogleAccountInfo

class RegisterLaunchHolder {
    private var googleAccount: GoogleAccountInfo? = null

    fun setGoogleAccount(account: GoogleAccountInfo?) {
        googleAccount = account
    }

    fun consumeGoogleAccount(): GoogleAccountInfo? {
        return googleAccount.also { googleAccount = null }
    }
}
