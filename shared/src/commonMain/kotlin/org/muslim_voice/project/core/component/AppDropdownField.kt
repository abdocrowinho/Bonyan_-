package org.muslim_voice.project.core.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.ui.theme.AppTypography
import org.muslim_voice.project.core.ui.theme.AppColors

data class DropdownOption(
    val id: String,
    val label: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdownField(
    selected: DropdownOption?,
    options: List<DropdownOption>,
    onOptionSelected: (DropdownOption) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    searchable: Boolean = false,
    placeholder: String = "اختر...",
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "dropdownArrowRotation",
    )

    val fieldValue = when {
        searchable && (expanded || searchQuery.isNotEmpty()) -> searchQuery
        else -> selected?.label.orEmpty()
    }

    val filteredOptions = remember(options, searchQuery, searchable) {
        if (searchable && searchQuery.isNotBlank()) {
            options.filter { option ->
                option.label.contains(searchQuery, ignoreCase = true)
            }
        } else {
            options
        }
    }

    val toggleExpanded = {
        expanded = !expanded
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { isExpanded ->
            expanded = isExpanded
            if (!isExpanded) {
                searchQuery = ""
            }
        },
        modifier = modifier.fillMaxWidth(),
    ) {
        AppTextField(
            value = fieldValue,
            onValueChange = { value ->
                if (searchable) {
                    searchQuery = value
                    expanded = true
                }
            },
            label = label,
            placeholder = if (selected == null) placeholder else null,
            errorMessage = errorMessage,
            readOnly = !searchable,
            trailingIcon = Icons.Filled.KeyboardArrowDown,
            trailingIconRotation = arrowRotation,
            onTrailingIconClick = toggleExpanded,
            textFieldModifier = Modifier.menuAnchor(
                type = if (searchable) {
                    MenuAnchorType.PrimaryEditable
                } else {
                    MenuAnchorType.PrimaryNotEditable
                },
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded && filteredOptions.isNotEmpty(),
            onDismissRequest = {
                expanded = false
                searchQuery = ""
            },
            modifier = Modifier
                .heightIn(max = 280.dp)
                .exposedDropdownSize(matchTextFieldWidth = true),
            containerColor = AppColors.Surface,
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.label,
                            style = AppTypography.bodyMedium,
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        searchQuery = ""
                        expanded = false
                    },
                )
            }
        }
    }
}
