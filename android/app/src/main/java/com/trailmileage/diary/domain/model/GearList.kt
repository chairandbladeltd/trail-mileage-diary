package com.trailmileage.diary.domain.model

data class GearItem(
    val id: String,
    val name: String,
    val packed: Boolean
)

data class GearList(
    val id: String,
    val title: String,
    val items: List<GearItem>
) {
    val packedCount: Int get() = items.count { it.packed }
}
