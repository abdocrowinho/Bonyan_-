package org.muslim_voice.project.core.data.repository

import org.muslim_voice.project.core.auth.GoogleAccountInfo
import org.muslim_voice.project.core.domain.model.UserProfile
import org.muslim_voice.project.core.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    private val registeredEmails = mutableSetOf<String>()
    private val completedProfiles = mutableSetOf<String>()
    private val pendingCodes = mutableMapOf<String, String>()

    override suspend fun login(email: String, password: String): Result<String> {
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("كلمة المرور مطلوبة"))
        }
        registeredEmails.add(email.lowercase())
        return Result.success("session_${email.lowercase()}")
    }

    override suspend fun hasCompletedProfile(email: String): Boolean {
        return completedProfiles.contains(email.lowercase())
    }

    override suspend fun hasCompletedProfile(googleAccount: GoogleAccountInfo): Boolean {
        return completedProfiles.contains(googleAccount.email.lowercase())
    }

    override suspend fun sendVerificationCode(email: String): Result<Unit> {
        pendingCodes[email.lowercase()] = DEFAULT_VERIFICATION_CODE
        return Result.success(Unit)
    }

    override suspend fun verifyCode(email: String, code: String): Result<Unit> {
        val expected = pendingCodes[email.lowercase()]
        return if (expected != null && expected == code.trim()) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("رمز التحقق غير صحيح"))
        }
    }

    override suspend fun submitProfile(profile: UserProfile): Result<String> {
        val email = profile.email.lowercase()
        registeredEmails.add(email)
        completedProfiles.add(email)
        pendingCodes.remove(email)
        return Result.success("session_$email")
    }

    private companion object {
        const val DEFAULT_VERIFICATION_CODE = "123456"
    }
}
