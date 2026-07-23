package org.muslim_voice.project.features.main.ui

sealed interface MainHomeIntent {
    data class LoadPrayerTimes(
        val city: String = DEFAULT_CITY,
        val country: String = DEFAULT_COUNTRY,
    ) : MainHomeIntent

    data class RefreshPrayerTimes(
        val city: String = DEFAULT_CITY,
        val country: String = DEFAULT_COUNTRY,
    ) : MainHomeIntent

    data object NavigateToQibla : MainHomeIntent
    data class SelectTab(val index: Int) : MainHomeIntent

    companion object {
        const val DEFAULT_CITY = "Cairo"
        const val DEFAULT_COUNTRY = "Egypt"
    }
}
