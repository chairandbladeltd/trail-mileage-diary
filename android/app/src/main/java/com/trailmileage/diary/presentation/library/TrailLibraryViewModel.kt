package com.trailmileage.diary.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmileage.diary.domain.model.AppSettings
import com.trailmileage.diary.domain.model.Trail
import com.trailmileage.diary.domain.model.UnitSystem
import com.trailmileage.diary.domain.repository.SettingsRepository
import com.trailmileage.diary.domain.repository.TrailRepository
import com.trailmileage.diary.domain.usecase.FormatMeasurementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class TrailLibraryViewModel(
    private val trailRepository: TrailRepository,
    private val settingsRepository: SettingsRepository,
    private val formatter: FormatMeasurementUseCase
) : ViewModel() {

    private val ready = MutableStateFlow(false)
    private val _state = MutableStateFlow(TrailLibraryUiState())
    val state: StateFlow<TrailLibraryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                trailRepository.trails,
                settingsRepository.settings,
                ready
            ) { trails, settings, loaded ->
                build(trails, settings, loaded)
            }.collect { next -> _state.value = next }
        }
        viewModelScope.launch {
            trailRepository.load()
            settingsRepository.load()
            ready.value = true
        }
    }

    private fun build(
        trails: List<Trail>,
        settings: AppSettings,
        loaded: Boolean
    ): TrailLibraryUiState = TrailLibraryUiState(
        isLoading = !loaded,
        rows = trails.map { trail ->
            TrailRowState(
                id = trail.id,
                name = trail.name,
                visitCount = trail.visitCount,
                totalValue = formatter.distance(trail.totalKm, settings.units)
            )
        },
        imperial = settings.units == UnitSystem.IMPERIAL
    )
}
