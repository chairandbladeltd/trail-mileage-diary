package com.trailmileage.diary.domain.model

data class Trail(
    val id: String,
    val name: String,
    val visitCount: Int,
    val totalKm: Double,
    val lastHikedEpochDay: Long
)
