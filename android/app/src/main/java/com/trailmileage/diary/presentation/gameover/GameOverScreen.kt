package com.trailmileage.diary.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmileage.diary.R
import com.trailmileage.diary.core.config.GameConfig
import com.trailmileage.diary.core.di.ServiceLocator
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.OliveDeep
import com.trailmileage.diary.core.theme.OliveLight
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.ParchmentLight
import com.trailmileage.diary.core.theme.RuleLine
import com.trailmileage.diary.core.theme.TrailOrange
import com.trailmileage.diary.core.theme.TrailOrangeLight
import com.trailmileage.diary.core.ui.GlassCard
import com.trailmileage.diary.core.ui.MonthlyBars
import com.trailmileage.diary.core.ui.PrimaryButton
import com.trailmileage.diary.core.ui.ScreenBackground
import com.trailmileage.diary.core.ui.SecondaryButton
import com.trailmileage.diary.core.ui.StatCard
import com.trailmileage.diary.core.ui.decimalText
import com.trailmileage.diary.core.ui.distanceUnit
import com.trailmileage.diary.core.ui.elevationUnit
import com.trailmileage.diary.core.ui.integerText
import com.trailmileage.diary.core.ui.measure
import com.trailmileage.diary.core.ui.monthInitials

@Composable
fun GameOverRoute(
    hikeId: Long,
    onLogAgain: () -> Unit,
    onViewHike: (Long) -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(hikeId) { gameOverViewModel.load(hikeId) }
    GameOverScreen(
        state = state,
        onLogAgain = onLogAgain,
        onViewHike = { onViewHike(hikeId) },
        onMenu = onMenu
    )
}

@Composable
private fun GameOverScreen(
    state: GameOverUiState,
    onLogAgain: () -> Unit,
    onViewHike: () -> Unit,
    onMenu: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    var rolled by remember { mutableStateOf(false) }
    LaunchedEffect(state.hikeId, state.isLoading) {
        if (!state.isLoading) rolled = true
    }
    val distance by animateFloatAsState(
        targetValue = if (rolled) state.yearDistance else state.previousDistance,
        animationSpec = tween(GameConfig.ODOMETER_MS, easing = FastOutSlowInEasing),
        finishedListener = { haptics.performHapticFeedback(HapticFeedbackType.LongPress) },
        label = "odometerDistance"
    )
    val elevation by animateFloatAsState(
        targetValue = if (rolled) state.yearElevation else state.previousElevation,
        animationSpec = tween(GameConfig.ODOMETER_MS, easing = FastOutSlowInEasing),
        label = "odometerElevation"
    )

    ScreenBackground(
        gradient = listOf(Olive, OliveLight, OliveDeep),
        textureAlpha = 0.12f
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                color = Parchment,
                modifier = Modifier.align(Alignment.Center)
            )
            return@ScreenBackground
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(18.dp))
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.cd_trail_marker),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(84.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.result_title),
                color = Parchment,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Black,
                fontSize = 32.sp,
                letterSpacing = 1.0.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = state.trailName,
                color = RuleLine,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Spacer(Modifier.height(16.dp))
            GlassCard(
                container = ParchmentLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { liveRegion = LiveRegionMode.Polite }
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Odometer(
                        label = stringResource(R.string.stat_year_distance),
                        value = measure(decimalText(distance), distanceUnit(state.imperial)),
                        accent = Olive,
                        modifier = Modifier.weight(1f)
                    )
                    Odometer(
                        label = stringResource(R.string.stat_year_climb),
                        value = measure(integerText(elevation), elevationUnit(state.imperial)),
                        accent = TrailOrange,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(14.dp))
                MonthlyBars(
                    values = state.monthly,
                    highlightMonth = state.currentMonth,
                    labels = monthInitials(),
                    chartHeight = 56.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(14.dp))
            if (state.statCardCount >= 2) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.hikeDistance.isNotEmpty()) {
                        StatCard(
                            label = stringResource(R.string.stat_distance),
                            value = measure(state.hikeDistance, distanceUnit(state.imperial)),
                            accent = Olive,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (state.hikeElevation.isNotEmpty()) {
                        StatCard(
                            label = stringResource(R.string.stat_climb),
                            value = measure(state.hikeElevation, elevationUnit(state.imperial)),
                            accent = TrailOrange,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (state.difficulty > 0) {
                        StatCard(
                            label = stringResource(R.string.stat_boots),
                            value = state.difficulty.toString(),
                            accent = OliveDeep,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.result_hint),
                style = MaterialTheme.typography.labelSmall,
                color = RuleLine,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(10.dp))
            PrimaryButton(
                text = stringResource(R.string.cta_log_again),
                onClick = onLogAgain,
                leadingIcon = ImageVector.vectorResource(R.drawable.ic_add),
                gradient = listOf(TrailOrange, TrailOrangeLight),
                shadowTint = TrailOrange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            SecondaryButton(
                text = stringResource(R.string.cta_view_hike),
                onClick = onViewHike,
                borderColor = Parchment,
                contentColor = Parchment,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onMenu,
                borderColor = Parchment,
                contentColor = Parchment,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Odometer(
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
            fontSize = 32.sp,
            maxLines = 1
        )
        Spacer(Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(2.dp)
                .background(accent)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = OliveDeep,
            fontSize = 10.sp
        )
    }
}
