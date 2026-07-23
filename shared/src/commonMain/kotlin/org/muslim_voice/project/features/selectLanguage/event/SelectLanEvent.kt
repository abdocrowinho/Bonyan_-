package org.muslim_voice.project.features.selectLanguage.event

import org.muslim_voice.project.core.mvi.BaseViewModel

sealed interface SelectLanEvent  {
    data object NavigateToRegisterScreen : SelectLanEvent
}