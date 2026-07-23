package org.muslim_voice.project.core.component.datepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import org.muslim_voice.project.core.component.AppTextField
import org.muslim_voice.project.core.ui.theme.AppTypography

// Tradeoff: uses a simple Compose dialog with numeric fields instead of UIDatePicker
// to avoid heavy UIKit interop while keeping the same rememberDatePickerLauncher contract.
@Composable
actual fun rememberDatePickerLauncher(
    onDateSelected: (LocalDate) -> Unit,
): () -> Unit {
    var showDialog by remember { mutableStateOf(false) }
    var day by remember { mutableIntStateOf(1) }
    var month by remember { mutableIntStateOf(1) }
    var year by remember { mutableIntStateOf(2000) }
    var validationError by remember { mutableStateOf<String?>(null) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "اختر التاريخ",
                    style = AppTypography.titleMedium,
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        DatePartField(
                            label = "اليوم",
                            value = day.toString(),
                            onValueChange = { value ->
                                day = value.filter(Char::isDigit).toIntOrNull() ?: day
                            },
                            modifier = Modifier.weight(1f),
                        )
                        DatePartField(
                            label = "الشهر",
                            value = month.toString(),
                            onValueChange = { value ->
                                month = value.filter(Char::isDigit).toIntOrNull() ?: month
                            },
                            modifier = Modifier.weight(1f),
                        )
                        DatePartField(
                            label = "السنة",
                            value = year.toString(),
                            onValueChange = { value ->
                                year = value.filter(Char::isDigit).toIntOrNull() ?: year
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    validationError?.let { error ->
                        Text(
                            text = error,
                            style = AppTypography.labelSmall,
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        runCatching {
                            onDateSelected(LocalDate(year, month, day))
                            validationError = null
                            showDialog = false
                        }.onFailure {
                            validationError = "تاريخ غير صالح"
                        }
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
        )
    }

    return remember {
        {
            validationError = null
            showDialog = true
        }
    }
}

@Composable
private fun DatePartField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier.padding(top = 4.dp),
        keyboardType = KeyboardType.Number,
        singleLine = true,
    )
}
