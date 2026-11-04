package com.trailmileage.diary.data.repository

import com.trailmileage.diary.data.local.PreferencesStorage
import com.trailmileage.diary.data.local.RecordCodec
import com.trailmileage.diary.data.sample.SampleData
import com.trailmileage.diary.domain.model.GearList
import com.trailmileage.diary.domain.repository.GearRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class GearRepositoryImpl(
    private val storage: PreferencesStorage
) : GearRepository {

    private val state = MutableStateFlow<List<GearList>>(emptyList())
    private val draft = MutableStateFlow("")
    private val mutex = Mutex()
    private var loaded = false

    override val lists: StateFlow<List<GearList>> = state.asStateFlow()

    override val draftListId: StateFlow<String> = draft.asStateFlow()

    override suspend fun load() {
        mutex.withLock {
            if (loaded) return
            val restored = withContext(Dispatchers.IO) {
                val raw = storage.read(PreferencesStorage.KEY_GEAR)
                if (raw == null) {
                    val seeded = SampleData.gearLists()
                    storage.write(PreferencesStorage.KEY_GEAR, RecordCodec.encodeGear(seeded))
                    seeded
                } else {
                    RecordCodec.decodeGear(raw)
                }
            }
            state.value = restored
            loaded = true
        }
    }

    override suspend fun toggleItem(listId: String, itemId: String) {
        mutex.withLock {
            state.value = state.value.map { list ->
                if (list.id != listId) {
                    list
                } else {
                    list.copy(
                        items = list.items.map { item ->
                            if (item.id == itemId) item.copy(packed = !item.packed) else item
                        }
                    )
                }
            }
            persist()
        }
    }

    override suspend fun resetList(listId: String) {
        mutex.withLock {
            state.value = state.value.map { list ->
                if (list.id != listId) {
                    list
                } else {
                    list.copy(items = list.items.map { it.copy(packed = false) })
                }
            }
            persist()
        }
    }

    override fun selectForDraft(listId: String) {
        draft.value = listId
    }

    private suspend fun persist() {
        withContext(Dispatchers.IO) {
            storage.write(PreferencesStorage.KEY_GEAR, RecordCodec.encodeGear(state.value))
        }
    }
}
