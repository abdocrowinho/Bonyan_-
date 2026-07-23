package org.muslim_voice.project.features.notificationsHistory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.muslim_voice.project.core.ui.theme.AppColors

enum class NotificationTypeUi {
    PRAYER_REMINDER,
    MEMBER_READY,
    MEMBER_DONE,
    NEW_HADITH,
    WALKIE_MESSAGE,
    SYSTEM;

    fun color(): Color = when (this) {
        PRAYER_REMINDER -> AppColors.Primary
        MEMBER_READY -> AppColors.StatusYellow
        MEMBER_DONE -> AppColors.StatusGreen
        NEW_HADITH -> AppColors.Accent
        WALKIE_MESSAGE -> AppColors.PrimaryDark
        SYSTEM -> AppColors.Subtle
    }

    fun icon(): ImageVector = when (this) {
        PRAYER_REMINDER -> Icons.Default.Schedule
        MEMBER_READY -> Icons.Default.CheckCircle
        MEMBER_DONE -> Icons.Default.CheckCircle
        NEW_HADITH -> Icons.Default.MenuBook
        WALKIE_MESSAGE -> Icons.Default.Mic
        SYSTEM -> Icons.Default.Campaign
    }
}
