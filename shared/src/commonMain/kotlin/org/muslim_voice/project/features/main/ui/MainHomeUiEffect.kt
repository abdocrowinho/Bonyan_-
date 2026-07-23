package org.muslim_voice.project.features.mainHome.effect

sealed interface MainHomeUiEffect {
    data object NavigateToQibla : MainHomeUiEffect
    data object NavigateToQuran : MainHomeUiEffect
    data object NavigateToGroups : MainHomeUiEffect
    data object NavigateToProfile : MainHomeUiEffect
    data class ShowError(val message: String) : MainHomeUiEffect
}
