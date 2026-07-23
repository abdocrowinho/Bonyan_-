package org.muslim_voice.project.core.domain.model

import kotlinx.datetime.LocalDate
import org.muslim_voice.project.core.auth.GoogleAccountInfo

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val roleModel: String,
    val favoriteSurah: String,
    val favoriteAyah: String,
    val countryId: String,
    val email: String,
    val password: String? = null,
    val googleAccount: GoogleAccountInfo? = null,
    val profileImageBytes: ByteArray?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as UserProfile
        return firstName == other.firstName &&
            lastName == other.lastName &&
            birthDate == other.birthDate &&
            roleModel == other.roleModel &&
            favoriteSurah == other.favoriteSurah &&
            favoriteAyah == other.favoriteAyah &&
            countryId == other.countryId &&
            email == other.email &&
            password == other.password &&
            googleAccount == other.googleAccount &&
            profileImageBytes.contentEquals(other.profileImageBytes)
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + birthDate.hashCode()
        result = 31 * result + roleModel.hashCode()
        result = 31 * result + favoriteSurah.hashCode()
        result = 31 * result + favoriteAyah.hashCode()
        result = 31 * result + countryId.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (googleAccount?.hashCode() ?: 0)
        result = 31 * result + (profileImageBytes?.contentHashCode() ?: 0)
        return result
    }
}
