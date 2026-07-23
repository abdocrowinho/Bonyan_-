package org.muslim_voice.project.core.component.datepicker

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate

@Composable
expect fun rememberDatePickerLauncher(
    onDateSelected: (LocalDate) -> Unit,
): () -> Unit
