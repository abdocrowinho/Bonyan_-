package org.muslim_voice.project.features.notificationsHistory.state

import org.muslim_voice.project.features.notificationsHistory.NotificationTypeUi

enum class NotificationDateGroup { TODAY, YESTERDAY, EARLIER }

data class NotificationItemUi(
    val id: String,
    val type: NotificationTypeUi,
    val title: String,
    val body: String,
    val timeLabel: String,
    val group: NotificationDateGroup,
    val isUnread: Boolean,
)

data class NotificationsHistoryState(
    val items: List<NotificationItemUi> = sampleItems,
) {
    companion object {
        private val sampleItems = listOf(
            NotificationItemUi(
                id = "1",
                type = NotificationTypeUi.PRAYER_REMINDER,
                title = "حان وقت الظهر",
                body = "تذكّر إخوانك في مجموعة المسجد",
                timeLabel = "10:30",
                group = NotificationDateGroup.TODAY,
                isUnread = true,
            ),
            NotificationItemUi(
                id = "2",
                type = NotificationTypeUi.MEMBER_READY,
                title = "خالد جاهز للصلاة",
                body = "سجّل جاهزيته قبل الأذان",
                timeLabel = "10:05",
                group = NotificationDateGroup.TODAY,
                isUnread = true,
            ),
            NotificationItemUi(
                id = "3",
                type = NotificationTypeUi.WALKIE_MESSAGE,
                title = "رسالة لاسلكية",
                body = "أحمد: سبحان الله وبحمده",
                timeLabel = "19:40",
                group = NotificationDateGroup.YESTERDAY,
                isUnread = false,
            ),
            NotificationItemUi(
                id = "4",
                type = NotificationTypeUi.NEW_HADITH,
                title = "حديث اليوم",
                body = "عن عمر بن الخطاب رضي الله عنه",
                timeLabel = "08:00",
                group = NotificationDateGroup.EARLIER,
                isUnread = false,
            ),
        )
    }
}
