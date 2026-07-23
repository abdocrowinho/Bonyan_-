package org.muslim_voice.project.core.utiltis


import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier

import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.onHover(onStatusChange: (Boolean) -> Unit): Modifier = this
    .pointerHoverIcon(PointerIcon.Hand)

@Composable
fun Modifier.screenPadding(): Modifier = this.padding(horizontal = 20.sdp)
