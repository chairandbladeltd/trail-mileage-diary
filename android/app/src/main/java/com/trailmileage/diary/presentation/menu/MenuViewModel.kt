package com.trailmileage.diary.presentation.menu

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

class MenuViewModel(
    private val hikeRepository: HikeRepository,
    private val settingsRepository: SettingsRepository,
    private val buildYearSummary: BuildYearSummaryUseCase,
    private val formatter: FormatMeasurementUseCase
) : ViewModel() {

    private val ready = MutableStateFlow(false)
    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                hikeRepository.hikes,
                settingsRepository.settings,
                ready
            ) { hikes, settings, loaded ->
                build(hikes, settings, loaded)
            }.collect { next -> _state.value = next }
        }
        viewModelScope.launch {
            hikeRepository.load()
            settingsRepository.load()
            ready.value = true
        }
    }

    private fun build(hikes: List<Hike>, settings: AppSettings, loaded: Boolean): MenuUiState {
        val imperial = settings.units == UnitSystem.IMPERIAL
        val summary = buildYearSummary(hikes, LocalDate.now().year)
        return MenuUiState(
            isLoading = !loaded,
            rows = hikes.map { hike ->
                HikeRow(
                    id = hike.id,
                    trailName = hike.trailName,
                    distanceValue = formatter.distance(hike.distanceKm, settings.units),
                    elevationValue = formatter.elevation(hike.elevationM, settings.units),
                    durationHours = formatter.hoursPart(hike.durationMin),
                    durationMinutes = formatter.minutesPart(hike.durationMin),
                    difficulty = hike.difficulty,
                    photoSlots = hike.photoSlots
                )
            },
            yearDistance = formatter.distance(summary.distanceKm, settings.units),
            yearElevation = formatter.elevation(summary.elevationM, settings.units),
            yearHasData = summary.distanceKm > 0.0 && summary.elevationM > 0,
            imperial = imperial
        )
    }
}
