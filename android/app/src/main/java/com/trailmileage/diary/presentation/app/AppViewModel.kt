package com.trailmileage.diary.presentation.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmileage.diary.core.navigation.Screen
import com.trailmileage.diary.domain.model.ThemeMode
import com.trailmileage.diary.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val stack = ArrayDeque<Screen>()
    private var requestCounter = 0L

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    val dusk: StateFlow<Boolean> = settingsRepository.settings
        .map { it.theme == ThemeMode.DUSK }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        viewModelScope.launch { settingsRepository.load() }
    }

    fun onSplashFinished() {
        stack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun navigate(target: Screen) {
        stack.addLast(_screen.value)
        _screen.value = target
        syncBack()
    }

    fun startNewHike(trailName: String = "") {
        requestCounter += 1
        navigate(Screen.Game(trailName, requestCounter))
    }

    fun restartNewHike() {
        requestCounter += 1
        _screen.value = Screen.Game("", requestCounter)
        syncBack()
    }

    fun openSavedHike(hikeId: Long) {
        _screen.value = Screen.GameOver(hikeId)
        syncBack()
    }

    fun backToMenu() {
        stack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun back() {
        if (stack.isEmpty()) return
        _screen.value = stack.removeLast()
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = stack.isNotEmpty()
    }
}
