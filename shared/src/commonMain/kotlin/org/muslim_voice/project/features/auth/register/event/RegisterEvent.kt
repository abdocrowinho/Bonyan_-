package org.muslim_voice.project.features.auth.register.event

sealed interface RegisterEvent {
    data object RequestMicRecording : RegisterEvent
    data object RequestGalleryPick : RegisterEvent
    data object RequestCameraCapture : RegisterEvent
    data object NavigateToHome : RegisterEvent
    data class ShowError(val message: String) : RegisterEvent
}
