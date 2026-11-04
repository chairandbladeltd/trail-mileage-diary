package com.trailmileage.diary.domain.model

data class YearSummary(
    val year: Int,
    val distanceKm: Double,
    val elevationM: Int,
    val hikeCount: Int,
    val longestKm: Double,
    val biggestClimbM: Int,
    val monthlyKm: List<Double>
) {
    companion object {
        fun empty(year: Int): YearSummary = YearSummary(
            year = year,
            distanceKm = 0.0,
            elevationM = 0,
            hikeCount = 0,
            longestKm = 0.0,
            biggestClimbM = 0,
            monthlyKm = List(12) { 0.0 }
        )
    }
}
