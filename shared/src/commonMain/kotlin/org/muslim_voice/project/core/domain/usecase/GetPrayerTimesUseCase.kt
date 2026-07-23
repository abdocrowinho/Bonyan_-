package org.muslim_voice.project.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.muslim_voice.project.core.data.helper.RequestState
import org.muslim_voice.project.core.domain.model.PrayerTimes
import org.muslim_voice.project.core.domain.repository.PrayerTimesRepository
import org.muslim_voice.project.core.utilities.DateTimeFormatter

class GetPrayerTimesUseCase(
    private val repository: PrayerTimesRepository,
) {
    operator fun invoke(
        city: String,
        country: String,
        date: String = DateTimeFormatter.todayApiDate(),
    ): Flow<RequestState<PrayerTimes>> = repository.getPrayerTimes(
        date = date,
        city = city,
        country = country,
    )
}
