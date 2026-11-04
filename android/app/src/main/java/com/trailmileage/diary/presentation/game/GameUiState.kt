package com.trailmileage.diary.presentation.game

import com.trailmileage.diary.core.config.GameConfig
import com.trailmileage.diary.domain.model.UnitSystem
import com.trailmileage.diary.domain.model.Weather

data class TrailSuggestion(
    val id: String,
    val name: String
)

data class GameUiState(
    val trailName: String = "",
    val autoFill: Boolean = true,
    val distanceKm: Double = GameConfig.DEFAULT_DISTANCE_KM,
    val elevationM: Int = GameConfig.DEFAULT_ELEVATION_M,
    val durationMin: Int = GameConfig.DEFAULT_DURATION_MIN,
    val weather: Weather = Weather.SUN,
    val difficulty: Int = GameConfig.DEFAULT_DIFFICULTY,
    val photoSlots: List<Int> = List(GameConfig.PHOTO_SLOTS) { 0 },
    val gearListId: String = "",
    val gearListTitle: String = "",
    val note: String = "",
    val isSaving: Boolean = false,
    val suggestions: List<TrailSuggestion> = emptyList(),
    val units: UnitSystem = UnitSystem.METRIC,
    val distanceValue: String = "",
    val elevationValue: String = "",
    val durationHours: Int = 0,
    val durationMinutes: Int = 0
) {
    val imperial: Boolean get() = units == UnitSystem.IMPERIAL
    val canSave: Boolean get() = trailName.isNotBlank() && distanceKm > 0.0
    val minusDistanceEnabled: Boolean get() = distanceKm > 0.0
    val plusDistanceEnabled: Boolean get() = distanceKm < GameConfig.DISTANCE_MAX
    val minusElevationEnabled: Boolean get() = elevationM > 0
    val plusElevationEnabled: Boolean get() = elevationM < GameConfig.ELEVATION_MAX
    val minusDurationEnabled: Boolean get() = durationMin > 0
    val plusDurationEnabled: Boolean get() = durationMin < GameConfig.DURATION_MAX
}

sealed interface GameEvent {
    data class Saved(val hikeId: Long) : GameEvent
    data object SaveFailed : GameEvent
}
