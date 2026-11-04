package com.trailmileage.diary.presentation.gear

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmileage.diary.domain.model.GearList
import com.trailmileage.diary.domain.repository.GearRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GearChecklistViewModel(
    private val gearRepository: GearRepository
) : ViewModel() {

    private val ready = MutableStateFlow(false)
    private val selected = MutableStateFlow("")

    private val _state = MutableStateFlow(GearChecklistUiState())
    val state: StateFlow<GearChecklistUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(gearRepository.lists, selected, ready) { lists, chosen, loaded ->
                build(lists, chosen, loaded)
            }.collect { next -> _state.value = next }
        }
        viewModelScope.launch {
            gearRepository.load()
            ready.value = true
        }
    }

    fun onSelectTab(listId: String) {
        selected.value = listId
    }

    fun onToggle(itemId: String) {
        val listId = _state.value.selectedId
        if (listId.isEmpty()) return
        viewModelScope.launch { gearRepository.toggleItem(listId, itemId) }
    }

    fun onReset() {
        val listId = _state.value.selectedId
        if (listId.isEmpty()) return
        viewModelScope.launch { gearRepository.resetList(listId) }
    }

    fun onAttach() {
        val listId = _state.value.selectedId
        if (listId.isEmpty()) return
        gearRepository.selectForDraft(listId)
    }

    private fun build(
        lists: List<GearList>,
        chosen: String,
        loaded: Boolean
    ): GearChecklistUiState {
        val active = lists.firstOrNull { it.id == chosen } ?: lists.firstOrNull()
        return GearChecklistUiState(
            isLoading = !loaded,
            tabs = lists.map { GearListTab(it.id, it.title) },
            selectedId = active?.id.orEmpty(),
            items = active?.items?.map { GearItemState(it.id, it.name, it.packed) }.orEmpty(),
            packedCount = active?.packedCount ?: 0,
            totalCount = active?.items?.size ?: 0
        )
    }
}
