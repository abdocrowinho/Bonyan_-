//package com.dodeal.features.shared
//
//import androidx.compose.foundation.ScrollState
//import androidx.compose.foundation.VerticalScrollbar
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollbarAdapter
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import com.dodeal.theme.AppTheme
//import org.muslim_voice.project.core.utiltis.pxToSdp
//import org.muslim_voice.project.core.utilities.sdp
//
//
//@Composable
//fun CustomVerticalScrollbar(
//    modifier: Modifier,
//    lazyListState: ScrollState
//) {
//    VerticalScrollbar(
//        adapter = rememberScrollbarAdapter(scrollState = lazyListState),
//        modifier = modifier
//            .fillMaxHeight()
//            .width(6.sdp),
//        style = androidx.compose.foundation.defaultScrollbarStyle().copy(
//            unhoverColor = AppTheme.colors.AccentGreen.copy(0.5f),
//            hoverColor = AppTheme.colors.AccentGreen,
//            shape = RoundedCornerShape(50)
//        )
//    )
//}
//
//@Composable
//fun CustomHorizontalScrollbar(scrollState: ScrollState, modifier: Modifier) {
//    if (scrollState.maxValue <= 0) return
//
//    BoxWithConstraints(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(10.sdp)
//            .background(Color.Black.copy(0.05f))
//    ) {
//        val viewPortWidthPx = constraints.maxWidth.toFloat()
//        val totalContentWidthPx = viewPortWidthPx + scrollState.maxValue.toFloat()
//
//        val thumbWidthPx = (viewPortWidthPx / totalContentWidthPx) * viewPortWidthPx
//
//        val trackWidthPx = viewPortWidthPx - thumbWidthPx
//
//        val scrollFraction = scrollState.value.toFloat() / scrollState.maxValue
//
//        Box(
//            modifier = Modifier
//                .offset(x = (trackWidthPx * scrollFraction).pxToSdp())
//                .width(thumbWidthPx.pxToSdp())
//                .fillMaxHeight()
//                .padding(vertical = 2.sdp)
//                .background(AppTheme.colors.AccentGreen, RoundedCornerShape(50))
//                .pointerInput(scrollState.maxValue) {
//                    detectDragGestures { change, dragAmount ->
//                        change.consume()
//
//                        val scrollDelta = (dragAmount.x / trackWidthPx) * scrollState.maxValue
//
//                        scrollState.dispatchRawDelta(scrollDelta)
//                    }
//                }
//        )
//    }
//}
