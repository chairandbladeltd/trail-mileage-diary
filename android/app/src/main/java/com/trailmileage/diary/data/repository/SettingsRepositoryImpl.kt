package com.trailmileage.diary.data.repository

import com.trailmileage.diary.data.local.PreferencesStorage
import com.trailmileage.diary.domain.model.AppSettings
import com.trailmileage.diary.domain.model.ThemeMode
import com.trailmileage.diary.domain.model.UnitSystem
import com.trailmileage.diary.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl(
    private val storage: PreferencesStorage
) : SettingsRepository {

    private val state = MutableStateFlow(AppSettings())
    private var loaded = false

    override val settings: StateFlow<AppSettings> = state.asStateFlow()

    override suspend fun load() {
        if (loaded) return
        val restored = withContext(Dispatchers.IO) {
            val units = storage.read(PreferencesStorage.KEY_UNITS)
            val theme = storage.read(PreferencesStorage.KEY_THEME)
            AppSettings(
                units = UnitSystem.entries.firstOrNull { it.name == units } ?: UnitSystem.METRIC,
                theme = ThemeMode.entries.firstOrNull { it.name == theme } ?: ThemeMode.PARCHMENT
            )
        }
        state.value = restored
        loaded = true
    }

    override suspend fun setUnits(units: UnitSystem) {
        state.value = state.value.copy(units = units)
        withContext(Dispatchers.IO) {
            storage.write(PreferencesStorage.KEY_UNITS, units.name)
        }
    }

    override suspend fun setTheme(theme: ThemeMode) {
        state.value = state.value.copy(theme = theme)
        withContext(Dispatchers.IO) {
            storage.write(PreferencesStorage.KEY_THEME, theme.name)
        }
    }
}
