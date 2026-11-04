package com.trailmileage.diary.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmileage.diary.core.config.GameConfig
import com.trailmileage.diary.core.di.ServiceLocator
import com.trailmileage.diary.core.navigation.Screen
import com.trailmileage.diary.core.theme.AppTheme
import com.trailmileage.diary.presentation.detail.HikeDetailRoute
import com.trailmileage.diary.presentation.game.GameRoute
import com.trailmileage.diary.presentation.gameover.GameOverRoute
import com.trailmileage.diary.presentation.gear.GearChecklistRoute
import com.trailmileage.diary.presentation.library.TrailLibraryRoute
import com.trailmileage.diary.presentation.menu.MenuRoute
import com.trailmileage.diary.presentation.settings.SettingsRoute
import com.trailmileage.diary.presentation.splash.SplashRoute
import com.trailmileage.diary.presentation.totals.YearTotalsRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()
    val dusk by appViewModel.dusk.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    AppTheme(dusk = dusk) {
        AnimatedContent(
            targetState = screen,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                val duration = GameConfig.SCREEN_TRANSITION_MS
                if (initialState == Screen.Splash) {
                    fadeIn(tween(duration)) togetherWith fadeOut(tween(duration))
                } else {
                    val spec = tween<IntOffset>(duration, easing = FastOutSlowInEasing)
                    (slideInHorizontally(spec) { it / 6 } + fadeIn(tween(duration))) togetherWith
                        (slideOutHorizontally(spec) { -it / 6 } + fadeOut(tween(duration)))
                }
            },
            label = "screen"
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashRoute(
                    onFinished = appViewModel::onSplashFinished
                )

                is Screen.Menu -> MenuRoute(
                    onNewHike = { appViewModel.startNewHike() },
                    onHikeClick = { appViewModel.navigate(Screen.HikeDetail(it)) },
                    onTrails = { appViewModel.navigate(Screen.TrailLibrary) },
                    onTotals = { appViewModel.navigate(Screen.YearTotals) },
                    onGear = { appViewModel.navigate(Screen.GearChecklist()) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )

                is Screen.Game -> GameRoute(
                    trailName = current.trailName,
                    requestId = current.requestId,
                    onBack = appViewModel::back,
                    onSaved = { appViewModel.openSavedHike(it) },
                    onOpenLibrary = { appViewModel.navigate(Screen.TrailLibrary) },
                    onPickGear = { appViewModel.navigate(Screen.GearChecklist(pickMode = true)) }
                )

                is Screen.GameOver -> GameOverRoute(
                    hikeId = current.hikeId,
                    onLogAgain = appViewModel::restartNewHike,
                    onViewHike = { appViewModel.navigate(Screen.HikeDetail(it)) },
                    onMenu = appViewModel::backToMenu
                )

                is Screen.TrailLibrary -> TrailLibraryRoute(
                    onBack = appViewModel::back,
                    onPickTrail = { name ->
                        appViewModel.back()
                        appViewModel.startNewHike(name)
                    }
                )

                is Screen.YearTotals -> YearTotalsRoute(
                    onBack = appViewModel::back
                )

                is Screen.GearChecklist -> GearChecklistRoute(
                    pickMode = current.pickMode,
                    onBack = appViewModel::back
                )

                is Screen.HikeDetail -> HikeDetailRoute(
                    hikeId = current.hikeId,
                    onBack = appViewModel::back
                )

                is Screen.Settings -> SettingsRoute(
                    onBack = appViewModel::back,
                    onDataCleared = appViewModel::backToMenu
                )
            }
        }
    }
}
