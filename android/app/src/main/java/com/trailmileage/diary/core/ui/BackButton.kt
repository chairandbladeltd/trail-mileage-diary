package com.trailmileage.diary.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.trailmileage.diary.core.theme.Olive

@Composable
fun BackButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    tint: Color = Olive
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .clickable(onClick = onClick)
            .semantics { contentDescription = label }
    ) {
        Canvas(modifier = Modifier.size(22.dp)) {
            val stroke = 2.4.dp.toPx()
            val right = size.width * 0.72f
            val left = size.width * 0.28f
            val mid = size.height / 2f
            drawLine(
                color = tint,
                start = Offset(right, size.height * 0.16f),
                end = Offset(left, mid),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = tint,
                start = Offset(left, mid),
                end = Offset(right, size.height * 0.84f),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        }
    }
}
