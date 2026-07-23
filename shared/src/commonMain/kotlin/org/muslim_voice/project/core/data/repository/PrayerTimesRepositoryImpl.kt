package org.muslim_voice.project.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.muslim_voice.project.core.data.helper.RequestState
import org.muslim_voice.project.core.data.helper.executeApi
import org.muslim_voice.project.core.data.remote.webservice.PrayerTimesWebService
import org.muslim_voice.project.core.domain.model.PrayerTimes
import org.muslim_voice.project.core.domain.repository.PrayerTimesRepository

class PrayerTimesRepositoryImpl(
    private val webService: PrayerTimesWebService,
) : PrayerTimesRepository {

    override fun getPrayerTimes(
        date: String,
        city: String,
        country: String,
    ): Flow<RequestState<PrayerTimes>> {
        return executeApi {
            webService.fetchPrayerTimes(date = date, city = city, country = country)
        }
    }
}
