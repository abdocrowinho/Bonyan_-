package org.muslim_voice.project.core.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class UserPreferencesRepository(
    private val settings: Settings = Settings(),
) {
    fun isOnboardingDone(): Boolean = settings.getBoolean(KEY_ONBOARDING_DONE, false)

    fun getSessionToken(): String? = settings.getStringOrNull(KEY_SESSION_TOKEN)

    fun setOnboardingDone(done: Boolean) {
        settings[KEY_ONBOARDING_DONE] = done
    }

    fun setSessionToken(token: String?) {
        if (token == null) {
            settings.remove(KEY_SESSION_TOKEN)
        } else {
            settings[KEY_SESSION_TOKEN] = token
        }
    }

    private companion object {
        const val KEY_ONBOARDING_DONE = "onboarding_done"
        const val KEY_SESSION_TOKEN = "session_token"
    }
}
