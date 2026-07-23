package org.muslim_voice.project.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.core.ui.theme.AppShapes

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val isError = !errorMessage.isNullOrBlank()
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = hint, color = AppColors.Subtle) },
            isError = isError,
            shape = AppShapes.Small,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.Primary,
                unfocusedBorderColor = AppColors.Divider,
                errorBorderColor = AppColors.Error,
                cursorColor = AppColors.Primary,
            ),
            singleLine = true,
        )
        if (isError) {
            Text(
                text = errorMessage.orEmpty(),
                color = AppColors.Error,
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            )
        }
    }
}
