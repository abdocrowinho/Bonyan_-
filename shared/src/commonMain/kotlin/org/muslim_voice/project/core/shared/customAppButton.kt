package org.muslim_voice.project.core.shared

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.utilities.onHover
import org.muslim_voice.project.core.utilities.sdp
import org.muslim_voice.project.core.utilities.ssp

enum class AppButtonType {
    PRIMARY,
    SECONDARY,
    DISABLED_SOLID
}

enum class AppButtonSize {
    LARGE(),
    MEDIUM
}

/**
 * Primary Button
 */
@Composable
fun AppPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leftIcon: (@Composable () -> Unit)? = null,
    rightIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    CustomAppButton(
        text = text,
        modifier = modifier,
        type = if (enabled) AppButtonType.PRIMARY else AppButtonType.DISABLED_SOLID,
        size = AppButtonSize.LARGE,
        enabled = enabled,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
        onClick = onClick
    )
}

/**
 * Secondary Button
 */
@Composable
fun AppSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leftIcon: (@Composable () -> Unit)? = null,
    rightIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    CustomAppButton(
        text = text,
        modifier = modifier,
        type = AppButtonType.SECONDARY,
        size = AppButtonSize.LARGE,
        enabled = enabled,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
        onClick = onClick
    )
}

/**
 * Toolbar Button
 */
@Composable
fun AppToolbarButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leftIcon: (@Composable () -> Unit)? = null,
    rightIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    CustomAppButton(
        text = text,
        modifier = modifier,
        type = AppButtonType.SECONDARY,
        size = AppButtonSize.MEDIUM,
        enabled = enabled,
        leftIcon = leftIcon,
        rightIcon = rightIcon,
        onClick = onClick
    )
}

/**
 * Delete Button
 */
@Composable
fun AppDeleteButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val red = MaterialTheme.colorScheme.onError

    CustomAppButton(
        text = text,
        modifier = modifier,
        type = AppButtonType.SECONDARY,
        size = AppButtonSize.LARGE,
        enabled = enabled,
        backgroundColor = red.copy(alpha = 0.1f),
        contentColor = red.copy(alpha = 0.7f),
        borderColor = red.copy(alpha = 0.5f),
        onClick = onClick
    )
}

@Composable
fun CustomAppButton(
    text: String,
    modifier: Modifier = Modifier,
    type: AppButtonType = AppButtonType.PRIMARY,
    size: AppButtonSize = AppButtonSize.LARGE,
    enabled: Boolean = true,
    leftIcon: (@Composable () -> Unit)? = null,
    rightIcon: (@Composable () -> Unit)? = null,
    backgroundColor: Color? = null,
    contentColor: Color? = null,
    borderColor: Color? = null,
    cornerRadius: Dp = 999.sdp,
    textStyle: TextStyle? = null,
    onClick: () -> Unit,
) {

    val colors = MaterialTheme.colorScheme

    var isHovered by remember {
        mutableStateOf(false)
    }

    /**
     * Sizes
     */
    val buttonHeight = when (size) {
        AppButtonSize.LARGE -> 48.sdp
        AppButtonSize.MEDIUM -> 45.sdp
    }

    val horizontalPadding = when (size) {
        AppButtonSize.LARGE -> 24.sdp
        AppButtonSize.MEDIUM -> 20.sdp
    }

    /**
     * Colors
     */
    val baseBackgroundColor = backgroundColor ?: when {
        !enabled || type == AppButtonType.DISABLED_SOLID -> {
            colors.secondary .copy(alpha = 0.1f)
        }

        type == AppButtonType.PRIMARY -> {
            colors.primary
        }

        else -> {
            colors.primary .copy(alpha = 0.1f)
        }
    }

    val baseContentColor = contentColor ?: when {
        !enabled || type == AppButtonType.DISABLED_SOLID -> {
            Color(0xFFA4A9B4)
        }

        type == AppButtonType.PRIMARY -> {
            Color.White
        }

        else -> {
            colors.primary
        }
    }

    val baseBorderColor = borderColor ?: when {
        !enabled || type == AppButtonType.DISABLED_SOLID -> {
            null
        }

        type == AppButtonType.SECONDARY -> {
            colors.primary
        }

        else -> {
            null
        }
    }

    // Hover Animation

    val animatedBackgroundColor by animateColorAsState(
        targetValue = when {
            isHovered && enabled && type == AppButtonType.PRIMARY -> {
                baseBackgroundColor.copy(alpha = 0.85f)
            }

            isHovered && enabled && type == AppButtonType.SECONDARY -> {
                baseBackgroundColor.copy(alpha = 0.2f)
            }

            else -> {
                baseBackgroundColor
            }
        }
    )

    val finalTextStyle = textStyle ?: TextStyle(
        fontSize = 16.ssp,
        fontWeight = FontWeight.Medium,
        color = baseContentColor
    )

    val shape = RoundedCornerShape(cornerRadius)

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = buttonHeight)
            .clip(shape)
            .background(animatedBackgroundColor, shape)
            .then(
                if (baseBorderColor != null) {
                    Modifier.border(
                        width = 1.dp,
                        color = baseBorderColor,
                        shape = shape
                    )
                } else {
                    Modifier
                }
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .onHover {
                isHovered = it
            }
            .padding(
                horizontal = horizontalPadding,
                vertical = 12.sdp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        leftIcon?.let {
            it()

            Spacer(
                modifier = Modifier.width(10.sdp)
            )
        }

        Text(
            text = text,
            style = finalTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        rightIcon?.let {

            Spacer(
                modifier = Modifier.width(10.sdp)
            )

            it()
        }
    }
}
