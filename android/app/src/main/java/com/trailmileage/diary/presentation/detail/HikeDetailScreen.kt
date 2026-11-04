package com.trailmileage.diary.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.trailmileage.diary.core.theme.TrailOrange
import com.trailmileage.diary.core.ui.BootRating
import com.trailmileage.diary.core.ui.DistanceBadge
import com.trailmileage.diary.core.ui.GlassCard
import com.trailmileage.diary.core.ui.ScreenBackground
import com.trailmileage.diary.core.ui.ScreenHeader
import com.trailmileage.diary.core.ui.SectionHeader
import com.trailmileage.diary.core.ui.TornPaperEdge
import com.trailmileage.diary.core.ui.weatherIconRes
import com.trailmileage.diary.core.ui.dateLabel
import com.trailmileage.diary.core.ui.distanceUnit
import com.trailmileage.diary.core.ui.durationLabel
import com.trailmileage.diary.core.ui.elevationUnit
import com.trailmileage.diary.core.ui.measure
import com.trailmileage.diary.domain.model.Weather
import com.trailmileage.diary.presentation.dialog.DeleteHikeDialog

@Composable
fun HikeDetailRoute(
    hikeId: Long,
    onBack: () -> Unit
) {
    val detailViewModel: HikeDetailViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by detailViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(hikeId) { detailViewModel.load(hikeId) }
    LaunchedEffect(Unit) {
        detailViewModel.deleted.collect { onBack() }
    }
    HikeDetailScreen(
        state = state,
        onBack = onBack,
        onDeleteRequest = detailViewModel::onDeleteRequest,
        onDeleteCancel = detailViewModel::onDeleteCancel,
        onDeleteConfirm = detailViewModel::onDeleteConfirm
    )
}

@Composable
private fun HikeDetailScreen(
    state: HikeDetailUiState,
    onBack: () -> Unit,
    onDeleteRequest: () -> Unit,
    onDeleteCancel: () -> Unit,
    onDeleteConfirm: () -> Unit
) {
    if (state.confirmDelete) {
        DeleteHikeDialog(
            trailName = state.trailName,
            onConfirm = onDeleteConfirm,
            onDismiss = onDeleteCancel
        )
    }
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
                title = stringResource(R.string.detail_title),
                backLabel = stringResource(R.string.cd_back),
                onBack = onBack,
                trailing = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(onClick = onDeleteRequest)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.cd_delete),
                            tint = TrailOrange,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
            when {
                state.isLoading -> Box(modifier = Modifier.weight(1f)) {
                    CircularProgressIndicator(
                        color = Olive,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                !state.found -> Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.detail_missing),
                        style = MaterialTheme.typography.bodyLarge,
                        color = InkMuted,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> DetailBody(state = state)
            }
        }
    }
}

@Composable
private fun ColumnScope.DetailBody(state: HikeDetailUiState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        PhotoStrip(
            slots = state.photoSlots,
            distanceText = measure(state.distanceValue, distanceUnit(state.imperial))
        )
        Column {
            Text(
                text = state.trailName,
                color = Ink,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = dateLabel(state.epochDay),
                style = MaterialTheme.typography.labelSmall,
                color = InkMuted
            )
        }
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Readout(
                    label = stringResource(R.string.field_elevation),
                    value = measure(state.elevationValue, elevationUnit(state.imperial)),
                    modifier = Modifier.weight(1f)
                )
                Readout(
                    label = stringResource(R.string.field_duration),
                    value = durationLabel(state.durationHours, state.durationMinutes),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(weatherIconRes(state.weather)),
                    contentDescription = stringResource(R.string.cd_weather),
                    tint = Olive,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.size(10.dp))
                Text(
                    text = weatherLabel(state.weather),
                    style = MaterialTheme.typography.titleSmall,
                    color = Ink
                )
                Spacer(Modifier.weight(1f))
                BootRating(value = state.difficulty, bootSize = 22.dp, spacing = 4.dp)
            }
        }
        if (state.note.isNotEmpty()) {
            Column {
                SectionHeader(title = stringResource(R.string.section_notes))
                Spacer(Modifier.height(10.dp))
                GlassCard(container = ParchmentLight, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = state.note,
                        style = MaterialTheme.typography.bodyLarge,
                        color = InkMuted
                    )
                }
            }
        }
        if (state.gearTitle.isNotEmpty()) {
            Column {
                SectionHeader(title = stringResource(R.string.section_gear))
                Spacer(Modifier.height(10.dp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.icon_pause),
                            contentDescription = stringResource(R.string.cd_gear_pack),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(Modifier.size(12.dp))
                        Text(
                            text = state.gearTitle,
                            style = MaterialTheme.typography.titleSmall,
                            color = Ink,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(
                                R.string.gear_packed,
                                state.gearPacked,
                                state.gearTotal
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = Olive
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoStrip(slots: List<Int>, distanceText: String) {
    val photos = slots.filter { it > 0 }.ifEmpty { listOf(1, 2) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ParchmentDeep)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 24.dp, end = 110.dp)
        ) {
            photos.take(2).forEach { slot ->
                Image(
                    painter = painterResource(
                        when (slot) {
                            1 -> R.drawable.sprite_2
                            2 -> R.drawable.sprite_3
                            else -> R.drawable.sprite_4
                        }
                    ),
                    contentDescription = stringResource(R.string.cd_hike_photo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(62.dp)
                )
            }
        }
        TornPaperEdge(
            color = Parchment,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(10.dp)
        )
        DistanceBadge(
            text = distanceText,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 8.dp)
        )
    }
}

@Composable
private fun Readout(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = InkMuted,
            fontSize = 10.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = Ink
        )
    }
}

@Composable
private fun weatherLabel(weather: Weather): String = stringResource(
    when (weather) {
        Weather.SUN -> R.string.weather_sun
        Weather.CLOUD -> R.string.weather_cloud
        Weather.RAIN -> R.string.weather_rain
        Weather.WIND -> R.string.weather_wind
        Weather.SNOW -> R.string.weather_snow
    }
)
