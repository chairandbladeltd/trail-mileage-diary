package com.trailmileage.diary.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmileage.diary.domain.model.AppSettings
import com.trailmileage.diary.domain.model.Hike
import com.trailmileage.diary.domain.model.ThemeMode
import com.trailmileage.diary.domain.model.UnitSystem
import com.trailmileage.diary.domain.repository.HikeRepository
import com.trailmileage.diary.domain.repository.SettingsRepository
import com.trailmileage.diary.domain.repository.TrailRepository
import com.trailmileage.diary.domain.usecase.FormatMeasurementUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val hikeRepository: HikeRepository,
    private val trailRepository: TrailRepository,
    private val settingsRepository: SettingsRepository,
    private val formatter: FormatMeasurementUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    private val channel = Channel<Unit>(Channel.BUFFERED)
    val cleared: Flow<Unit> = channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(hikeRepository.hikes, settingsRepository.settings) { hikes, settings ->
                hikes to settings
            }.collect { pair -> applyState(pair.first, pair.second) }
        }
        viewModelScope.launch {
            hikeRepository.load()
            trailRepository.load()
            settingsRepository.load()
        }
    }

    fun onUnitsSelect(index: Int) {
        val units = if (index == 1) UnitSystem.IMPERIAL else UnitSystem.METRIC
        viewModelScope.launch { settingsRepository.setUnits(units) }
    }

    fun onThemeSelect(index: Int) {
        val theme = if (index == 1) ThemeMode.DUSK else ThemeMode.PARCHMENT
        viewModelScope.launch { settingsRepository.setTheme(theme) }
    }

    fun onExportOpen() {
        _state.update { it.copy(showExport = true) }
    }

    fun onExportClose() {
        _state.update { it.copy(showExport = false) }
    }

    fun onClearRequest() {
        _state.update { it.copy(showClear = true) }
    }

    fun onClearCancel() {
        _state.update { it.copy(showClear = false) }
    }

    fun onClearConfirm() {
        _state.update { it.copy(showClear = false) }
        viewModelScope.launch {
            hikeRepository.clear()
            trailRepository.clear()
            channel.send(Unit)
        }
    }

    private fun applyState(hikes: List<Hike>, settings: AppSettings) {
        _state.update { current ->
            current.copy(
                unitsIndex = if (settings.units == UnitSystem.IMPERIAL) 1 else 0,
                themeIndex = if (settings.theme == ThemeMode.DUSK) 1 else 0,
                imperial = settings.units == UnitSystem.IMPERIAL,
                hikeCount = hikes.size,
                exportRows = hikes.map { hike ->
                    ExportRow(
                        epochDay = hike.epochDay,
                        trailName = hike.trailName,
                        distanceValue = formatter.distance(hike.distanceKm, settings.units),
                        elevationValue = formatter.elevation(hike.elevationM, settings.units),
                        durationHours = formatter.hoursPart(hike.durationMin),
                        durationMinutes = formatter.minutesPart(hike.durationMin)
                    )
                }
            )
        }
    }
}
