package org.muslim_voice.project.core.component.datepicker

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun rememberDatePickerLauncher(
    onDateSelected: (LocalDate) -> Unit,
): () -> Unit {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date
                            onDateSelected(localDate)
                        }
                        showDialog = false
                    },
                ) {
                    Text(text = "تأكيد")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "إلغاء")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    return remember { { showDialog = true } }
}
