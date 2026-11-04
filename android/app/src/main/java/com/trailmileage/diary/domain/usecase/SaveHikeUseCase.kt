package com.trailmileage.diary.domain.usecase

import com.trailmileage.diary.domain.model.Hike
import com.trailmileage.diary.domain.model.HikeDraft
import com.trailmileage.diary.domain.repository.HikeRepository
import com.trailmileage.diary.domain.repository.TrailRepository

class SaveHikeUseCase(
    private val hikeRepository: HikeRepository,
    private val trailRepository: TrailRepository
) {

    suspend operator fun invoke(draft: HikeDraft, epochDay: Long): Long {
        val hike = Hike(
            id = hikeRepository.nextId(),
            trailName = draft.trailName.trim(),
            distanceKm = draft.distanceKm,
            elevationM = draft.elevationM,
            durationMin = draft.durationMin,
            weather = draft.weather,
            difficulty = draft.difficulty,
            photoSlots = draft.photoSlots,
            gearListId = draft.gearListId,
            note = draft.note.trim(),
            epochDay = epochDay
        )
        val id = hikeRepository.add(hike)
        trailRepository.register(hike.trailName, hike.distanceKm, epochDay)
        return id
    }
}
