package com.trailmileage.diary.domain.repository

import com.trailmileage.diary.domain.model.Hike
import kotlinx.coroutines.flow.StateFlow

interface HikeRepository {
    val hikes: StateFlow<List<Hike>>
    suspend fun load()
    suspend fun add(hike: Hike): Long
    suspend fun delete(id: Long)
    suspend fun clear()
    fun nextId(): Long
}
