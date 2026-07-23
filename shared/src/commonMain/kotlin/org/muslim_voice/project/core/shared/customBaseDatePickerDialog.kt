//package com.dodeal.features.shared
//
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.RowScope
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Icon
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.KeyboardArrowLeft
//import androidx.compose.material.icons.filled.KeyboardArrowRight
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.window.Dialog
//import androidx.compose.ui.window.DialogProperties
//import com.dodeal.features.main.composables.BlurredBackground
//import com.dodeal.theme.AppTheme
//import org.muslim_voice.project.core.utiltis.sdp
//import org.muslim_voice.project.core.utiltis.ssp
//import com.dodeal.utiltis.themeCardSurface
//import java.time.DayOfWeek
//import java.time.LocalDate
//import java.time.YearMonth
//import java.time.format.DateTimeFormatter
//
//@Composable
//fun CustomBaseDatePickerDialog(
//    initialDate: LocalDate,
//    onDismissRequest: () -> Unit,
//    onDateSelected: (LocalDate) -> Unit
//) {
//    var displayedMonth by remember(initialDate) { mutableStateOf(YearMonth.from(initialDate)) }
//    var selectedDate by remember { mutableStateOf(initialDate) }
//
//    val daysOfWeek = remember {
//        listOf(
//            DayOfWeek.SUNDAY,
//            DayOfWeek.MONDAY,
//            DayOfWeek.TUESDAY,
//            DayOfWeek.WEDNESDAY,
//            DayOfWeek.THURSDAY,
//            DayOfWeek.FRIDAY,
//            DayOfWeek.SATURDAY
//        )
//    }
//
//    val firstDayOffset = displayedMonth.atDay(1).dayOfWeek.value % 7
//    val days = buildList<LocalDate?> {
//        repeat(firstDayOffset) { add(null) }
//        repeat(displayedMonth.lengthOfMonth()) { index ->
//            add(displayedMonth.atDay(index + 1))
//        }
//        while (size % 7 != 0) {
//            add(null)
//        }
//    }.chunked(7)
//
//    Dialog(
//        onDismissRequest = onDismissRequest,
//        properties = DialogProperties(
//            dismissOnBackPress = true,
//            dismissOnClickOutside = true,
//            usePlatformDefaultWidth = false
//        )
//    ) {
//        Box(
//            modifier = Modifier.clip(RoundedCornerShape(22.sdp))
//                .clickable(
//                    interactionSource = remember { MutableInteractionSource() },
//                    indication = null
//                ) { onDismissRequest() },
//            contentAlignment = Alignment.Center
//        ) {
//            BlurredBackground(backgroundColor = AppTheme.colors.backgroundColor.copy(alpha = 0.5f),
//                modifier = Modifier.matchParentSize()
//                )
//
//            Column(
//                modifier = Modifier
//                    .width(360.sdp).themeCardSurface()
//
//                    .clickable(
//                        interactionSource = remember { MutableInteractionSource() },
//                        indication = null
//                    ) {  }
//                    .padding(18.sdp),
//                verticalArrangement = Arrangement.spacedBy(14.sdp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(32.sdp)
//                            .clip(CircleShape)
//                            .clickable { displayedMonth = displayedMonth.minusMonths(1) },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowLeft,
//                            contentDescription = "Previous Month",
//                            tint = AppTheme.colors.AccentGreen,
//                            modifier = Modifier.size(24.sdp)
//                        )
//                    }
//
//                    Text(
//                        text = displayedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
//                        color = AppTheme.colors.TextMain,
//                        fontSize = 18.ssp,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Box(
//                        modifier = Modifier
//                            .size(32.sdp)
//                            .clip(CircleShape)
//                            .clickable { displayedMonth = displayedMonth.plusMonths(1) },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowRight,
//                            contentDescription = "Next Month",
//                            tint = AppTheme.colors.AccentGreen,
//                            modifier = Modifier.size(24.sdp)
//                        )
//                    }
//                }
//
//                HorizontalDivider(color = AppTheme.colors.BorderDefault)
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(6.sdp)
//                ) {
//                    daysOfWeek.forEach { day ->
//                        Text(
//                            text = day.displayName,
//                            color = AppTheme.colors.TextSecondary,
//                            fontSize = 11.ssp,
//                            fontWeight = FontWeight.SemiBold,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.weight(1f)
//                        )
//                    }
//                }
//
//                Column(verticalArrangement = Arrangement.spacedBy(6.sdp)) {
//                    days.forEach { week ->
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.spacedBy(6.sdp)
//                        ) {
//                            week.forEach { date ->
//                                DateCell(
//                                    date = date,
//                                    selected = date == selectedDate,
//                                    today = date == LocalDate.now(),
//                                    onClick = {
//                                        if (date != null) {
//                                            selectedDate = date
//                                        }
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//
//                HorizontalDivider(color = AppTheme.colors.BorderDefault)
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = selectedDate.formatOfferDate(),
//                        color = AppTheme.colors.TextSecondary,
//                        fontSize = 13.ssp
//                    )
//
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(10.sdp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .clip(RoundedCornerShape(50.sdp))
//                                .background(Color.Transparent)
//                                .border(1.sdp, AppTheme.colors.BorderHover, RoundedCornerShape(50.sdp))
//                                .clickable { onDismissRequest() }
//                                .padding(horizontal = 24.sdp, vertical = 11.sdp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = "Cancel",
//                                color = AppTheme.colors.AccentGreen,
//                                fontSize = 14.ssp,
//                                fontWeight = FontWeight.Medium
//                            )
//                        }
//
//                        Box(
//                            modifier = Modifier
//                                .clip(RoundedCornerShape(50.sdp))
//                                .background(AppTheme.colors.AccentGreen)
//                                .clickable {
//                                    onDateSelected(selectedDate)
//                                    onDismissRequest()
//                                }
//                                .padding(horizontal = 34.sdp, vertical = 12.sdp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = "Done",
//                                color = AppTheme.colors.TextMain,
//                                fontSize = 15.ssp,
//                                fontWeight = FontWeight.SemiBold
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//val DayOfWeek.displayName: String
//    get() = when (this) {
//        DayOfWeek.SUNDAY -> "Sun"
//        DayOfWeek.MONDAY -> "Mon"
//        DayOfWeek.TUESDAY -> "Tue"
//        DayOfWeek.WEDNESDAY -> "Wed"
//        DayOfWeek.THURSDAY -> "Thu"
//        DayOfWeek.FRIDAY -> "Fri"
//        DayOfWeek.SATURDAY -> "Sat"
//    }
//@Composable
//private fun RowScope.DateCell(
//    date: LocalDate?,
//    selected: Boolean,
//    today: Boolean,
//    onClick: () -> Unit
//) {
//    val background by animateColorAsState(
//        targetValue = when {
//            selected -> AppTheme.colors.AccentGreen
//            today -> AppTheme.colors.AccentGreenSoft
//            else -> AppTheme.colors.lightBlack.copy(alpha = 0.35f)
//        }
//    )
//    val border by animateColorAsState(
//        targetValue = when {
//            selected -> AppTheme.colors.AccentGreen
//            today -> AppTheme.colors.BorderHover
//            else -> AppTheme.colors.BorderDefault
//        }
//    )
//
//    Box(
//        modifier = Modifier
//            .weight(1f)
//            .height(40.sdp)
//            .clip(RoundedCornerShape(12.sdp))
//            .background(if (date == null) Color.Transparent else background)
//            .border(
//                width = if (date == null) 0.sdp else 1.sdp,
//                color = if (date == null) Color.Transparent else border,
//                shape = RoundedCornerShape(12.sdp)
//            )
//            .let { base ->
//                if (date != null) {
//                    base.clickable { onClick() }
//                } else {
//                    base
//                }
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = date?.dayOfMonth?.toString().orEmpty(),
//            color = when {
//                date == null -> Color.Transparent
//                selected -> AppTheme.colors.TextMain
//                else -> AppTheme.colors.TextSecondary
//            },
//            fontSize = 13.ssp,
//            fontWeight = if (selected || today) FontWeight.SemiBold else FontWeight.Medium
//        )
//    }
//}
//
//fun parseDate(dateString: String): LocalDate? {
//    if (dateString.isEmpty()) return null
//    return try {
//        val parts = dateString.split("/")
//        if (parts.size == 3) {
//            LocalDate.of(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
//        } else null
//    } catch (e: Exception) {
//        null
//    }
//}
//
// fun formatDate(date: LocalDate): String {
//    return "${date.dayOfMonth.toString().padStart(2, '0')}/${date.monthValue.toString().padStart(2, '0')}/${date.year}"
//}
//
//private fun LocalDate.formatOfferDate(): String =
//    format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))