package org.muslim_voice.project.features.selectLanguage.state

import org.muslim_voice.project.features.selectLanguage.ui_Model.LanItem

data class SelectLanState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false ,
    val selectedLan : String ?= null,
    val languages: List<LanItem> = listOf(
        LanItem(
            lanIcon = "\uD83C\uDDF8\uD83C\uDDE6" ,
            lan = "العربية",
            lanInLanSyntax = "Arabic"
        ),  LanItem(
            lanIcon = "\uD83C\uDDFA\uD83C\uDDF8" ,
            lan = "English",
            lanInLanSyntax = "English"
        ),  LanItem(
            lanIcon = "\uD83C\uDDEB\uD83C\uDDF7" ,
            lan = "Français",
            lanInLanSyntax = "French"
        ),  LanItem(
            lanIcon = "\uD83C\uDDE9\uD83C\uDDEA" ,
            lan = "Germany",
            lanInLanSyntax = "Deutschland"
        ),

    )
)
