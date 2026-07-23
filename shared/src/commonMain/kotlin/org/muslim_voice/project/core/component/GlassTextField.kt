package org.muslim_voice.project.core.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.theme.Spacing
import org.muslim_voice.project.core.theme.VoiceOfMuslimColors

@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, textAlign = TextAlign.End) },
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = VoiceOfMuslimColors.SurfaceGlassBorder,
                shape = RoundedCornerShape(Spacing.md),
            ),
        shape = RoundedCornerShape(Spacing.md),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = VoiceOfMuslimColors.SurfaceGlass,
            unfocusedContainerColor = VoiceOfMuslimColors.SurfaceGlass,
            focusedBorderColor = VoiceOfMuslimColors.GoldPrimary,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = VoiceOfMuslimColors.TextPrimary,
            unfocusedTextColor = VoiceOfMuslimColors.TextPrimary,
            focusedLabelColor = VoiceOfMuslimColors.GoldMuted,
            unfocusedLabelColor = VoiceOfMuslimColors.TextSecondary,
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
    )
}
