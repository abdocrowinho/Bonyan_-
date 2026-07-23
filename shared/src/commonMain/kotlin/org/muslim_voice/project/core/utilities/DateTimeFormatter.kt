package org.muslim_voice.project.core.utilities

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateTimeFormatter {

    fun todayApiDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
        return formatToApiDate(Clock.System.now().toLocalDateTime(timeZone).date)
    }

    fun formatToApiDate(date: LocalDate): String {
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.monthNumber.toString().padStart(2, '0')
        val year = date.year.toString()
        return "$day-$month-$year"
    }

    fun parseApiDate(apiDate: String): LocalDate? {
        val parts = apiDate.split("-")
        if (parts.size != 3) return null
        return runCatching {
            LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
        }.getOrNull()
    }

    fun parsePrayerTime(time24h: String): LocalTime? {
        val cleaned = time24h.take(5)
        val parts = cleaned.split(":")
        if (parts.size != 2) return null
        return runCatching {
            LocalTime(parts[0].toInt(), parts[1].toInt())
        }.getOrNull()
    }

    fun formatTo12Hour(time24h: String): String {
        val time = parsePrayerTime(time24h) ?: return time24h
        val hour = time.hour
        val minute = time.minute.toString().padStart(2, '0')
        val period = if (hour < 12) "AM" else "PM"
        val hour12 = when (val mod = hour % 12) {
            0 -> 12
            else -> mod
        }
        return "$hour12:$minute $period"
    }

    fun nowLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
        return Clock.System.now().toLocalDateTime(timeZone)
    }

    fun minutesUntil(target: LocalTime, from: LocalTime): Int {
        val fromMinutes = from.hour * 60 + from.minute
        val targetMinutes = target.hour * 60 + target.minute
        return if (targetMinutes >= fromMinutes) {
            targetMinutes - fromMinutes
        } else {
            (24 * 60 - fromMinutes) + targetMinutes
        }
    }

    fun formatRemainingMinutes(minutes: Int): String {
        val hours = minutes / 60
        val mins = minutes % 60
        return if (hours > 0) {
            "${toArabicNumeral(hours)}:${toArabicNumeral(mins).padStart(2, '0')} متبقي"
        } else {
            "${toArabicNumeral(mins)} دقيقة متبقية"
        }
    }

    fun formatHijriLabel(day: String, monthAr: String, year: String, weekdayAr: String): String {
        return "$weekdayAr، $day $monthAr $year هـ"
    }

    private fun toArabicNumeral(number: Int): String {
        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return number.toString().map { arabicDigits[it.digitToInt()] }.joinToString("")
    }
}
