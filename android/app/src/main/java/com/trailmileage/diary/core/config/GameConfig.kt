package com.trailmileage.diary.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val SCREEN_TRANSITION_MS = 240
    const val ODOMETER_MS = 800
    const val BOOT_STAGGER_MS = 100
    const val BOOT_STAMP_MS = 180
    const val CHECK_DRAW_MS = 180
    const val BAR_GROW_MS = 400
    const val VALUE_FADE_MS = 120
    const val PRESS_DAMPING = 0.55f
    const val PRESS_STIFFNESS = 900f

    const val DISTANCE_STEP = 0.5
    const val DISTANCE_MAX = 99.5
    const val ELEVATION_STEP = 25
    const val ELEVATION_MAX = 5000
    const val DURATION_STEP = 10
    const val DURATION_MAX = 1440

    const val PHOTO_SLOTS = 4
    const val MAX_DIFFICULTY = 5
    const val MENU_TRAIL_CHIPS = 4

    const val DEFAULT_DISTANCE_KM = 8.0
    const val DEFAULT_ELEVATION_M = 250
    const val DEFAULT_DURATION_MIN = 120
    const val DEFAULT_DIFFICULTY = 3

    const val MI_PER_KM = 0.621371
    const val FT_PER_M = 3.28084
}
