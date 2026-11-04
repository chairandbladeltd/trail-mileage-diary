package com.trailmileage.diary.presentation.gameover

data class GameOverUiState(
    val isLoading: Boolean = true,
    val hikeId: Long = 0L,
    val trailName: String = "",
    val previousDistance: Float = 0f,
    val yearDistance: Float = 0f,
    val previousElevation: Float = 0f,
    val yearElevation: Float = 0f,
    val monthly: List<Double> = List(12) { 0.0 },
    val currentMonth: Int = 1,
    val hikeDistance: String = "",
    val hikeElevation: String = "",
    val difficulty: Int = 0,
    val imperial: Boolean = false
) {
    val statCardCount: Int
        get() = listOf(
            hikeDistance.isNotEmpty(),
            hikeElevation.isNotEmpty(),
            difficulty > 0
        ).count { it }
}
