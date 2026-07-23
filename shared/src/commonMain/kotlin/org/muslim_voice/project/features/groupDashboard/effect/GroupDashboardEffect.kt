package org.muslim_voice.project.features.groupDashboard.effect

sealed interface GroupDashboardEffect {
    data object NavigateToNotifications : GroupDashboardEffect
}
