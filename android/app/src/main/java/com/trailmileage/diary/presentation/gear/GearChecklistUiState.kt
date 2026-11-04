package com.trailmileage.diary.presentation.gear

data class GearItemState(
    val id: String,
    val name: String,
    val packed: Boolean
)

data class GearListTab(
    val id: String,
    val title: String
)

data class GearChecklistUiState(
    val isLoading: Boolean = true,
    val tabs: List<GearListTab> = emptyList(),
    val selectedId: String = "",
    val items: List<GearItemState> = emptyList(),
    val packedCount: Int = 0,
    val totalCount: Int = 0
)
