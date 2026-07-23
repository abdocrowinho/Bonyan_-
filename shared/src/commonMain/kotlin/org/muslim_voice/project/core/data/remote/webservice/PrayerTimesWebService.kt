package org.muslim_voice.project.core.data.remote.webservice

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.muslim_voice.project.core.data.remote.dto.PrayerTimesResponseDto
import org.muslim_voice.project.core.data.remote.mapper.toDomain
import org.muslim_voice.project.core.domain.model.PrayerTimes

class PrayerTimesWebService(
    private val httpClient: HttpClient,
) {
    suspend fun fetchPrayerTimes(
        date: String,
        city: String,
        country: String,
    ): PrayerTimes {
        val url = buildString {
            append(BASE_URL)
            append(date)
            append("?city=")
            append(city)
            append("&country=")
            append(country)
            append("&method=5")
        }
        val response: PrayerTimesResponseDto = httpClient.get(url).body()
        return response.toDomain()
    }

    companion object {
        private const val BASE_URL = "https://api.aladhan.com/v1/timingsByCity/"
    }
}
