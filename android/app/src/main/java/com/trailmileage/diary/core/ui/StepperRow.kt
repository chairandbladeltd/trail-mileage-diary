package com.trailmileage.diary.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trailmileage.diary.R
import com.trailmileage.diary.core.config.GameConfig
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.ParchmentDeep
import com.trailmileage.diary.core.theme.RuleLine

@Composable
fun StepperRow(
    label: String,
    value: String,
    unit: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = Olive,
    minusEnabled: Boolean = true,
    plusEnabled: Boolean = true,
    stateLabel: String = ""
) {
    GlassCard(modifier = modifier.semantics { stateDescription = stateLabel }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMuted
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    AnimatedContent(
                        targetState = value,
                        transitionSpec = {
                            fadeIn(tween(GameConfig.VALUE_FADE_MS)) togetherWith
                                fadeOut(tween(GameConfig.VALUE_FADE_MS))
                        },
                        label = "value"
                    ) { shown ->
                        Text(
                            text = shown,
                            color = accent,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Black,
                            fontSize = 34.sp,
                            maxLines = 1
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.labelSmall,
                        color = InkMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }
            StepperKey(enabled = minusEnabled, onClick = onMinus, accent = accent) {
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .height(3.dp)
                        .background(if (minusEnabled) Parchment else InkMuted)
                )
            }
            Spacer(Modifier.width(10.dp))
            StepperKey(enabled = plusEnabled, onClick = onPlus, accent = accent) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                    contentDescription = null,
                    tint = if (plusEnabled) Parchment else InkMuted,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun StepperKey(
    enabled: Boolean,
    onClick: () -> Unit,
    accent: Color,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (enabled) accent else ParchmentDeep,
        border = BorderStroke(1.dp, RuleLine),
        modifier = Modifier
            .size(56.dp)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(56.dp)
        ) {
            content()
        }
    }
}

