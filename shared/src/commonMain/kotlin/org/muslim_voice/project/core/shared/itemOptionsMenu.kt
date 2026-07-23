package com.dodeal.features.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import org.muslim_voice.project.core.utiltis.onHover
import org.muslim_voice.project.core.utiltis.sdp

private val MenuWidth = 189
private val MenuCorner = 8
private val MenuPadding = 8
private val MenuItemGap = 8
private val MenuPopupGap = 4
private val MenuItemHeight = 24
private val MenuItemTextColor = Color(0xFFD2D4D9)
private val MenuDividerColor = Color(0xFF3B566A)

/**
 * Wraps an [anchor] (e.g. the ⋮ action button) and shows [EntityOptionsMenu] in a popup
 * directly underneath, matching [org.muslim_voice.project.core.shared.AppDropdown] positioning behavior.
 */
@Composable
fun EntityOptionsMenuHost(
    expanded: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    menuWidth: Dp = MenuWidth.sdp,
    popupAlignment: Alignment = Alignment.TopCenter,
    anchor: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier = modifier) {
        anchor()

        EntityOptionsMenu(
            expanded = expanded,
            onDismiss = onDismiss,
            menuWidth = menuWidth,
            content = content,
        )
    }
}

/**
 * Popup options menu panel. For correct placement under an anchor, prefer [EntityOptionsMenuHost]
 * or call from the same [Box] as the anchor (popup aligns to the parent box top edge).
 */
@Composable
fun EntityOptionsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    menuWidth: Dp = MenuWidth.sdp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val automaticLeftOffset = DpOffset(
        x = -menuWidth + 32.sdp,
        y = MenuPopupGap.sdp
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = automaticLeftOffset,
        scrollState = rememberScrollState(),
        properties = PopupProperties(focusable = true),
        containerColor = Color.Transparent,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(0.dp),
        border = null,
    ) {
        Box(
            modifier = Modifier
                .width(menuWidth)
                .clip(RoundedCornerShape(MenuCorner.sdp))

        ) {


            Column(
                modifier = Modifier.padding(MenuPadding.sdp),
                verticalArrangement = Arrangement.spacedBy(MenuItemGap.sdp),
            ) {
                content()
            }
        }
    }
}

@Composable
fun CustomItemOption(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    iconColor: Color = MenuItemTextColor,
    textColor: Color = MenuItemTextColor,
    showDividerBelow: Boolean = true,
) {
    var isHovered by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().height(MenuItemHeight.sdp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth().height(MenuItemHeight.sdp)
                .onHover { isHovered = it }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick,
                )
                .padding(horizontal = 8.sdp, vertical = 4.sdp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(9.sdp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.sdp),
            )
            Text(
                text = text,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
}