package com.trailmileage.diary.domain.repository

import com.trailmileage.diary.domain.model.AppSettings
import com.trailmileage.diary.domain.model.ThemeMode
import com.trailmileage.diary.domain.model.UnitSystem
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<AppSettings>
    suspend fun load()
    suspend fun setUnits(units: UnitSystem)
    suspend fun setTheme(theme: ThemeMode)
}
