package org.muslim_voice.project.core.domain.model.auth.register.request


data class RegisterRequestModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val favoriteAyah: String? = null,
    val favoriteSurah: String? = null,
    val country: String? = null,
    val birthDate: String? = null,
    val roleModel: String? = null,
    val profilePicture: String? = null,
    val recordings: RecordingsModel? = null
)

