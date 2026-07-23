package org.muslim_voice.project.core.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.muslim_voice.project.core.ui.theme.AppColors
import org.muslim_voice.project.core.ui.theme.AppTypography

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    placeholder: String? = null,
    errorMessage: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    trailingIconRotation: Float = 0f,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isError = errorMessage != null

    var passwordVisible by remember { mutableStateOf(false) }

    val iconTint = when {
        isError -> AppColors.Error
        isFocused -> AppColors.Primary
        else -> AppColors.Subtle
    }

    val labelColor = when {
        isError -> AppColors.Error
        isFocused -> AppColors.Primary
        else -> AppColors.Subtle
    }

    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = textFieldModifier.fillMaxWidth(),
            label = {
                Text(
                    text = label,
                    style = AppTypography.labelSmall.copy(color = labelColor),
                )
            },
            placeholder = placeholder?.let { hint ->
                {
                    Text(
                        text = hint,
                        style = AppTypography.bodyMedium.copy(color = AppColors.Subtle),
                    )
                }
            },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                    )
                }
            },
            trailingIcon = when {
                isPassword -> {
                    {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = null,
                                tint = iconTint,
                            )
                        }
                    }
                }

                trailingIcon != null -> {
                    {
                        if (onTrailingIconClick != null) {
                            IconButton(onClick = onTrailingIconClick) {
                                Icon(
                                    imageVector = trailingIcon,
                                    contentDescription = null,
                                    tint = iconTint,
                                    modifier = Modifier.rotate(trailingIconRotation),
                                )
                            }
                        } else {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.rotate(trailingIconRotation),
                            )
                        }
                    }
                }

                else -> null
            },
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
            ),
            singleLine = singleLine,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = AppColors.Primary,
                unfocusedIndicatorColor = AppColors.Divider,
                errorIndicatorColor = AppColors.Error,
                disabledIndicatorColor = AppColors.Divider.copy(alpha = 0.5f),
                focusedTextColor = AppColors.OnSurface,
                unfocusedTextColor = AppColors.OnSurface,
                disabledTextColor = AppColors.OnSurface.copy(alpha = 0.5f),
                cursorColor = AppColors.Primary,
            ),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .padding(top = 2.dp),
        ) {
            if (isError) {
                Text(
                    text = errorMessage.orEmpty(),
                    color = AppColors.Error,
                    fontSize = 12.sp,
                    style = AppTypography.labelSmall.copy(
                        color = AppColors.Error,
                        fontSize = 12.sp,
                    ),
                )
            }
        }
    }
}
