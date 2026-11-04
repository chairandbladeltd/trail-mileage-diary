package com.trailmileage.diary.data.repository

import com.trailmileage.diary.data.local.PreferencesStorage
import com.trailmileage.diary.data.local.RecordCodec
import com.trailmileage.diary.data.sample.SampleData
import com.trailmileage.diary.domain.model.Hike
import com.trailmileage.diary.domain.repository.HikeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicLong

class HikeRepositoryImpl(
    private val storage: PreferencesStorage
) : HikeRepository {

    private val state = MutableStateFlow<List<Hike>>(emptyList())
    private val mutex = Mutex()
    private val sequence = AtomicLong(1L)
    private var loaded = false

    override val hikes: StateFlow<List<Hike>> = state.asStateFlow()

    override suspend fun load() {
        mutex.withLock {
            if (loaded) return
            val snapshot = withContext(Dispatchers.IO) {
                val raw = storage.read(PreferencesStorage.KEY_HIKES)
                val restored = if (raw == null) {
                    val seeded = SampleData.hikes(LocalDate.now().toEpochDay())
                    storage.write(PreferencesStorage.KEY_HIKES, RecordCodec.encodeHikes(seeded))
                    storage.writeLong(PreferencesStorage.KEY_SEQUENCE, seeded.size.toLong() + 1L)
                    seeded
                } else {
                    RecordCodec.decodeHikes(raw)
                }
                val next = storage.readLong(
                    PreferencesStorage.KEY_SEQUENCE,
                    restored.size.toLong() + 1L
                )
                restored to next
            }
            sequence.set(snapshot.second)
            state.value = snapshot.first.sortedByDescending { it.epochDay }
            loaded = true
        }
    }

    override suspend fun add(hike: Hike): Long {
        mutex.withLock {
            val updated = (state.value + hike).sortedByDescending { it.epochDay }
            state.value = updated
            withContext(Dispatchers.IO) {
                storage.write(PreferencesStorage.KEY_HIKES, RecordCodec.encodeHikes(updated))
                storage.writeLong(PreferencesStorage.KEY_SEQUENCE, sequence.get())
            }
        }
        return hike.id
    }

    override suspend fun delete(id: Long) {
        mutex.withLock {
            val updated = state.value.filterNot { it.id == id }
            state.value = updated
            withContext(Dispatchers.IO) {
                storage.write(PreferencesStorage.KEY_HIKES, RecordCodec.encodeHikes(updated))
            }
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            state.value = emptyList()
            withContext(Dispatchers.IO) {
                storage.write(PreferencesStorage.KEY_HIKES, "")
            }
        }
    }

    override fun nextId(): Long = sequence.incrementAndGet()
}
