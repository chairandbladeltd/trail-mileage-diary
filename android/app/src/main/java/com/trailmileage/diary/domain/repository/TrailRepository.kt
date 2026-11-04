package com.trailmileage.diary.domain.repository

import com.trailmileage.diary.domain.model.Trail
import kotlinx.coroutines.flow.StateFlow

interface TrailRepository {
    val trails: StateFlow<List<Trail>>
    suspend fun load()
    suspend fun register(name: String, distanceKm: Double, epochDay: Long)
    suspend fun clear()
}
