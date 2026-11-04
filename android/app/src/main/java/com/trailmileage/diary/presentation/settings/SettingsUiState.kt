package com.trailmileage.diary.presentation.settings

data class ExportRow(
    val epochDay: Long,
    val trailName: String,
    val distanceValue: String,
    val elevationValue: String,
    val durationHours: Int,
    val durationMinutes: Int
)

data class SettingsUiState(
    val unitsIndex: Int = 0,
    val themeIndex: Int = 0,
    val imperial: Boolean = false,
    val hikeCount: Int = 0,
    val exportRows: List<ExportRow> = emptyList(),
    val showExport: Boolean = false,
    val showClear: Boolean = false
)
