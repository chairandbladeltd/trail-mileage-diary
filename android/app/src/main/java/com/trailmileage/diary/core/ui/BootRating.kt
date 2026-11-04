package com.trailmileage.diary.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.trailmileage.diary.R
import com.trailmileage.diary.core.config.GameConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BootRating(
    value: Int,
    modifier: Modifier = Modifier,
    interactive: Boolean = false,
    onChange: (Int) -> Unit = {},
    bootSize: Dp = 44.dp,
    spacing: Dp = 10.dp,
    stateLabel: String = ""
) {
    val haptics = LocalHapticFeedback.current
    val scales = remember { List(GameConfig.MAX_DIFFICULTY) { Animatable(1f) } }

    LaunchedEffect(value, interactive) {
        if (!interactive) return@LaunchedEffect
        for (index in 0 until value.coerceAtMost(GameConfig.MAX_DIFFICULTY)) {
            launch {
                delay(index.toLong() * GameConfig.BOOT_STAGGER_MS)
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                scales[index].snapTo(1.35f)
                scales[index].animateTo(
                    targetValue = 1f,
                    animationSpec = tween(GameConfig.BOOT_STAMP_MS, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.semantics { stateDescription = stateLabel }
    ) {
        for (index in 0 until GameConfig.MAX_DIFFICULTY) {
            val filled = index < value
            val slot = Modifier.size(if (interactive) 48.dp else bootSize)
            Box(
                contentAlignment = Alignment.Center,
                modifier = if (interactive) {
                    slot.clickable { onChange(index + 1) }
                } else {
                    slot
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alpha = if (filled) 1f else 0.22f,
                    modifier = Modifier
                        .size(bootSize)
                        .graphicsLayer {
                            val factor = scales[index].value
                            scaleX = factor
                            scaleY = factor
                        }
                )
            }
            if (index < GameConfig.MAX_DIFFICULTY - 1) {
                Spacer(Modifier.width(spacing))
            }
        }
    }
}
