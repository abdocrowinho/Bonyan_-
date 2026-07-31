package org.muslim_voice.project.features.auth.register.state

import kotlinx.datetime.LocalDate
import org.muslim_voice.project.core.auth.GoogleAccountInfo
import org.muslim_voice.project.core.component.DropdownOption
import org.muslim_voice.project.core.constants.PrayerType
import org.muslim_voice.project.features.auth.register.ui_Model.RegisterStep

data class RegisterFormData(
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: LocalDate? = null,
    val roleModel: String = "",
    val favoriteSurah: String = "",
    val favoriteAyah: String = "",
    val selectedCountry: DropdownOption? = null,
    val email: String = "",
    val password: String = "",
    val recordings: Map<PrayerType, String> = emptyMap(),
    val profileImageBytes: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RegisterFormData) return false

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (birthDate != other.birthDate) return false
        if (roleModel != other.roleModel) return false
        if (favoriteSurah != other.favoriteSurah) return false
        if (favoriteAyah != other.favoriteAyah) return false
        if (selectedCountry != other.selectedCountry) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (recordings != other.recordings) return false

        if (profileImageBytes != null) {
            if (other.profileImageBytes == null) return false
            if (!profileImageBytes.contentEquals(other.profileImageBytes)) return false
        } else if (other.profileImageBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + (birthDate?.hashCode() ?: 0)
        result = 31 * result + roleModel.hashCode()
        result = 31 * result + favoriteSurah.hashCode()
        result = 31 * result + favoriteAyah.hashCode()
        result = 31 * result + (selectedCountry?.hashCode() ?: 0)
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + recordings.hashCode()
        result = 31 * result + (profileImageBytes?.contentHashCode() ?: 0)
        return result
    }
}

data class RegisterState(
    val registerInfo: RegisterFormData = RegisterFormData(),
    val currentStep: RegisterStep = RegisterStep.PERSONAL_INFO,
    val isLoading: Boolean = false,
    val countryOptions: List<DropdownOption> = emptyList(),
    val step1Errors: Map<String, String> = emptyMap(),
    val googleAccount: GoogleAccountInfo? = null,
    val currentlyRecording: PrayerType? = null,
    val currentlyPlaying: PrayerType? = null,
    val verificationCode: String = "",
    val verificationError: String? = null,
    val isResendingCode: Boolean = false,
)