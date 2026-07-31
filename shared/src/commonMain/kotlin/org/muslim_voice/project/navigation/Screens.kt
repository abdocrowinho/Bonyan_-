package org.muslim_voice.project.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens {
    @Serializable
    data object Splash : Screens()

    @Serializable
    data object Onboarding : Screens()

    @Serializable
    data object Login : Screens()

    @Serializable
    data object Register : Screens()

    @Serializable
    data class Otp(val email: String) : Screens()

    @Serializable
    data object SelectLan : Screens()

    @Serializable
    data object LocationPermissionScreen : Screens()
    @Serializable
    data object MainHomeScreen : Screens()

    @Serializable
    data object GroupDashboard : Screens()

    @Serializable
    data object WalkieTalkie : Screens()

    @Serializable
    data object Library : Screens()

    @Serializable
    data object Stories : Screens()

    @Serializable
    data object NotificationsHistory : Screens()

    @Serializable
    data object Qibla : Screens()

    @Serializable
    data object Quran : Screens()

    @Serializable
    data object Groups : Screens()

    @Serializable
    data object Profile : Screens()
}
