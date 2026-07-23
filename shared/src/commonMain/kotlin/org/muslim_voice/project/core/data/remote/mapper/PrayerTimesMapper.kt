package org.muslim_voice.project.core.data.remote.mapper

import org.muslim_voice.project.core.data.remote.dto.PrayerTimesResponseDto
import org.muslim_voice.project.core.domain.model.PrayerTimings
import org.muslim_voice.project.core.domain.model.PrayerTimes

fun PrayerTimesResponseDto.toDomain(): PrayerTimes {
    val hijri = data.date.hijri
    val hijriDay = hijri.date.substringBefore("-")
    return PrayerTimes(
        timings = PrayerTimings(
            fajr = data.timings.Fajr,
            sunrise = data.timings.Sunrise,
            dhuhr = data.timings.Dhuhr,
            asr = data.timings.Asr,
            sunset = data.timings.Sunset,
            maghrib = data.timings.Maghrib,
            isha = data.timings.Isha,
        ),
        gregorianDate = data.date.readable,
        hijriDay = hijriDay,
        hijriMonthAr = hijri.month.ar,
        hijriYear = hijri.year,
        hijriWeekdayAr = hijri.weekday.ar,
    )
}
