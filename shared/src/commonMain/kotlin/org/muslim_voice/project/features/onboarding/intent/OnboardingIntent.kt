package org.muslim_voice.project.features.onboarding.intent

sealed interface OnboardingIntent {
    data object Next : OnboardingIntent
    data object Back : OnboardingIntent
    data object Skip : OnboardingIntent
    data object Finish : OnboardingIntent
    data class SetPage(val page: Int) : OnboardingIntent
}
