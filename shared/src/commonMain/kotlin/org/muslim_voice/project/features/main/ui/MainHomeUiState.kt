package org.muslim_voice.project.features.mainHome.state

import org.muslim_voice.project.core.data.helper.AppError
import org.muslim_voice.project.core.domain.model.PrayerTimes
import org.muslim_voice.project.core.mvi.ConnectionAwareState

data class MainHomeUiState(
    val selectedTabIndex: Int = 0,
    val prayerTimes: PrayerTimes? = null,
    val currentPrayerName: String = "",
    val currentTime: String = "",
    val remainingTime: String = "",
    val prayerTimeItems: List<PrayerTimeItem> = emptyList(),
    val hijriDateLabel: String = "",
    val gregorianDateLabel: String = "",
    val isLoading: Boolean = false,
    override val appError: AppError? = null,
    override val isOffline: Boolean = false,
) : ConnectionAwareState

data class PrayerTimeItem(
    val name: String,
    val time: String,
    val status: PrayerStatus,
)

enum class PrayerStatus {
    PASSED, CURRENT, UPCOMING
}
