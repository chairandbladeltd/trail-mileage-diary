package com.trailmileage.diary.presentation.totals

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

class YearTotalsViewModel(
    private val hikeRepository: HikeRepository,
    private val settingsRepository: SettingsRepository,
    private val buildYearSummary: BuildYearSummaryUseCase,
    private val formatter: FormatMeasurementUseCase
) : ViewModel() {

    private val ready = MutableStateFlow(false)
    private val _state = MutableStateFlow(YearTotalsUiState())
    val state: StateFlow<YearTotalsUiState> = _state.asStateFlow()

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

    private fun build(
        hikes: List<Hike>,
        settings: AppSettings,
        loaded: Boolean
    ): YearTotalsUiState {
        val today = LocalDate.now()
        val summary = buildYearSummary(hikes, today.year)
        return YearTotalsUiState(
            isLoading = !loaded,
            year = today.year,
            distance = formatter.distanceValue(summary.distanceKm, settings.units).toFloat(),
            elevation = formatter.elevationValue(summary.elevationM, settings.units).toFloat(),
            monthly = summary.monthlyKm,
            currentMonth = today.monthValue,
            hikeCount = summary.hikeCount,
            longestValue = formatter.distance(summary.longestKm, settings.units),
            biggestClimbValue = formatter.elevation(summary.biggestClimbM, settings.units),
            imperial = settings.units == UnitSystem.IMPERIAL
        )
    }
}
