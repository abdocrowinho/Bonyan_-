package org.muslim_voice.project.features.notificationsHistory.intent

sealed interface NotificationsHistoryIntent {
    data class Dismiss(val id: String) : NotificationsHistoryIntent
}
