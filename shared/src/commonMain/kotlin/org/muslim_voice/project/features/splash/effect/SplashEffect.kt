package org.muslim_voice.project.features.splash.effect

sealed interface SplashEffect {
    data object NavigateToOnboarding : SplashEffect
    data object NavigateToMain : SplashEffect
}
