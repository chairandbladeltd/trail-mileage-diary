package com.trailmileage.diary.presentation.detail

import com.trailmileage.diary.domain.model.Weather

data class HikeDetailUiState(
    val isLoading: Boolean = true,
    val found: Boolean = false,
    val hikeId: Long = 0L,
    val trailName: String = "",
    val epochDay: Long = 0L,
    val distanceValue: String = "",
    val elevationValue: String = "",
    val durationHours: Int = 0,
    val durationMinutes: Int = 0,
    val weather: Weather = Weather.SUN,
    val difficulty: Int = 0,
    val photoSlots: List<Int> = emptyList(),
    val note: String = "",
    val gearTitle: String = "",
    val gearPacked: Int = 0,
    val gearTotal: Int = 0,
    val imperial: Boolean = false,
    val confirmDelete: Boolean = false
)
