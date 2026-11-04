package com.trailmileage.diary.presentation.totals

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.trailmileage.diary.core.theme.OliveDeep
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.ParchmentDeep
import com.trailmileage.diary.core.theme.TrailOrange
import com.trailmileage.diary.core.ui.GlassCard
import com.trailmileage.diary.core.ui.MonthlyBars
import com.trailmileage.diary.core.ui.ScreenBackground
import com.trailmileage.diary.core.ui.ScreenHeader
import com.trailmileage.diary.core.ui.SectionHeader
import com.trailmileage.diary.core.ui.StatCard
import com.trailmileage.diary.core.ui.decimalText
import com.trailmileage.diary.core.ui.distanceUnit
import com.trailmileage.diary.core.ui.elevationUnit
import com.trailmileage.diary.core.ui.integerText
import com.trailmileage.diary.core.ui.measure
import com.trailmileage.diary.core.ui.monthInitials

@Composable
fun YearTotalsRoute(onBack: () -> Unit) {
    val totalsViewModel: YearTotalsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by totalsViewModel.state.collectAsStateWithLifecycle()
    YearTotalsScreen(state = state, onBack = onBack)
}

@Composable
private fun YearTotalsScreen(state: YearTotalsUiState, onBack: () -> Unit) {
    val haptics = LocalHapticFeedback.current
    var rolled by remember { mutableStateOf(false) }
    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) rolled = true
    }
    val distance by animateFloatAsState(
        targetValue = if (rolled) state.distance else 0f,
        animationSpec = tween(GameConfig.ODOMETER_MS, easing = FastOutSlowInEasing),
        finishedListener = { haptics.performHapticFeedback(HapticFeedbackType.LongPress) },
        label = "totalDistance"
    )
    val elevation by animateFloatAsState(
        targetValue = if (rolled) state.elevation else 0f,
        animationSpec = tween(GameConfig.ODOMETER_MS, easing = FastOutSlowInEasing),
        label = "totalElevation"
    )

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
                title = stringResource(R.string.totals_title),
                backLabel = stringResource(R.string.cd_back),
                onBack = onBack,
                trailing = {
                    Text(
                        text = state.year.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = TrailOrange
                    )
                }
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
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { liveRegion = LiveRegionMode.Polite }
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TotalsCounter(
                            label = stringResource(R.string.stat_year_distance),
                            value = measure(decimalText(distance), distanceUnit(state.imperial)),
                            accent = Olive,
                            modifier = Modifier.weight(1f)
                        )
                        TotalsCounter(
                            label = stringResource(R.string.stat_year_climb),
                            value = measure(integerText(elevation), elevationUnit(state.imperial)),
                            accent = TrailOrange,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                SectionHeader(title = stringResource(R.string.totals_months))
                MonthlyBars(
                    values = state.monthly,
                    highlightMonth = state.currentMonth,
                    labels = monthInitials(),
                    chartHeight = 120.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                if (state.hasData) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatCard(
                            label = stringResource(R.string.stat_hikes),
                            value = state.hikeCount.toString(),
                            accent = OliveDeep,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_longest),
                            value = measure(state.longestValue, distanceUnit(state.imperial)),
                            accent = Olive,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(R.string.stat_biggest_climb),
                            value = measure(state.biggestClimbValue, elevationUnit(state.imperial)),
                            accent = TrailOrange,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.totals_empty),
                        style = MaterialTheme.typography.labelSmall,
                        color = InkMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun TotalsCounter(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = value,
            color = accent,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Black,
            fontSize = 34.sp,
            maxLines = 1
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(2.dp)
                .background(accent, RoundedCornerShape(1.dp))
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Ink,
            fontSize = 10.sp
        )
    }
}
