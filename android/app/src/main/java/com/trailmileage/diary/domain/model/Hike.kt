package com.trailmileage.diary.domain.model

import java.time.LocalDate

data class Hike(
    val id: Long,
    val trailName: String,
    val distanceKm: Double,
    val elevationM: Int,
    val durationMin: Int,
    val weather: Weather,
    val difficulty: Int,
    val photoSlots: List<Int>,
    val gearListId: String,
    val note: String,
    val epochDay: Long
) {
    val date: LocalDate get() = LocalDate.ofEpochDay(epochDay)
    val year: Int get() = date.year
    val month: Int get() = date.monthValue
}
