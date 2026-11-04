package com.trailmileage.diary.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmileage.diary.core.config.GameConfig
import com.trailmileage.diary.domain.model.GearList
import com.trailmileage.diary.domain.model.AppSettings
import com.trailmileage.diary.domain.model.Trail
import com.trailmileage.diary.domain.model.HikeDraft
import com.trailmileage.diary.domain.model.Weather
import com.trailmileage.diary.domain.repository.GearRepository
import com.trailmileage.diary.domain.repository.SettingsRepository
import com.trailmileage.diary.domain.repository.TrailRepository
import com.trailmileage.diary.domain.usecase.FormatMeasurementUseCase
import com.trailmileage.diary.domain.usecase.SaveHikeUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class GameViewModel(
    private val trailRepository: TrailRepository,
    private val gearRepository: GearRepository,
    private val settingsRepository: SettingsRepository,
    private val saveHike: SaveHikeUseCase,
    private val formatter: FormatMeasurementUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState().refreshed())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val channel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = channel.receiveAsFlow()

    private var lastRequestId = -1L

    init {
        viewModelScope.launch {
            trailRepository.load()
            gearRepository.load()
            settingsRepository.load()
        }
        viewModelScope.launch {
            combine(
                trailRepository.trails,
                gearRepository.lists,
                gearRepository.draftListId,
                settingsRepository.settings
            ) { trails, lists, draftId, settings ->
                Reference(trails, lists, draftId, settings)
            }.collect { reference -> applyReference(reference) }
        }
    }

    private fun applyReference(reference: Reference) {
        _state.update { current ->
            val suggestions = reference.trails.take(GameConfig.MENU_TRAIL_CHIPS)
                .map { TrailSuggestion(it.id, it.name) }
            val filled = if (current.autoFill && current.trailName.isBlank()) {
                suggestions.firstOrNull()?.name ?: current.trailName
            } else {
                current.trailName
            }
            current.copy(
                suggestions = suggestions,
                trailName = filled,
                gearListId = reference.draftId,
                gearListTitle = reference.lists
                    .firstOrNull { it.id == reference.draftId }?.title.orEmpty(),
                units = reference.settings.units
            ).refreshed()
        }
    }

    fun startEntry(trailName: String, requestId: Long) {
        if (requestId == lastRequestId) return
        lastRequestId = requestId
        gearRepository.selectForDraft("")
        _state.update { current ->
            GameUiState(
                trailName = trailName,
                autoFill = trailName.isBlank(),
                suggestions = current.suggestions,
                units = current.units
            ).refreshed()
        }
    }

    fun onTrailNameChange(value: String) {
        _state.update { it.copy(trailName = value, autoFill = false) }
    }

    fun onSuggestionPick(name: String) {
        _state.update { it.copy(trailName = name, autoFill = false) }
    }

    fun onDistanceStep(up: Boolean) {
        _state.update { current ->
            val next = if (up) {
                current.distanceKm + GameConfig.DISTANCE_STEP
            } else {
                current.distanceKm - GameConfig.DISTANCE_STEP
            }
            current.copy(distanceKm = next.coerceIn(0.0, GameConfig.DISTANCE_MAX)).refreshed()
        }
    }

    fun onElevationStep(up: Boolean) {
        _state.update { current ->
            val next = if (up) {
                current.elevationM + GameConfig.ELEVATION_STEP
            } else {
                current.elevationM - GameConfig.ELEVATION_STEP
            }
            current.copy(elevationM = next.coerceIn(0, GameConfig.ELEVATION_MAX)).refreshed()
        }
    }

    fun onDurationStep(up: Boolean) {
        _state.update { current ->
            val next = if (up) {
                current.durationMin + GameConfig.DURATION_STEP
            } else {
                current.durationMin - GameConfig.DURATION_STEP
            }
            current.copy(durationMin = next.coerceIn(0, GameConfig.DURATION_MAX)).refreshed()
        }
    }

    fun onWeatherSelect(weather: Weather) {
        _state.update { it.copy(weather = weather) }
    }

    fun onDifficultySelect(value: Int) {
        _state.update { it.copy(difficulty = value.coerceIn(1, GameConfig.MAX_DIFFICULTY)) }
    }

    fun onPhotoToggle(slot: Int) {
        _state.update { current ->
            val slots = current.photoSlots.toMutableList()
            if (slot !in slots.indices) return@update current
            slots[slot] = (slots[slot] + 1) % 3
            current.copy(photoSlots = slots.toList())
        }
    }

    fun onNoteChange(value: String) {
        _state.update { it.copy(note = value) }
    }

    fun onSave() {
        val current = _state.value
        if (!current.canSave || current.isSaving) return
        _state.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val outcome = runCatching {
                saveHike(
                    HikeDraft(
                        trailName = current.trailName,
                        distanceKm = current.distanceKm,
                        elevationM = current.elevationM,
                        durationMin = current.durationMin,
                        weather = current.weather,
                        difficulty = current.difficulty,
                        photoSlots = current.photoSlots,
                        gearListId = current.gearListId,
                        note = current.note
                    ),
                    LocalDate.now().toEpochDay()
                )
            }
            _state.update { it.copy(isSaving = false) }
            outcome
                .onSuccess { id -> channel.send(GameEvent.Saved(id)) }
                .onFailure { channel.send(GameEvent.SaveFailed) }
        }
    }

    private fun GameUiState.refreshed(): GameUiState = copy(
        distanceValue = formatter.distance(distanceKm, units),
        elevationValue = formatter.elevation(elevationM, units),
        durationHours = formatter.hoursPart(durationMin),
        durationMinutes = formatter.minutesPart(durationMin)
    )

    private data class Reference(
        val trails: List<Trail>,
        val lists: List<GearList>,
        val draftId: String,
        val settings: AppSettings
    )
}
