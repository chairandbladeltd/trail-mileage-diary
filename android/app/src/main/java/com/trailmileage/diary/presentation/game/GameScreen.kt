package com.trailmileage.diary.presentation.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
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
import com.trailmileage.diary.core.theme.TrailOrangeLight
import com.trailmileage.diary.core.ui.BootRating
import com.trailmileage.diary.core.ui.GlassCard
import com.trailmileage.diary.core.ui.PrimaryButton
import com.trailmileage.diary.core.ui.ScreenBackground
import com.trailmileage.diary.core.ui.ScreenHeader
import com.trailmileage.diary.core.ui.SectionHeader
import com.trailmileage.diary.core.ui.StepperRow
import com.trailmileage.diary.core.ui.WeatherChip
import com.trailmileage.diary.core.ui.weatherIconRes
import com.trailmileage.diary.core.ui.distanceUnit
import com.trailmileage.diary.core.ui.durationLabel
import com.trailmileage.diary.core.ui.elevationUnit
import com.trailmileage.diary.core.ui.measure
import com.trailmileage.diary.domain.model.Weather

@Composable
fun GameRoute(
    trailName: String,
    requestId: Long,
    onBack: () -> Unit,
    onSaved: (Long) -> Unit,
    onOpenLibrary: () -> Unit,
    onPickGear: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val failureMessage = stringResource(R.string.msg_save_failed)

    LaunchedEffect(requestId) {
        gameViewModel.startEntry(trailName, requestId)
    }
    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.Saved -> onSaved(event.hikeId)
                is GameEvent.SaveFailed -> snackbarHostState.showSnackbar(failureMessage)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GameScreen(
            state = state,
            onBack = onBack,
            onTrailNameChange = gameViewModel::onTrailNameChange,
            onSuggestionPick = gameViewModel::onSuggestionPick,
            onDistanceStep = gameViewModel::onDistanceStep,
            onElevationStep = gameViewModel::onElevationStep,
            onDurationStep = gameViewModel::onDurationStep,
            onWeatherSelect = gameViewModel::onWeatherSelect,
            onDifficultySelect = gameViewModel::onDifficultySelect,
            onPhotoToggle = gameViewModel::onPhotoToggle,
            onNoteChange = gameViewModel::onNoteChange,
            onOpenLibrary = onOpenLibrary,
            onPickGear = onPickGear,
            onSave = gameViewModel::onSave
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
        )
    }
}

@Composable
private fun GameScreen(
    state: GameUiState,
    onBack: () -> Unit,
    onTrailNameChange: (String) -> Unit,
    onSuggestionPick: (String) -> Unit,
    onDistanceStep: (Boolean) -> Unit,
    onElevationStep: (Boolean) -> Unit,
    onDurationStep: (Boolean) -> Unit,
    onWeatherSelect: (Weather) -> Unit,
    onDifficultySelect: (Int) -> Unit,
    onPhotoToggle: (Int) -> Unit,
    onNoteChange: (String) -> Unit,
    onOpenLibrary: () -> Unit,
    onPickGear: () -> Unit,
    onSave: () -> Unit
) {
    ScreenBackground(
        gradient = listOf(Parchment, androidx.compose.ui.graphics.Color(0xFFEDE6D6)),
        textureAlpha = 0.10f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.game_title),
                backLabel = stringResource(R.string.cd_back),
                onBack = onBack,
                trailing = {
                    Text(
                        text = stringResource(R.string.game_step),
                        style = MaterialTheme.typography.labelSmall,
                        color = Olive
                    )
                }
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                TrailField(
                    value = state.trailName,
                    suggestions = state.suggestions,
                    onValueChange = onTrailNameChange,
                    onSuggestionPick = onSuggestionPick,
                    onOpenLibrary = onOpenLibrary
                )
                StepperRow(
                    label = stringResource(R.string.field_distance),
                    value = state.distanceValue,
                    unit = distanceUnit(state.imperial),
                    accent = Olive,
                    minusEnabled = state.minusDistanceEnabled,
                    plusEnabled = state.plusDistanceEnabled,
                    stateLabel = measure(state.distanceValue, distanceUnit(state.imperial)),
                    onMinus = { onDistanceStep(false) },
                    onPlus = { onDistanceStep(true) }
                )
                StepperRow(
                    label = stringResource(R.string.field_elevation),
                    value = state.elevationValue,
                    unit = elevationUnit(state.imperial),
                    accent = TrailOrange,
                    minusEnabled = state.minusElevationEnabled,
                    plusEnabled = state.plusElevationEnabled,
                    stateLabel = measure(state.elevationValue, elevationUnit(state.imperial)),
                    onMinus = { onElevationStep(false) },
                    onPlus = { onElevationStep(true) }
                )
                StepperRow(
                    label = stringResource(R.string.field_duration),
                    value = durationLabel(state.durationHours, state.durationMinutes),
                    unit = stringResource(R.string.unit_time),
                    accent = Ink,
                    minusEnabled = state.minusDurationEnabled,
                    plusEnabled = state.plusDurationEnabled,
                    stateLabel = durationLabel(state.durationHours, state.durationMinutes),
                    onMinus = { onDurationStep(false) },
                    onPlus = { onDurationStep(true) }
                )
                WeatherSection(selected = state.weather, onSelect = onWeatherSelect)
                DifficultySection(value = state.difficulty, onSelect = onDifficultySelect)
                PhotoSection(slots = state.photoSlots, onToggle = onPhotoToggle)
                GearAttachRow(title = state.gearListTitle, onClick = onPickGear)
                NoteField(value = state.note, onValueChange = onNoteChange)
            }
            LiveStatStrip(state = state)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                PrimaryButton(
                    text = stringResource(R.string.cta_save_hike),
                    onClick = onSave,
                    enabled = state.canSave,
                    loading = state.isSaving,
                    leadingIcon = ImageVector.vectorResource(R.drawable.ic_check),
                    gradient = listOf(TrailOrange, TrailOrangeLight),
                    shadowTint = TrailOrange,
                    stateLabel = stringResource(
                        if (state.canSave) R.string.state_ready else R.string.state_incomplete
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun TrailField(
    value: String,
    suggestions: List<TrailSuggestion>,
    onValueChange: (String) -> Unit,
    onSuggestionPick: (String) -> Unit,
    onOpenLibrary: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            label = { Text(stringResource(R.string.field_trail_name)) },
            placeholder = { Text(stringResource(R.string.field_trail_hint)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Olive,
                unfocusedBorderColor = RuleLine,
                focusedContainerColor = ParchmentLight,
                unfocusedContainerColor = ParchmentLight,
                focusedLabelColor = Olive,
                cursorColor = Olive
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            suggestions.forEach { suggestion ->
                FilterChip(
                    selected = suggestion.name == value,
                    onClick = { onSuggestionPick(suggestion.name) },
                    shape = RoundedCornerShape(14.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = ParchmentLight,
                        selectedContainerColor = Olive,
                        labelColor = InkMuted,
                        selectedLabelColor = Parchment
                    ),
                    label = {
                        Text(
                            text = suggestion.name,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    },
                    modifier = Modifier.height(40.dp)
                )
            }
        }
        Spacer(Modifier.height(2.dp))
        TextButton(onClick = onOpenLibrary) {
            Text(
                text = stringResource(R.string.cta_open_library),
                style = MaterialTheme.typography.labelSmall,
                color = TrailOrange,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun WeatherSection(selected: Weather, onSelect: (Weather) -> Unit) {
    val labels = listOf(
        Weather.SUN to stringResource(R.string.weather_sun),
        Weather.CLOUD to stringResource(R.string.weather_cloud),
        Weather.RAIN to stringResource(R.string.weather_rain),
        Weather.WIND to stringResource(R.string.weather_wind),
        Weather.SNOW to stringResource(R.string.weather_snow)
    )
    Column {
        SectionHeader(title = stringResource(R.string.section_weather))
        Spacer(Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            labels.forEach { entry ->
                WeatherChip(
                    label = entry.second,
                    iconRes = weatherIconRes(entry.first),
                    selected = entry.first == selected,
                    stateLabel = entry.second,
                    onClick = { onSelect(entry.first) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DifficultySection(value: Int, onSelect: (Int) -> Unit) {
    Column {
        SectionHeader(title = stringResource(R.string.section_difficulty))
        Spacer(Modifier.height(10.dp))
        BootRating(
            value = value,
            interactive = true,
            onChange = onSelect,
            stateLabel = stringResource(R.string.state_difficulty, value)
        )
    }
}

@Composable
private fun PhotoSection(slots: List<Int>, onToggle: (Int) -> Unit) {
    Column {
        SectionHeader(title = stringResource(R.string.section_photos))
        Spacer(Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            slots.forEachIndexed { index, slot ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(76.dp)
                        .background(ParchmentDeep, RoundedCornerShape(12.dp))
                        .border(
                            BorderStroke(if (slot == 0) 1.dp else 2.dp, if (slot == 0) RuleLine else Olive),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onToggle(index) }
                ) {
                    if (slot == 0) {
                        Text(
                            text = stringResource(R.string.photo_add),
                            style = MaterialTheme.typography.labelSmall,
                            color = InkMuted,
                            fontSize = 10.sp
                        )
                    } else {
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
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GearAttachRow(title: String, onClick: () -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_gear_pack),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.gear_attach),
                    style = MaterialTheme.typography.titleSmall,
                    color = Ink
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = title.ifEmpty { stringResource(R.string.value_none) },
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMuted
                )
            }
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = Olive,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun NoteField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        minLines = 3,
        maxLines = 5,
        shape = RoundedCornerShape(14.dp),
        label = { Text(stringResource(R.string.field_notes)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Olive,
            unfocusedBorderColor = RuleLine,
            focusedContainerColor = ParchmentLight,
            unfocusedContainerColor = ParchmentLight,
            focusedLabelColor = Olive,
            cursorColor = Olive
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun LiveStatStrip(state: GameUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(RuleLine)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(ParchmentDeep.copy(alpha = 0.9f))
                .semantics { liveRegion = LiveRegionMode.Polite }
        ) {
            LiveReadout(
                label = stringResource(R.string.field_distance),
                value = measure(state.distanceValue, distanceUnit(state.imperial))
            )
            StripDivider()
            LiveReadout(
                label = stringResource(R.string.field_elevation),
                value = measure(state.elevationValue, elevationUnit(state.imperial))
            )
            StripDivider()
            LiveReadout(
                label = stringResource(R.string.field_duration),
                value = durationLabel(state.durationHours, state.durationMinutes)
            )
        }
    }
}

@Composable
private fun LiveReadout(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = Ink,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = InkMuted,
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun StripDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(RuleLine)
    )
}
