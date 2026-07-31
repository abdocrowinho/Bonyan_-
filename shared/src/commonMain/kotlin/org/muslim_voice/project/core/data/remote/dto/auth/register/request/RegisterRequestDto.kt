package org.muslim_voice.project.core.data.remote.dto.auth.register.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    @SerialName("first_name")
    val firstName: String? = null,

    @SerialName("last_name")
    val lastName: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("password")
    val password: String? = null,

    @SerialName("fav_ayah")
    val favoriteAyah: String? = null,

    @SerialName("fav_surah")
    val favoriteSurah: String? = null,

    @SerialName("country")
    val country: String? = null,

    @SerialName("birthdate")
    val birthDate: String? = null,

    @SerialName("role_model")
    val roleModel: String? = null,

    @SerialName("profile_picture")
    val profilePicture: String? = null,

    @SerialName("prayer_recordings")
    val recordings: RecordingsDto? = null
)

