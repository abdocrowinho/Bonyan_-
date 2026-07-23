package org.muslim_voice.project.core.component

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDate
import org.muslim_voice.project.core.component.datepicker.rememberDatePickerLauncher

@Composable
fun AppDatePickerField(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    val showDatePicker = rememberDatePickerLauncher(onDateSelected = onDateSelected)

    AppTextField(
        value = selectedDate?.let(::formatLocalDate).orEmpty(),
        onValueChange = {},
        label = label,
        modifier = modifier.clickable { showDatePicker() },
        placeholder = "اختر التاريخ",
        errorMessage = errorMessage,
        readOnly = true,
        trailingIcon = Icons.Filled.CalendarToday,
        onTrailingIconClick = showDatePicker,
    )
}

private fun formatLocalDate(date: LocalDate): String {
    return buildString {
        append(date.dayOfMonth.toString().padStart(2, '0'))
        append('/')
        append(date.monthNumber.toString().padStart(2, '0'))
        append('/')
        append(date.year)
    }
}
