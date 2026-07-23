package org.muslim_voice.project.features.selectLanguage.viewModel

import org.muslim_voice.project.core.mvi.BaseViewModel
import org.muslim_voice.project.features.selectLanguage.event.SelectLanEvent
import org.muslim_voice.project.features.selectLanguage.intent.SelectLanIntent
import org.muslim_voice.project.features.selectLanguage.state.SelectLanState

class SelectLanViewModel : BaseViewModel<SelectLanState, SelectLanIntent, SelectLanEvent>(
    initialState = SelectLanState(
        isLoading = false,
        isSuccess = false
    )
) {


    override fun handleIntent(intent: SelectLanIntent) {
        when (intent) {
            SelectLanIntent.OnFollowClicked -> TODO()
            is SelectLanIntent.OnLanSelect -> {
                val current = state.value
                setState(
                    current.copy(
                        selectedLan = intent.lan.lanInLanSyntax,
                        languages = current.languages.map { selectedItem ->
                            selectedItem.copy(
                                isSelected = selectedItem.lanInLanSyntax == intent.lan.lanInLanSyntax
                            )
                        }
                    ),
                )

            }
        }
    }
}