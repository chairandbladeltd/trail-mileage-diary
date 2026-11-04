package com.trailmileage.diary.data.repository

import com.trailmileage.diary.data.local.PreferencesStorage
import com.trailmileage.diary.data.local.RecordCodec
import com.trailmileage.diary.data.sample.SampleData
import com.trailmileage.diary.domain.model.Trail
import com.trailmileage.diary.domain.repository.TrailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.time.LocalDate

class TrailRepositoryImpl(
    private val storage: PreferencesStorage
) : TrailRepository {

    private val state = MutableStateFlow<List<Trail>>(emptyList())
    private val mutex = Mutex()
    private var loaded = false

    override val trails: StateFlow<List<Trail>> = state.asStateFlow()

    override suspend fun load() {
        mutex.withLock {
            if (loaded) return
            val restored = withContext(Dispatchers.IO) {
                val raw = storage.read(PreferencesStorage.KEY_TRAILS)
                if (raw == null) {
                    val seeded = SampleData.trails(LocalDate.now().toEpochDay())
                    storage.write(PreferencesStorage.KEY_TRAILS, RecordCodec.encodeTrails(seeded))
                    seeded
                } else {
                    RecordCodec.decodeTrails(raw)
                }
            }
            state.value = restored.sortedByDescending { it.visitCount }
            loaded = true
        }
    }

    override suspend fun register(name: String, distanceKm: Double, epochDay: Long) {
        mutex.withLock {
            val key = slug(name)
            val existing = state.value.firstOrNull { it.id == key }
            val updated = if (existing == null) {
                state.value + Trail(key, name, 1, distanceKm, epochDay)
            } else {
                state.value.map { trail ->
                    if (trail.id == key) {
                        trail.copy(
                            visitCount = trail.visitCount + 1,
                            totalKm = trail.totalKm + distanceKm,
                            lastHikedEpochDay = epochDay
                        )
                    } else {
                        trail
                    }
                }
            }
            state.value = updated.sortedByDescending { it.visitCount }
            withContext(Dispatchers.IO) {
                storage.write(PreferencesStorage.KEY_TRAILS, RecordCodec.encodeTrails(state.value))
            }
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            state.value = emptyList()
            withContext(Dispatchers.IO) {
                storage.write(PreferencesStorage.KEY_TRAILS, "")
            }
        }
    }

    private fun slug(name: String): String {
        val lowered = name.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_")
        return lowered.trim('_').ifEmpty { "trail" }
    }
}
