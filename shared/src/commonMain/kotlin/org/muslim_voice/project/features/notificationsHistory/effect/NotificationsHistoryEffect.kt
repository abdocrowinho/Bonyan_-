package org.muslim_voice.project.features.notificationsHistory.effect

sealed interface NotificationsHistoryEffect {
    data object NavigateBack : NotificationsHistoryEffect
}
