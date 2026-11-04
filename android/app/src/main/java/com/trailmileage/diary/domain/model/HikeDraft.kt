package com.trailmileage.diary.domain.model

data class HikeDraft(
    val trailName: String,
    val distanceKm: Double,
    val elevationM: Int,
    val durationMin: Int,
    val weather: Weather,
    val difficulty: Int,
    val photoSlots: List<Int>,
    val gearListId: String,
    val note: String
)
