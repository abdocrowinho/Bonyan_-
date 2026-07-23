package org.muslim_voice.project.core.domain.repository

import org.muslim_voice.project.core.auth.GoogleAccountInfo
import org.muslim_voice.project.core.domain.model.UserProfile

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun hasCompletedProfile(email: String): Boolean
    suspend fun hasCompletedProfile(googleAccount: GoogleAccountInfo): Boolean
    suspend fun sendVerificationCode(email: String): Result<Unit>
    suspend fun verifyCode(email: String, code: String): Result<Unit>
    suspend fun submitProfile(profile: UserProfile): Result<String>
}
