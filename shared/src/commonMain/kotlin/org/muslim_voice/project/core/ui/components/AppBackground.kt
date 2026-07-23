package org.muslim_voice.project.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.muslim_voice.project.core.ui.theme.AppColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = AppColors.Background)

            val blobRadius = size.minDimension * 0.45f
            val blobs = listOf(
                Triple(Offset(size.width * 0.15f, size.height * 0.12f), AppColors.Primary, 0.08f),
                Triple(Offset(size.width * 0.85f, size.height * 0.1f), AppColors.Accent, 0.07f),
                Triple(Offset(size.width * 0.1f, size.height * 0.88f), AppColors.Accent, 0.09f),
                Triple(Offset(size.width * 0.9f, size.height * 0.85f), AppColors.Primary, 0.06f),
            )
            blobs.forEach { (center, color, alpha) ->
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color.copy(alpha = alpha), Color.Transparent),
                        center = center,
                        radius = blobRadius,
                    ),
                    radius = blobRadius,
                    center = center,
                    blendMode = BlendMode.SrcOver,
                )
            }

            val tileSizePx = 80.dp.toPx()
            val cols = (size.width / tileSizePx).toInt() + 2
            val rows = (size.height / tileSizePx).toInt() + 2
            val starColor = AppColors.Primary.copy(alpha = 0.03f)
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    val cx = col * tileSizePx + tileSizePx / 2f
                    val cy = row * tileSizePx + tileSizePx / 2f
                    val starPath = buildIslamicStarPath(
                        center = Offset(cx, cy),
                        outerRadius = tileSizePx * 0.22f,
                        innerRadius = tileSizePx * 0.1f,
                        points = 8,
                    )
                    drawPath(
                        path = starPath,
                        color = starColor,
                        style = Stroke(width = 1.2f),
                    )
                }
            }
        }
        content()
    }
}

private fun buildIslamicStarPath(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    points: Int,
): Path {
    val path = Path()
    val total = points * 2
    for (i in 0 until total) {
        val angle = (PI / 2) + (i * PI / points)
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val x = center.x + (cos(angle) * radius).toFloat()
        val y = center.y - (sin(angle) * radius).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}
