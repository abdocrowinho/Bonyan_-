package org.muslim_voice.project.core.utiltis

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val LocalDesignScale = staticCompositionLocalOf { 1f }

@Composable
fun ProvideAppScale(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        val designWidth = 430.dp

        val scale = (screenWidth / designWidth).coerceIn(0.75f, 1.4f)

        CompositionLocalProvider(LocalDesignScale provides scale) {
            content()
        }
    }
}

val Int.sdp: Dp
    @Composable get() = (this * LocalDesignScale.current).dp

val Float.sdp: Dp
    @Composable get() = (this * LocalDesignScale.current).dp

val Double.sdp: Dp
    @Composable get() = (this * LocalDesignScale.current).dp

val Int.ssp: TextUnit
    @Composable get() = (this * LocalDesignScale.current).sp

val Float.ssp: TextUnit
    @Composable get() = (this * LocalDesignScale.current).sp

val Double.ssp: TextUnit
    @Composable get() = (this.toFloat() * LocalDesignScale.current).sp