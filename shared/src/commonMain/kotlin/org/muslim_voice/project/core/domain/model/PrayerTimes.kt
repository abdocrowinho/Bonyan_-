package org.muslim_voice.project.core.domain.model

data class PrayerTimes(
    val timings: PrayerTimings,
    val gregorianDate: String,
    val hijriDay: String,
    val hijriMonthAr: String,
    val hijriYear: String,
    val hijriWeekdayAr: String,
)

data class PrayerTimings(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val sunset: String,
    val maghrib: String,
    val isha: String,
)
