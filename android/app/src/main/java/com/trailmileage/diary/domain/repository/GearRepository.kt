package com.trailmileage.diary.domain.repository

import com.trailmileage.diary.domain.model.GearList
import kotlinx.coroutines.flow.StateFlow

interface GearRepository {
    val lists: StateFlow<List<GearList>>
    val draftListId: StateFlow<String>
    suspend fun load()
    suspend fun toggleItem(listId: String, itemId: String)
    suspend fun resetList(listId: String)
    fun selectForDraft(listId: String)
}
