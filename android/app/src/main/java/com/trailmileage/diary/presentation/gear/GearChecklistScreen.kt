package com.trailmileage.diary.presentation.gear

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmileage.diary.R
import com.trailmileage.diary.core.config.GameConfig
import com.trailmileage.diary.core.di.ServiceLocator
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.ParchmentDeep
import com.trailmileage.diary.core.theme.ParchmentLight
import com.trailmileage.diary.core.theme.RuleLine
import com.trailmileage.diary.core.ui.PrimaryButton
import com.trailmileage.diary.core.ui.ScreenBackground
import com.trailmileage.diary.core.ui.ScreenHeader
import com.trailmileage.diary.core.ui.SecondaryButton

@Composable
fun GearChecklistRoute(
    pickMode: Boolean,
    onBack: () -> Unit
) {
    val gearViewModel: GearChecklistViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gearViewModel.state.collectAsStateWithLifecycle()
    GearChecklistScreen(
        state = state,
        pickMode = pickMode,
        onBack = onBack,
        onSelectTab = gearViewModel::onSelectTab,
        onToggle = gearViewModel::onToggle,
        onReset = gearViewModel::onReset,
        onAttach = {
            gearViewModel.onAttach()
            onBack()
        }
    )
}

@Composable
private fun GearChecklistScreen(
    state: GearChecklistUiState,
    pickMode: Boolean,
    onBack: () -> Unit,
    onSelectTab: (String) -> Unit,
    onToggle: (String) -> Unit,
    onReset: () -> Unit,
    onAttach: () -> Unit
) {
    ScreenBackground(
        gradient = listOf(Color(0xFFF7F3EA), Parchment, ParchmentDeep),
        textureAlpha = 0.12f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.gear_title),
                backLabel = stringResource(R.string.cd_back),
                onBack = onBack
            )
            if (state.isLoading) {
                Box(modifier = Modifier.weight(1f)) {
                    CircularProgressIndicator(
                        color = Olive,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                return@Column
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                state.tabs.forEach { tab ->
                    FilterChip(
                        selected = tab.id == state.selectedId,
                        onClick = { onSelectTab(tab.id) },
                        shape = RoundedCornerShape(14.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = ParchmentLight,
                            selectedContainerColor = Olive,
                            labelColor = InkMuted,
                            selectedLabelColor = Parchment
                        ),
                        label = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1
                            )
                        },
                        modifier = Modifier.height(40.dp)
                    )
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.items, key = { item -> item.id }) { item ->
                    GearRow(
                        name = item.name,
                        packed = item.packed,
                        onToggle = { onToggle(item.id) }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ParchmentDeep.copy(alpha = 0.9f))
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.gear_packed,
                        state.packedCount,
                        state.totalCount
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = Ink
                )
                Spacer(Modifier.height(10.dp))
                if (pickMode) {
                    PrimaryButton(
                        text = stringResource(R.string.cta_attach_list),
                        onClick = onAttach,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))
                }
                SecondaryButton(
                    text = stringResource(R.string.cta_reset_list),
                    onClick = onReset,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun GearRow(
    name: String,
    packed: Boolean,
    onToggle: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    val progress = remember { Animatable(if (packed) 1f else 0f) }
    LaunchedEffect(packed) {
        progress.animateTo(
            targetValue = if (packed) 1f else 0f,
            animationSpec = tween(
                durationMillis = if (packed) GameConfig.CHECK_DRAW_MS else 120,
                easing = FastOutSlowInEasing
            )
        )
    }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onToggle()
                }
                .semantics { stateDescription = name }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(26.dp)
                    .background(
                        if (packed) Olive.copy(alpha = 0.12f) else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Canvas(modifier = Modifier.size(26.dp)) {
                    val stroke = 2.dp.toPx()
                    drawRoundRect(
                        color = Olive,
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                        style = Stroke(width = stroke)
                    )
                    val value = progress.value
                    if (value > 0f) {
                        val startX = size.width * 0.24f
                        val midX = size.width * 0.44f
                        val endX = size.width * 0.78f
                        val startY = size.height * 0.52f
                        val midY = size.height * 0.72f
                        val endY = size.height * 0.28f
                        val firstLeg = (value / 0.4f).coerceIn(0f, 1f)
                        drawLine(
                            color = Olive,
                            start = Offset(startX, startY),
                            end = Offset(
                                startX + (midX - startX) * firstLeg,
                                startY + (midY - startY) * firstLeg
                            ),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                        if (value > 0.4f) {
                            val secondLeg = ((value - 0.4f) / 0.6f).coerceIn(0f, 1f)
                            drawLine(
                                color = Olive,
                                start = Offset(midX, midY),
                                end = Offset(
                                    midX + (endX - midX) * secondLeg,
                                    midY + (endY - midY) * secondLeg
                                ),
                                strokeWidth = stroke,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = if (packed) InkMuted else Ink,
                fontSize = 15.sp,
                maxLines = 1
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(RuleLine)
        )
    }
}
