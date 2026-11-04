package com.trailmileage.diary.presentation.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmileage.diary.R
import com.trailmileage.diary.core.di.ServiceLocator
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.ParchmentDeep
import com.trailmileage.diary.core.theme.ParchmentLight
import com.trailmileage.diary.core.theme.RuleLine
import com.trailmileage.diary.core.theme.TrailOrange
import com.trailmileage.diary.core.ui.HikeCard
import com.trailmileage.diary.core.ui.EmptyState
import com.trailmileage.diary.core.ui.PrimaryButton
import com.trailmileage.diary.core.ui.ScreenBackground
import com.trailmileage.diary.core.ui.StatCard
import com.trailmileage.diary.core.ui.distanceUnit
import com.trailmileage.diary.core.ui.elevationUnit
import com.trailmileage.diary.core.ui.measure
import com.trailmileage.diary.core.ui.metaLine

@Composable
fun MenuRoute(
    onNewHike: () -> Unit,
    onHikeClick: (Long) -> Unit,
    onTrails: () -> Unit,
    onTotals: () -> Unit,
    onGear: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    MenuScreen(
        state = state,
        onNewHike = onNewHike,
        onHikeClick = onHikeClick,
        onTrails = onTrails,
        onTotals = onTotals,
        onGear = onGear,
        onSettings = onSettings
    )
}

@Composable
private fun MenuScreen(
    state: MenuUiState,
    onNewHike: () -> Unit,
    onHikeClick: (Long) -> Unit,
    onTrails: () -> Unit,
    onTotals: () -> Unit,
    onGear: () -> Unit,
    onSettings: () -> Unit
) {
    ScreenBackground(
        gradient = listOf(Color(0xFFF7F3EA), Parchment, ParchmentDeep),
        textureAlpha = 0.14f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            MenuHeaderRail(onSettings = onSettings)
            Box(modifier = Modifier.fillMaxWidth()) {
                Masthead(modifier = Modifier.padding(start = 24.dp, top = 18.dp, end = 120.dp))
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_trail_marker),
                    contentScale = ContentScale.Fit,
                    alpha = 0.95f,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = 12.dp)
                        .size(108.dp)
                        .graphicsLayer { rotationZ = -4f }
                )
            }
            if (state.yearHasData) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_year_distance),
                        value = measure(state.yearDistance, distanceUnit(state.imperial)),
                        accent = Olive,
                        onClick = onTotals,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_year_climb),
                        value = measure(state.yearElevation, elevationUnit(state.imperial)),
                        accent = TrailOrange,
                        onClick = onTotals,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Spacer(Modifier.height(10.dp))
            }
            if (!state.isLoading && !state.isEmpty) {
                Text(
                    text = stringResource(R.string.menu_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = Olive.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 6.dp)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                when {
                    state.isLoading -> CircularProgressIndicator(
                        color = Olive,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    state.isEmpty -> EmptyState(
                        imageRes = R.drawable.sprite_1,
                        title = stringResource(R.string.menu_empty_title),
                        subtitle = stringResource(R.string.menu_empty_subtitle),
                        modifier = Modifier.align(Alignment.TopCenter)
                    )

                    else -> LazyColumn(
                        contentPadding = PaddingValues(
                            start = 24.dp,
                            end = 24.dp,
                            top = 4.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.rows, key = { row -> row.id }) { row ->
                            HikeCard(
                                trailName = row.trailName,
                                distanceText = measure(row.distanceValue, distanceUnit(state.imperial)),
                                metaText = metaLine(
                                    elevation = row.elevationValue,
                                    imperial = state.imperial,
                                    hours = row.durationHours,
                                    minutes = row.durationMinutes
                                ),
                                difficulty = row.difficulty,
                                photoSlots = row.photoSlots,
                                photoLabel = stringResource(R.string.cd_hike_photo),
                                seed = row.id.toInt(),
                                onClick = { onHikeClick(row.id) }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            MenuCtaBlock(onNewHike = onNewHike, onGear = onGear)
            MenuNavRail(onTrails = onTrails, onTotals = onTotals, onGear = onGear)
        }
    }
}

@Composable
private fun MenuHeaderRail(onSettings: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_compass),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onSettings) {
                Text(
                    text = stringResource(R.string.cta_settings),
                    style = MaterialTheme.typography.labelSmall,
                    color = Olive,
                    letterSpacing = 2.0.sp
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                    contentDescription = null,
                    tint = Olive,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(RuleLine)
        )
    }
}

@Composable
private fun Masthead(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        listOf(
            stringResource(R.string.menu_title_one),
            stringResource(R.string.menu_title_two),
            stringResource(R.string.menu_title_three)
        ).forEach { line ->
            Text(
                text = line,
                color = Ink,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Black,
                fontSize = 40.sp,
                lineHeight = 42.sp,
                letterSpacing = (-0.5).sp,
                maxLines = 1
            )
        }
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(2.dp)
                .background(TrailOrange)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.menu_tagline),
            style = MaterialTheme.typography.labelSmall,
            color = Olive,
            fontSize = 12.sp,
            letterSpacing = 1.4.sp,
            maxLines = 2
        )
    }
}

@Composable
private fun MenuCtaBlock(onNewHike: () -> Unit, onGear: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        PrimaryButton(
            text = stringResource(R.string.cta_new_hike),
            onClick = onNewHike,
            leadingIcon = ImageVector.vectorResource(R.drawable.ic_add),
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(12.dp))
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = ParchmentLight,
            border = BorderStroke(1.dp, Olive),
            modifier = Modifier
                .size(60.dp)
                .clickable(onClick = onGear)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_gear_pack),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun MenuNavRail(onTrails: () -> Unit, onTotals: () -> Unit, onGear: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        NavRailButton(stringResource(R.string.cta_trails), onTrails, Modifier.weight(1f))
        NavRailButton(stringResource(R.string.cta_totals), onTotals, Modifier.weight(1f))
        NavRailButton(stringResource(R.string.cta_gear), onGear, Modifier.weight(1f))
    }
}

@Composable
private fun NavRailButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Olive),
        contentPadding = PaddingValues(horizontal = 4.dp),
        modifier = modifier.height(48.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = InkMuted,
            fontSize = 11.sp,
            letterSpacing = 1.4.sp,
            maxLines = 1
        )
    }
}
