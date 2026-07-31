package org.muslim_voice.project.core.data.remote.dto.user.response

import kotlinx.serialization.Serializable
import org.muslim_voice.project.core.data.remote.dto.auth.register.request.RecordingsDto

@Serializable
data class UserDto(
    val country: String? = null,
    val birthdate: String? = null,
    val roleModel: String? = null,
    val createdAt: String? = null,
    val lastName: String? = null,
    val profilePicture: String? = null,
    val isVerified: Boolean? = null,
    val favSurah: String? = null,
    val prayerRecordings: RecordingsDto? = null,
    val id: Int? = null,
    val favAyah: String? = null,
    val firstName: String? = null,
    val email: String? = null
)