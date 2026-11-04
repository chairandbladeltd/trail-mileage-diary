package com.trailmileage.diary.presentation.menu

data class HikeRow(
    val id: Long,
    val trailName: String,
    val distanceValue: String,
    val elevationValue: String,
    val durationHours: Int,
    val durationMinutes: Int,
    val difficulty: Int,
    val photoSlots: List<Int>
)

data class MenuUiState(
    val isLoading: Boolean = true,
    val rows: List<HikeRow> = emptyList(),
    val yearDistance: String = "0.0",
    val yearElevation: String = "0",
    val yearHasData: Boolean = false,
    val imperial: Boolean = false
) {
    val isEmpty: Boolean get() = !isLoading && rows.isEmpty()
}
