package org.muslim_voice.project.features.onboarding.state

import org.jetbrains.compose.resources.StringResource
import org.muslim_voice.project.generated.resources.*

data class OnboardingPage(
    val title: StringResource,
    val subtitle: StringResource,
    val accentIsGold: Boolean,
    val illustrationType: OnboardingIllustrationType,
)

enum class OnboardingIllustrationType {
    MOSQUE, GROUP, WALKIE, BOOK,
}

data class OnboardingState(
    val currentPage: Int = 0,
    val pages: List<OnboardingPage> = defaultPages,
) {
    companion object {
        val defaultPages = listOf(
            OnboardingPage(Res.string.onboarding_page1_title, Res.string.onboarding_page1_subtitle, false, OnboardingIllustrationType.MOSQUE),
            OnboardingPage(Res.string.onboarding_page2_title, Res.string.onboarding_page2_subtitle, true, OnboardingIllustrationType.GROUP),
            OnboardingPage(Res.string.onboarding_page3_title, Res.string.onboarding_page3_subtitle, false, OnboardingIllustrationType.WALKIE),
            OnboardingPage(Res.string.onboarding_page4_title, Res.string.onboarding_page4_subtitle, true, OnboardingIllustrationType.BOOK),
        )
    }
}
