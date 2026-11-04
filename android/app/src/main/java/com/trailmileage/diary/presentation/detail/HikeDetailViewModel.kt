package com.trailmileage.diary.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmileage.diary.domain.model.AppSettings
import com.trailmileage.diary.domain.model.GearList
import com.trailmileage.diary.domain.model.Hike
import com.trailmileage.diary.domain.model.UnitSystem
import com.trailmileage.diary.domain.repository.GearRepository
import com.trailmileage.diary.domain.repository.HikeRepository
import com.trailmileage.diary.domain.repository.SettingsRepository
import com.trailmileage.diary.domain.usecase.FormatMeasurementUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HikeDetailViewModel(
    private val hikeRepository: HikeRepository,
    private val gearRepository: GearRepository,
    private val settingsRepository: SettingsRepository,
    private val formatter: FormatMeasurementUseCase
) : ViewModel() {

    private val target = MutableStateFlow(0L)
    private val ready = MutableStateFlow(false)

    private val _state = MutableStateFlow(HikeDetailUiState())
    val state: StateFlow<HikeDetailUiState> = _state.asStateFlow()

    private val channel = Channel<Unit>(Channel.BUFFERED)
    val deleted: Flow<Unit> = channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(
                hikeRepository.hikes,
                gearRepository.lists,
                settingsRepository.settings,
                target,
                ready
            ) { hikes, lists, settings, hikeId, loaded ->
                build(hikes, lists, settings, hikeId, loaded)
            }.collect { next ->
                _state.update { current -> next.copy(confirmDelete = current.confirmDelete) }
            }
        }
        viewModelScope.launch {
            hikeRepository.load()
            gearRepository.load()
            settingsRepository.load()
            ready.value = true
        }
    }

    fun load(hikeId: Long) {
        target.value = hikeId
    }

    fun onDeleteRequest() {
        _state.update { it.copy(confirmDelete = true) }
    }

    fun onDeleteCancel() {
        _state.update { it.copy(confirmDelete = false) }
    }

    fun onDeleteConfirm() {
        val id = _state.value.hikeId
        _state.update { it.copy(confirmDelete = false) }
        viewModelScope.launch {
            hikeRepository.delete(id)
            channel.send(Unit)
        }
    }

    private fun build(
        hikes: List<Hike>,
        lists: List<GearList>,
        settings: AppSettings,
        hikeId: Long,
        loaded: Boolean
    ): HikeDetailUiState {
        val hike = hikes.firstOrNull { it.id == hikeId }
        if (!loaded) return HikeDetailUiState(isLoading = true, hikeId = hikeId)
        if (hike == null) return HikeDetailUiState(isLoading = false, found = false, hikeId = hikeId)
        val gear = lists.firstOrNull { it.id == hike.gearListId }
        return HikeDetailUiState(
            isLoading = false,
            found = true,
            hikeId = hike.id,
            trailName = hike.trailName,
            epochDay = hike.epochDay,
            distanceValue = formatter.distance(hike.distanceKm, settings.units),
            elevationValue = formatter.elevation(hike.elevationM, settings.units),
            durationHours = formatter.hoursPart(hike.durationMin),
            durationMinutes = formatter.minutesPart(hike.durationMin),
            weather = hike.weather,
            difficulty = hike.difficulty,
            photoSlots = hike.photoSlots,
            note = hike.note,
            gearTitle = gear?.title.orEmpty(),
            gearPacked = gear?.packedCount ?: 0,
            gearTotal = gear?.items?.size ?: 0,
            imperial = settings.units == UnitSystem.IMPERIAL
        )
    }
}
