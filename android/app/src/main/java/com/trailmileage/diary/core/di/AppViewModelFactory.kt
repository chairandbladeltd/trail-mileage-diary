package com.trailmileage.diary.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trailmileage.diary.domain.repository.GearRepository
import com.trailmileage.diary.domain.repository.HikeRepository
import com.trailmileage.diary.domain.repository.SettingsRepository
import com.trailmileage.diary.domain.repository.TrailRepository
import com.trailmileage.diary.domain.usecase.BuildYearSummaryUseCase
import com.trailmileage.diary.domain.usecase.FormatMeasurementUseCase
import com.trailmileage.diary.domain.usecase.SaveHikeUseCase
import com.trailmileage.diary.presentation.app.AppViewModel
import com.trailmileage.diary.presentation.detail.HikeDetailViewModel
import com.trailmileage.diary.presentation.game.GameViewModel
import com.trailmileage.diary.presentation.gameover.GameOverViewModel
import com.trailmileage.diary.presentation.gear.GearChecklistViewModel
import com.trailmileage.diary.presentation.library.TrailLibraryViewModel
import com.trailmileage.diary.presentation.menu.MenuViewModel
import com.trailmileage.diary.presentation.settings.SettingsViewModel
import com.trailmileage.diary.presentation.splash.SplashViewModel
import com.trailmileage.diary.presentation.totals.YearTotalsViewModel

class AppViewModelFactory(
    private val hikeRepository: HikeRepository,
    private val trailRepository: TrailRepository,
    private val gearRepository: GearRepository,
    private val settingsRepository: SettingsRepository,
    private val saveHike: SaveHikeUseCase,
    private val buildYearSummary: BuildYearSummaryUseCase,
    private val formatter: FormatMeasurementUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel(settingsRepository)

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(hikeRepository, settingsRepository, buildYearSummary, formatter)

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(trailRepository, gearRepository, settingsRepository, saveHike, formatter)

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(hikeRepository, settingsRepository, buildYearSummary, formatter)

            modelClass.isAssignableFrom(TrailLibraryViewModel::class.java) ->
                TrailLibraryViewModel(trailRepository, settingsRepository, formatter)

            modelClass.isAssignableFrom(YearTotalsViewModel::class.java) ->
                YearTotalsViewModel(hikeRepository, settingsRepository, buildYearSummary, formatter)

            modelClass.isAssignableFrom(GearChecklistViewModel::class.java) ->
                GearChecklistViewModel(gearRepository)

            modelClass.isAssignableFrom(HikeDetailViewModel::class.java) ->
                HikeDetailViewModel(hikeRepository, gearRepository, settingsRepository, formatter)

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(hikeRepository, trailRepository, settingsRepository, formatter)

            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
