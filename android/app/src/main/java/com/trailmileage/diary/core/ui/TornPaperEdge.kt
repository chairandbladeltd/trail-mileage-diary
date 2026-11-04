package com.trailmileage.diary.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun TornPaperEdge(
    color: Color,
    modifier: Modifier = Modifier,
    seed: Int = 0,
    notches: Int = 7
) {
    Canvas(modifier = modifier) {
        val path = Path()
        val step = size.width / notches
        path.moveTo(0f, 0f)
        for (index in 0..notches) {
            val x = step * index
            val wobble = abs(sin((index + 1) * 1.7f + seed * 0.37f))
            val y = size.height * (0.35f + 0.6f * wobble)
            path.lineTo(x, y)
        }
        path.lineTo(size.width, 0f)
        path.close()
        drawPath(path = path, color = color)
    }
}
