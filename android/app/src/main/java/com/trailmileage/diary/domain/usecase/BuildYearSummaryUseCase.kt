package com.trailmileage.diary.domain.usecase

import com.trailmileage.diary.domain.model.Hike
import com.trailmileage.diary.domain.model.YearSummary

class BuildYearSummaryUseCase {

    operator fun invoke(hikes: List<Hike>, year: Int): YearSummary {
        val ofYear = hikes.filter { it.year == year }
        if (ofYear.isEmpty()) return YearSummary.empty(year)
        val monthly = MutableList(12) { 0.0 }
        ofYear.forEach { hike ->
            val index = hike.month - 1
            if (index in 0..11) monthly[index] = monthly[index] + hike.distanceKm
        }
        return YearSummary(
            year = year,
            distanceKm = ofYear.sumOf { it.distanceKm },
            elevationM = ofYear.sumOf { it.elevationM },
            hikeCount = ofYear.size,
            longestKm = ofYear.maxOf { it.distanceKm },
            biggestClimbM = ofYear.maxOf { it.elevationM },
            monthlyKm = monthly
        )
    }
}
