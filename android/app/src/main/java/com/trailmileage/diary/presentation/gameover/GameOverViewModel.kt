package com.trailmileage.diary.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmileage.diary.domain.model.AppSettings
import com.trailmileage.diary.domain.model.Hike
import com.trailmileage.diary.domain.model.UnitSystem
import com.trailmileage.diary.domain.repository.HikeRepository
import com.trailmileage.diary.domain.repository.SettingsRepository
import com.trailmileage.diary.domain.usecase.BuildYearSummaryUseCase
import com.trailmileage.diary.domain.usecase.FormatMeasurementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

class GameOverViewModel(
    private val hikeRepository: HikeRepository,
    private val settingsRepository: SettingsRepository,
    private val buildYearSummary: BuildYearSummaryUseCase,
    private val formatter: FormatMeasurementUseCase
) : ViewModel() {

    private val target = MutableStateFlow(0L)
    private val ready = MutableStateFlow(false)

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                hikeRepository.hikes,
                settingsRepository.settings,
                target,
                ready
            ) { hikes, settings, hikeId, loaded ->
                build(hikes, settings, hikeId, loaded)
            }.collect { next -> _state.value = next }
        }
        viewModelScope.launch {
            hikeRepository.load()
            settingsRepository.load()
            ready.value = true
        }
    }

    fun load(hikeId: Long) {
        target.value = hikeId
    }

    private fun build(
        hikes: List<Hike>,
        settings: AppSettings,
        hikeId: Long,
        loaded: Boolean
    ): GameOverUiState {
        val hike = hikes.firstOrNull { it.id == hikeId }
        if (!loaded || hike == null) {
            return GameOverUiState(isLoading = true, hikeId = hikeId)
        }
        val summary = buildYearSummary(hikes, LocalDate.now().year)
        val totalDistance = formatter.distanceValue(summary.distanceKm, settings.units).toFloat()
        val totalElevation = formatter.elevationValue(summary.elevationM, settings.units).toFloat()
        val ownDistance = formatter.distanceValue(hike.distanceKm, settings.units).toFloat()
        val ownElevation = formatter.elevationValue(hike.elevationM, settings.units).toFloat()
        return GameOverUiState(
            isLoading = false,
            hikeId = hikeId,
            trailName = hike.trailName,
            previousDistance = (totalDistance - ownDistance).coerceAtLeast(0f),
            yearDistance = totalDistance,
            previousElevation = (totalElevation - ownElevation).coerceAtLeast(0f),
            yearElevation = totalElevation,
            monthly = summary.monthlyKm,
            currentMonth = hike.month,
            hikeDistance = formatter.distance(hike.distanceKm, settings.units),
            hikeElevation = if (hike.elevationM > 0) {
                formatter.elevation(hike.elevationM, settings.units)
            } else {
                ""
            },
            difficulty = hike.difficulty,
            imperial = settings.units == UnitSystem.IMPERIAL
        )
    }
}
