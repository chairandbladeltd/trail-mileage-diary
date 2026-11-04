package com.trailmileage.diary.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val trailName: String = "", val requestId: Long = 0L) : Screen
    data class GameOver(val hikeId: Long) : Screen
    data object TrailLibrary : Screen
    data object YearTotals : Screen
    data class GearChecklist(val pickMode: Boolean = false) : Screen
    data class HikeDetail(val hikeId: Long) : Screen
    data object Settings : Screen
}
