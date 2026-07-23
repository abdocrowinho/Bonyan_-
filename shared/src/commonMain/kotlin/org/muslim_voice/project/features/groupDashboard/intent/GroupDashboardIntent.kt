package org.muslim_voice.project.features.groupDashboard.intent

sealed interface GroupDashboardIntent {
    data object ToggleReady : GroupDashboardIntent
    data object OpenNotifications : GroupDashboardIntent
    data class SetWalkiePressed(val pressed: Boolean) : GroupDashboardIntent
}
