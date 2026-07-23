package org.muslim_voice.project.features.groupDashboard.state

import org.muslim_voice.project.core.ui.components.DayDotState
import org.muslim_voice.project.core.ui.components.StatusRingColor

enum class ReadyStateUi { IDLE, READY, DONE }

data class MemberStatus(
    val id: String,
    val name: String,
    val emoji: String,
    val statusRing: StatusRingColor,
    val hasVoiceNote: Boolean,
    val statusText: String,
)

data class DayStatus(
    val dayLabel: String,
    val state: DayDotState,
)

data class HadithUiModel(
    val quote: String,
    val source: String,
)

data class GroupDashboardState(
    val groupName: String = "مجموعة المسجد",
    val activeMembersCount: Int = 8,
    val prayerName: String = "الظهر",
    val prayerTime: String = "12:15",
    val countdownHours: Int = 0,
    val countdownMinutes: Int = 42,
    val countdownSeconds: Int = 18,
    val readyState: ReadyStateUi = ReadyStateUi.IDLE,
    val members: List<MemberStatus> = sampleMembers,
    val weekHistory: List<DayStatus> = sampleWeek,
    val hadith: HadithUiModel = HadithUiModel(
        quote = "«إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ»",
        source = "رواه البخاري ومسلم",
    ),
    val isWalkiePressed: Boolean = false,
) {
    companion object {
        private val sampleMembers = listOf(
            MemberStatus("1", "أحمد", "🧔", StatusRingColor.GREEN, true, "صلّى"),
            MemberStatus("2", "خالد", "👨", StatusRingColor.YELLOW, false, "جاهز"),
            MemberStatus("3", "يوسف", "🧑", StatusRingColor.RED, false, "في الانتظار"),
            MemberStatus("4", "عمر", "👳", StatusRingColor.GREEN, true, "صلّى"),
            MemberStatus("5", "سعد", "🙂", StatusRingColor.YELLOW, true, "جاهز"),
            MemberStatus("6", "فهد", "😊", StatusRingColor.GREEN, false, "صلّى"),
        )
        private val sampleWeek = listOf(
            DayStatus("س", DayDotState.DONE),
            DayStatus("ح", DayDotState.DONE),
            DayStatus("ن", DayDotState.MISS),
            DayStatus("ث", DayDotState.DONE),
            DayStatus("ر", DayDotState.TODAY),
            DayStatus("خ", DayDotState.FUTURE),
            DayStatus("ج", DayDotState.FUTURE),
        )
    }
}
