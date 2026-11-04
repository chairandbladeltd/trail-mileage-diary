package com.trailmileage.diary.presentation.totals

data class YearTotalsUiState(
    val isLoading: Boolean = true,
    val year: Int = 0,
    val distance: Float = 0f,
    val elevation: Float = 0f,
    val monthly: List<Double> = List(12) { 0.0 },
    val currentMonth: Int = 1,
    val hikeCount: Int = 0,
    val longestValue: String = "",
    val biggestClimbValue: String = "",
    val imperial: Boolean = false
) {
    val hasData: Boolean get() = hikeCount > 0
}
