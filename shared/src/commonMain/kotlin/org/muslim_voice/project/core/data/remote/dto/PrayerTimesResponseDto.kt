package org.muslim_voice.project.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PrayerTimesResponseDto(
    val code: Int,
    val status: String,
    val data: PrayerTimesDataDto,
)

@Serializable
data class PrayerTimesDataDto(
    val timings: TimingsDto,
    val date: DateDto,
)

@Serializable
data class TimingsDto(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Sunset: String,
    val Maghrib: String,
    val Isha: String,
)

@Serializable
data class DateDto(
    val readable: String,
    val gregorian: GregorianDto,
    val hijri: HijriDto,
)

@Serializable
data class GregorianDto(
    val date: String,
)

@Serializable
data class HijriDto(
    val date: String,
    val weekday: WeekdayDto,
    val month: MonthDto,
    val year: String,
)

@Serializable
data class WeekdayDto(
    val ar: String,
)

@Serializable
data class MonthDto(
    val ar: String,
)
