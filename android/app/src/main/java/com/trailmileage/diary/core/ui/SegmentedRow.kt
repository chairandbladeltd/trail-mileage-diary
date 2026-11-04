package com.trailmileage.diary.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.ParchmentDeep
import com.trailmileage.diary.core.theme.RuleLine

@Composable
fun SegmentedRow(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = Olive
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = ParchmentDeep,
        border = BorderStroke(1.dp, RuleLine),
        modifier = modifier.height(48.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, option ->
                val active = index == selectedIndex
                val container by animateColorAsState(
                    targetValue = if (active) accent else Color.Transparent,
                    animationSpec = tween(160),
                    label = "segment"
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                        .background(container, RoundedCornerShape(11.dp))
                        .clickable { onSelect(index) }
                        .semantics { stateDescription = option }
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (active) Parchment else InkMuted,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
