package com.trailmileage.diary.core.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.trailmileage.diary.data.local.PreferencesStorage
import com.trailmileage.diary.data.repository.GearRepositoryImpl
import com.trailmileage.diary.data.repository.HikeRepositoryImpl
import com.trailmileage.diary.data.repository.SettingsRepositoryImpl
import com.trailmileage.diary.data.repository.TrailRepositoryImpl
import com.trailmileage.diary.domain.usecase.BuildYearSummaryUseCase
import com.trailmileage.diary.domain.usecase.FormatMeasurementUseCase
import com.trailmileage.diary.domain.usecase.SaveHikeUseCase

object ServiceLocator {

    private var factory: ViewModelProvider.Factory? = null

    fun init(context: Context) {
        if (factory != null) return
        val storage = PreferencesStorage(context)
        val hikeRepository = HikeRepositoryImpl(storage)
        val trailRepository = TrailRepositoryImpl(storage)
        val gearRepository = GearRepositoryImpl(storage)
        val settingsRepository = SettingsRepositoryImpl(storage)
        factory = AppViewModelFactory(
            hikeRepository = hikeRepository,
            trailRepository = trailRepository,
            gearRepository = gearRepository,
            settingsRepository = settingsRepository,
            saveHike = SaveHikeUseCase(hikeRepository, trailRepository),
            buildYearSummary = BuildYearSummaryUseCase(),
            formatter = FormatMeasurementUseCase()
        )
    }

    val viewModelFactory: ViewModelProvider.Factory
        get() = checkNotNull(factory)
}
