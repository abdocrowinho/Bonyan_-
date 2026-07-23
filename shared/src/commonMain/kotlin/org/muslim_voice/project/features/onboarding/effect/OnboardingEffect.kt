package org.muslim_voice.project.features.onboarding.effect

sealed interface OnboardingEffect {
    data object NavigateToLogin : OnboardingEffect
}
