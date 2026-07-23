package org.muslim_voice.project.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.muslim_voice.project.core.data.helper.RequestState
import org.muslim_voice.project.core.domain.model.PrayerTimes

interface PrayerTimesRepository {
    fun getPrayerTimes(
        date: String,
        city: String,
        country: String,
    ): Flow<RequestState<PrayerTimes>>
}
