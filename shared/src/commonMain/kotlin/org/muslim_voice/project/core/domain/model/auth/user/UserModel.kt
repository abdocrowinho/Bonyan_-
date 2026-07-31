package org.muslim_voice.project.core.domain.model.auth.user

import org.muslim_voice.project.core.domain.model.auth.register.request.RecordingsModel

data class UserModel(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val isVerified: Boolean,
    val profilePicture: String?,
    val country: String?,
    val birthdate: String?,
    val roleModel: String?,
    val favSurah: String?,
    val favAyah: String?,
    val createdAt: String?,
    val prayerRecordings: RecordingsModel? // لو محتاج تحوله لـ Domain Model
) {
    val fullName: String
        get() = "$firstName $lastName".trim()
}
