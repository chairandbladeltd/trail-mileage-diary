package com.trailmileage.diary.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trailmileage.diary.core.config.GameConfig
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.RuleLine
import com.trailmileage.diary.core.theme.TrailOrange

@Composable
fun MonthlyBars(
    values: List<Double>,
    highlightMonth: Int,
    labels: List<String>,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 120.dp,
    barColor: Color = Olive,
    highlightColor: Color = TrailOrange,
    baselineColor: Color = RuleLine,
    labelColor: Color = InkMuted
) {
    val peak = (values.maxOrNull() ?: 0.0).coerceAtLeast(1.0)
    val grown by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(GameConfig.BAR_GROW_MS),
        label = "bars"
    )
    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            val slot = size.width / values.size
            val barWidth = slot * 0.52f
            val baseline = size.height - 1.dp.toPx()
            drawRect(
                color = baselineColor,
                topLeft = Offset(0f, baseline),
                size = Size(size.width, 1.dp.toPx())
            )
            values.forEachIndexed { index, value ->
                val ratio = (value / peak).toFloat().coerceIn(0f, 1f) * grown
                val barHeight = (baseline - 4.dp.toPx()) * ratio
                if (barHeight > 0.5f) {
                    drawRoundRect(
                        color = if (index + 1 == highlightMonth) highlightColor else barColor,
                        topLeft = Offset(slot * index + (slot - barWidth) / 2f, baseline - barHeight),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEachIndexed { index, label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = if (index + 1 == highlightMonth) highlightColor else labelColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
