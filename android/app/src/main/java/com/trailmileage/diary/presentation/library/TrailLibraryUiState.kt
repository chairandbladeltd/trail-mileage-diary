package com.trailmileage.diary.presentation.library

data class TrailRowState(
    val id: String,
    val name: String,
    val visitCount: Int,
    val totalValue: String
)

data class TrailLibraryUiState(
    val isLoading: Boolean = true,
    val rows: List<TrailRowState> = emptyList(),
    val imperial: Boolean = false
) {
    val isEmpty: Boolean get() = !isLoading && rows.isEmpty()
}
