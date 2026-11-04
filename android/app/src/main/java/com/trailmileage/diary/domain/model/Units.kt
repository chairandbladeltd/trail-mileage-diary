package com.trailmileage.diary.domain.model

enum class UnitSystem {
    METRIC,
    IMPERIAL
}

enum class ThemeMode {
    PARCHMENT,
    DUSK
}

data class AppSettings(
    val units: UnitSystem = UnitSystem.METRIC,
    val theme: ThemeMode = ThemeMode.PARCHMENT
)
