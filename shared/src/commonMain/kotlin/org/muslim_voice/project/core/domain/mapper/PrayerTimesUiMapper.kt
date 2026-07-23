package org.muslim_voice.project.core.domain.mapper

import org.muslim_voice.project.core.domain.model.PrayerTimes
import org.muslim_voice.project.core.utilities.DateTimeFormatter
import org.muslim_voice.project.features.mainHome.state.PrayerStatus
import org.muslim_voice.project.features.mainHome.state.PrayerTimeItem

data class PrayerTimesUiData(
    val items: List<PrayerTimeItem>,
    val currentPrayerName: String,
    val currentTime: String,
    val remainingTime: String,
    val hijriDateLabel: String,
    val gregorianDateLabel: String,
)

object PrayerTimesUiMapper {

    private val prayerOrder = listOf(
        "الفجر" to { pt: PrayerTimes -> pt.timings.fajr },
        "الشروق" to { pt: PrayerTimes -> pt.timings.sunrise },
        "الظهر" to { pt: PrayerTimes -> pt.timings.dhuhr },
        "العصر" to { pt: PrayerTimes -> pt.timings.asr },
        "المغرب" to { pt: PrayerTimes -> pt.timings.maghrib },
        "العشاء" to { pt: PrayerTimes -> pt.timings.isha },
    )

    fun map(prayerTimes: PrayerTimes): PrayerTimesUiData {
        val now = DateTimeFormatter.nowLocalDateTime().time
        val currentIndex = findCurrentPrayerIndex(prayerTimes, now)

        val items = prayerOrder.mapIndexed { index, (name, timeProvider) ->
            val rawTime = timeProvider(prayerTimes)
            val status = when {
                currentIndex == -1 -> PrayerStatus.UPCOMING
                index < currentIndex -> PrayerStatus.PASSED
                index == currentIndex -> PrayerStatus.CURRENT
                else -> PrayerStatus.UPCOMING
            }
            PrayerTimeItem(
                name = name,
                time = DateTimeFormatter.formatTo12Hour(rawTime),
                status = status,
            )
        }

        val currentItem = if (currentIndex == -1) {
            items.firstOrNull()
        } else {
            items.getOrNull(currentIndex)
        }
        val nextIndex = when {
            currentIndex == -1 -> 0
            currentIndex >= prayerOrder.lastIndex -> 0
            else -> currentIndex + 1
        }
        val nextRawTime = prayerOrder[nextIndex].second(prayerTimes)
        val nextTime = DateTimeFormatter.parsePrayerTime(nextRawTime)

        val remainingTime = if (nextTime != null) {
            DateTimeFormatter.formatRemainingMinutes(
                DateTimeFormatter.minutesUntil(nextTime, now),
            )
        } else {
            ""
        }

        return PrayerTimesUiData(
            items = items,
            currentPrayerName = currentItem?.name.orEmpty(),
            currentTime = currentItem?.time.orEmpty(),
            remainingTime = remainingTime,
            hijriDateLabel = DateTimeFormatter.formatHijriLabel(
                day = prayerTimes.hijriDay,
                monthAr = prayerTimes.hijriMonthAr,
                year = prayerTimes.hijriYear,
                weekdayAr = prayerTimes.hijriWeekdayAr,
            ),
            gregorianDateLabel = prayerTimes.gregorianDate,
        )
    }

    private fun findCurrentPrayerIndex(prayerTimes: PrayerTimes, now: kotlinx.datetime.LocalTime): Int {
        val times = prayerOrder.map { it.second(prayerTimes) }
            .mapNotNull { DateTimeFormatter.parsePrayerTime(it) }

        if (times.isEmpty()) return -1

        val nowMinutes = now.hour * 60 + now.minute
        val firstMinutes = times.first().hour * 60 + times.first().minute
        if (nowMinutes < firstMinutes) return -1

        var currentIndex = -1
        for (index in times.indices) {
            val prayerMinutes = times[index].hour * 60 + times[index].minute
            if (nowMinutes >= prayerMinutes) {
                currentIndex = index
            }
        }
        return currentIndex
    }
}
