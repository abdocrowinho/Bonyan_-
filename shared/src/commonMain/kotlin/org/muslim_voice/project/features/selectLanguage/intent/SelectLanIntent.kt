package org.muslim_voice.project.features.selectLanguage.intent

import org.muslim_voice.project.features.selectLanguage.ui_Model.LanItem

sealed interface SelectLanIntent{
    data class OnLanSelect(val lan : LanItem) : SelectLanIntent
    data object OnFollowClicked : SelectLanIntent

}