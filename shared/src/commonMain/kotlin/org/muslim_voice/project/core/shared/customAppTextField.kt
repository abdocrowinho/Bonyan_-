package com.dodeal.features.shared

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import org.muslim_voice.project.core.theme.VoiceOfMuslimColors
import org.muslim_voice.project.core.utiltis.onHover
import org.muslim_voice.project.core.utiltis.sdp
import org.muslim_voice.project.core.utiltis.ssp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class InputFieldState {
    Idle,
    Active,
    Error,
    Success
}

@Composable
fun CustomAppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Search",
    modifier: Modifier = Modifier,
    fieldState: InputFieldState = InputFieldState.Idle,
    leadingIconNew: Any? = null,
    trailingIconNew: Any? = null,
    leadingIcon: DrawableResource? = null,
    trailingIcon: ImageVector? = null,
    isIconRight: Boolean = false,
    singleLine: Boolean = true,
    height: Dp = 45.sdp,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    cornerRadius: Dp = 50.sdp,
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    textStyle: TextStyle? = null,
    iconPadding: Dp = 10.sdp,
    fieldPadding: Dp = 16.sdp,
    iconSize: Dp = 18.sdp,
    error: String? = null,
    edgeBorderColor: Color? = null,
) {
    val effectiveState = when {
        error != null -> InputFieldState.Error
        else -> fieldState
    }

    val fieldBg = when (effectiveState) {
        InputFieldState.Idle -> VoiceOfMuslimColors.BackgroundElevated
        InputFieldState.Active -> edgeBorderColor?.copy(.1f) ?: MaterialTheme.colorScheme.primary.copy(.1f)
        InputFieldState.Error -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        InputFieldState.Success -> VoiceOfMuslimColors.GreenBright.copy(alpha = 0.1f)
    }
    val fieldBorder = when (effectiveState) {
        InputFieldState.Idle -> VoiceOfMuslimColors.TextPrimary.copy(alpha = 0.2f)
        InputFieldState.Active -> edgeBorderColor ?: MaterialTheme.colorScheme.primary
        InputFieldState.Error -> MaterialTheme.colorScheme.error
        InputFieldState.Success -> VoiceOfMuslimColors.GreenBright
    }
    val fieldTextColor = when (effectiveState) {
        InputFieldState.Idle -> if (value.isEmpty()) VoiceOfMuslimColors.TextPrimary.copy(alpha = 0.5f) else VoiceOfMuslimColors.TextPrimary
        InputFieldState.Active -> edgeBorderColor ?: MaterialTheme.colorScheme.primary
        InputFieldState.Error -> MaterialTheme.colorScheme.error
        InputFieldState.Success -> VoiceOfMuslimColors.GreenBright
    }

    var isHovered by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val isPasswordType = keyboardType == KeyboardType.Password

    val resolvedState = when {
        isFocused && effectiveState == InputFieldState.Idle -> InputFieldState.Active
        else -> effectiveState
    }

    val resolvedBg = when (resolvedState) {
        InputFieldState.Idle -> fieldBg
        InputFieldState.Active -> edgeBorderColor?.copy(.1f) ?: MaterialTheme.colorScheme.primary.copy(.1f)
        InputFieldState.Error -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        InputFieldState.Success -> VoiceOfMuslimColors.GreenBright.copy(alpha = 0.1f)
    }
    val resolvedBorder = edgeBorderColor ?: when (resolvedState) {
        InputFieldState.Idle -> if (isHovered) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else fieldBorder
        InputFieldState.Active -> MaterialTheme.colorScheme.primary
        InputFieldState.Error -> MaterialTheme.colorScheme.error
        InputFieldState.Success -> VoiceOfMuslimColors.GreenBright
    }
    val resolvedTextColor = when (resolvedState) {
        InputFieldState.Idle -> if (value.isEmpty()) VoiceOfMuslimColors.TextPrimary.copy(alpha = 0.5f) else VoiceOfMuslimColors.TextPrimary
        InputFieldState.Active -> MaterialTheme.colorScheme.primary
        InputFieldState.Error -> MaterialTheme.colorScheme.error
        InputFieldState.Success -> VoiceOfMuslimColors.GreenBright
    }

    val animatedBorder by animateColorAsState(resolvedBorder, tween(300))
    val animatedBg by animateColorAsState(resolvedBg, tween(300))

    val resolvedStyle = textStyle ?: TextStyle(
        color = resolvedTextColor,
        fontSize = 16.ssp,
        fontWeight = FontWeight.Normal,
    )

    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(value.length) {
        if (value.isNotEmpty()) {
            shakeOffset.animateTo(5f, spring(Spring.DampingRatioHighBouncy, Spring.StiffnessMedium))
            shakeOffset.animateTo(0f)
        }
    }

    val cardShape = RoundedCornerShape(cornerRadius)

    Column(modifier = modifier.animateContentSize()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .onFocusChanged { isFocused = it.isFocused }
                .onHover { isHovered = it }
                .clip(cardShape)
                .background(animatedBg, cardShape)
                .border(0.5.sdp, animatedBorder, cardShape)
                .padding(horizontal = fieldPadding),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            enabled = enabled,
            readOnly = readOnly,
            maxLines = maxLines,
            textStyle = resolvedStyle,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPasswordType && !passwordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {

                    val showLegacyLeft = !isIconRight && (leadingIcon != null || trailingIcon != null || isPasswordType)
                    val showNewLeft = leadingIconNew != null

                    if (showNewLeft || showLegacyLeft) {
                        if (showNewLeft) {
                            FlexibleIcon(icon = leadingIconNew, tint = resolvedTextColor, shakeOffset = shakeOffset.value, iconSize = iconSize)
                        } else {
                            IconSection(
                                leadingIcon = leadingIcon, trailingIcon = trailingIcon, iconTint = resolvedTextColor,
                                shakeOffset = shakeOffset.value, iconSize = iconSize, isPassword = isPasswordType,
                                passwordVisible = passwordVisible, onPasswordToggle = { passwordVisible = !passwordVisible }
                            )
                        }
                        Spacer(Modifier.width(iconPadding))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = VoiceOfMuslimColors.TextPrimary.copy(alpha = 0.5f),
                                style = resolvedStyle.copy(color = VoiceOfMuslimColors.TextPrimary.copy(alpha = 0.5f))
                            )
                        }
                        innerTextField()
                    }

                    val showLegacyRight = isIconRight && (leadingIcon != null || trailingIcon != null || isPasswordType)
                    val showNewRight = trailingIconNew != null

                    if (showNewRight || showLegacyRight) {
                        Spacer(Modifier.width(iconPadding))
                        if (showNewRight) {
                            FlexibleIcon(icon = trailingIconNew, tint = resolvedTextColor, shakeOffset = shakeOffset.value, iconSize = iconSize)
                        } else {
                            IconSection(
                                leadingIcon = leadingIcon, trailingIcon = trailingIcon, iconTint = resolvedTextColor,
                                shakeOffset = shakeOffset.value, iconSize = iconSize, isPassword = isPasswordType,
                                passwordVisible = passwordVisible, onPasswordToggle = { passwordVisible = !passwordVisible }
                            )
                        }
                    }
                }
            }
        )

        if (error != null) {
            Text(
                text = error,
                style = resolvedStyle.copy(color = MaterialTheme.colorScheme.error, fontSize = 11.ssp),
                modifier = Modifier.padding(start = 16.sdp, top = 4.sdp)
            )
        }
    }
}

@Composable
private fun FlexibleIcon(
    icon: Any?,
    tint: Color,
    shakeOffset: Float,
    iconSize: Dp
) {
    val modifier = Modifier
        .size(iconSize)
        .graphicsLayer { translationX = shakeOffset }

    when (icon) {
        is DrawableResource -> {
            Icon(painter = painterResource(icon), contentDescription = null, tint = tint, modifier = modifier)
        }
        is ImageVector -> {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = modifier)
        }
    }
}

@Composable
private fun IconSection(
    leadingIcon: DrawableResource?,
    trailingIcon: ImageVector?,
    iconTint: Color,
    shakeOffset: Float,
    iconSize: Dp = 18.sdp,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {}
) {
    val modifier = Modifier
        .size(iconSize)
        .graphicsLayer { translationX = shakeOffset }

    if (isPassword) {
        Icon(
            imageVector = if (passwordVisible) Icons.Default.Share else Icons.Default.Add,
            contentDescription = "Toggle Password Visibility",
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onPasswordToggle() }
        )
    } else if (leadingIcon != null) {
        Icon(
            painter = painterResource(leadingIcon),
            contentDescription = null,
            tint = iconTint,
            modifier = modifier
        )
    } else if (trailingIcon != null) {
        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            tint = iconTint,
            modifier = modifier
        )
    }
}