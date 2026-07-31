package org.muslim_voice.project.core.shared


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.dodeal.features.shared.InputFieldState
import org.muslim_voice.project.core.utilities.onHover
import org.muslim_voice.project.core.utilities.sdp
import org.muslim_voice.project.core.utilities.ssp
import org.muslim_voice.project.generated.resources.*
import org.jetbrains.compose.resources.painterResource

data class DropdownItem(
    val id: String,
    val label: String,
    val color: Color? = null,
    val value: String? = null,
)

enum class DropdownVariant { PILL, RECT }

private val DropdownGap = 5

@Composable
fun AppDropdown(
    selected: DropdownItem?,
    options: List<DropdownItem>,
    placeholder: String = "Select",
    onItemSelected: (DropdownItem?) -> Unit,
    allowClear: Boolean = false,
    isLoading: Boolean = false,
    variant: DropdownVariant = DropdownVariant.PILL,
    minWidth: Dp = 152.sdp,
    maxDropdownHeight: Dp = 260.sdp,
    triggerHeight: Dp = 30.sdp,
    fieldState: InputFieldState = InputFieldState.Idle,
    error: String? = null,
    modifier: Modifier = Modifier,
) {
    val effectiveState = when {
        error != null -> InputFieldState.Error
        else -> fieldState
    }
    val isError = effectiveState == InputFieldState.Error

    var expanded by remember { mutableStateOf(false) }
    var anchorWidthPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val triggerHeightPx = with(density) { triggerHeight.roundToPx() }
    val gapPx = with(density) { DropdownGap.sdp.roundToPx() }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(200),
        label = "dropdown_arrow",
    )
    val arrowColor = if (expanded) MaterialTheme.colorScheme.primary
    else Color(0xFFC8D6E0)

    val hasValue = selected != null

    val triggerCorner: Dp = when (variant) {
        DropdownVariant.PILL -> 999.sdp
        DropdownVariant.RECT -> 8.sdp
    }
    val triggerShape = RoundedCornerShape(triggerCorner)
    val listShape = RoundedCornerShape(16.sdp)
    val panelWidth = if (anchorWidthPx > 0) {
        with(density) { anchorWidthPx.toDp() }
    } else {
        minWidth
    }

    val popupOffset = IntOffset(0, triggerHeightPx + gapPx)

    val triggerBorderColor = when {
        isError -> MaterialTheme.colorScheme.onError
        selected?.color != null -> selected.color
        expanded || hasValue -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.inverseSurface
    }
    val triggerBackgroundColor = when {
        isError -> MaterialTheme.colorScheme.onError.copy(.1f)
        selected?.color != null -> selected.color!!.copy(alpha = 0.1f)
        else -> MaterialTheme.colorScheme.secondary.copy(.1f)
    }

    Column(modifier = modifier.animateContentSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    anchorWidthPx = coordinates.size.width
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(min = minWidth)
                    .height(triggerHeight)

                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { expanded = !expanded },
                    )
                    .padding(horizontal = 16.sdp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.sdp),
            ) {
                Text(
                    text = selected?.label ?: placeholder,
                    fontSize = 14.ssp,
                    fontWeight = FontWeight.Normal,
                    color = when {
                        isError -> MaterialTheme.colorScheme.onError
                        selected?.color != null -> selected.color!!
                        hasValue -> Color.White
                        else -> Color(0xFFD2D4D9)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )

                Icon(
                    painter = painterResource(resource = Res.drawable.ic_arrowup_drop_dwon),
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = when {
                        isError -> MaterialTheme.colorScheme.onError
                        selected?.color != null -> selected.color!!
                        else -> arrowColor
                    },
                    modifier = Modifier
                        .size(15.sdp)
                        .rotate(arrowRotation),
                )
            }

            if (expanded) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = popupOffset,
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(focusable = true),
                ) {
                    DropdownOptionsPanel(
                        width = panelWidth,
                        minWidth = minWidth,
                        listShape = listShape,
                        isLoading = isLoading,
                        options = options,
                        selected = selected,
                        allowClear = allowClear,
                        maxDropdownHeight = maxDropdownHeight,
                        onItemSelected = onItemSelected,
                        onDismiss = { expanded = false },
                    )
                }
            }
        }

        if (error != null) {
            Text(
                text = error,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onError,
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.Normal,
                ),
                modifier = Modifier.padding(start = 16.sdp, top = 4.sdp),
            )
        }
    }
}

@Composable
private fun DropdownOptionsPanel(
    width: Dp,
    minWidth: Dp,
    listShape: Shape,
    isLoading: Boolean,
    options: List<DropdownItem>,
    selected: DropdownItem?,
    allowClear: Boolean,
    maxDropdownHeight: Dp,
    onItemSelected: (DropdownItem?) -> Unit,
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(width)
            .widthIn(min = minWidth)
            .wrapContentHeight()
            .clip(listShape)
    ) {
        Box(modifier = Modifier) {

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(60.sdp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.sdp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.sdp,
                        )
                    }
                }

                options.isEmpty() -> {
                    Text(
                        text = "No options available",
                        fontSize = 13.ssp,
                        color = Color(0xFFD2D4D9),
                        modifier = Modifier.padding(horizontal = 16.sdp, vertical = 10.sdp),
                    )
                }

                else -> {
                    val listScrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = maxDropdownHeight)
                            .verticalScroll(listScrollState),
                    ) {
                        if (allowClear) {
                            DropdownListItem(
                                label = "— Unassigned —",
                                isSelected = false,
                                accentColor = selected?.color?.copy(.1f) ?: Color(0xFFD2D4D9),
                                onClick = {
                                    onItemSelected(null)
                                    onDismiss()
                                },
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 12.sdp),
                                color = MaterialTheme.colorScheme.inverseSurface,
                                thickness = 0.5.sdp,
                            )
                        }

                        options.forEach { item ->
                            val isSelected = item.id == selected?.id
                            DropdownListItem(
                                label = item.label,
                                isSelected = isSelected,
                                accentColor = item.color ?: MaterialTheme.colorScheme.primary,
                                onClick = {
                                    onItemSelected(item)
                                    onDismiss()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DropdownListItem(
    label: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
) {
    var isHovered by remember { mutableStateOf(false) }


    val rowBackground = when {
        isSelected -> accentColor.copy(alpha = 0.10f)
        isHovered -> MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(31.sdp)
            .background(rowBackground)
            .then(
                if (isSelected) {
                    val lineColor = accentColor
                    Modifier.drawBehind {
                        val strokePx = 2.dp.toPx()
                        drawLine(
                            color = lineColor,
                            start = Offset(strokePx / 2f, 0f),
                            end = Offset(strokePx / 2f, size.height),
                            strokeWidth = strokePx,
                        )
                    }
                } else Modifier
            )
            .onHover { isHovered = it }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(horizontal = 16.sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.sdp),
    ) {


        Text(
            text = label,
            fontSize = 14.ssp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) accentColor else Color(0xFFD2D4D9),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun RowScope.AppDropdownCell(
    weight: Float,
    selected: DropdownItem?,
    options: List<DropdownItem>,
    placeholder: String = "Select",
    isLoading: Boolean = false,
    allowClear: Boolean = false,
    fieldState: InputFieldState = InputFieldState.Idle,
    error: String? = null,
    onItemSelected: (DropdownItem?) -> Unit,
) {
    Box(modifier = Modifier.weight(weight)) {
        AppDropdown(
            selected = selected,
            options = options,
            placeholder = placeholder,
            onItemSelected = onItemSelected,
            allowClear = allowClear,
            isLoading = isLoading,
            variant = DropdownVariant.PILL,
            minWidth = 0.dp,
            fieldState = fieldState,
            error = error,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
