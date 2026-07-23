package org.muslim_voice.project.features.auth.register.state

import kotlinx.datetime.LocalDate
import org.muslim_voice.project.core.auth.GoogleAccountInfo
import org.muslim_voice.project.core.component.DropdownOption
import org.muslim_voice.project.features.auth.register.ui_Model.PrayType
import org.muslim_voice.project.features.auth.register.ui_Model.RegisterStep

data class RegisterState(
    val currentStep: RegisterStep = RegisterStep.PERSONAL_INFO,
    val isLoading: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: LocalDate? = null,
    val roleModel: String = "",
    val favoriteSurah: String = "",
    val favoriteAyah: String = "",
    val selectedCountry: DropdownOption? = null,
    val countryOptions: List<DropdownOption> = emptyList(),
    val step1Errors: Map<String, String> = emptyMap(),
    val googleAccount: GoogleAccountInfo? = null,
    val recordings: Map<PrayType, ByteArray> = emptyMap(),
    val currentlyRecording: PrayType? = null,
    val currentlyPlaying: PrayType? = null,
    val profileImageBytes: ByteArray? = null,
    val verificationCode: String = "",
    val verificationError: String? = null,
    val isResendingCode: Boolean = false,
    val email: String = "",
    val password: String = "",
)
