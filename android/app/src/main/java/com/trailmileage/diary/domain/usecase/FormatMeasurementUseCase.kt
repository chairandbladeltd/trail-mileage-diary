package com.trailmileage.diary.domain.usecase

import com.trailmileage.diary.domain.model.UnitSystem
import java.util.Locale

private const val MI_PER_KM = 0.621371
private const val FT_PER_M = 3.28084

class FormatMeasurementUseCase {

    fun distance(km: Double, units: UnitSystem): String {
        val value = if (units == UnitSystem.IMPERIAL) km * MI_PER_KM else km
        return String.format(Locale.US, "%.1f", value)
    }

    fun elevation(meters: Int, units: UnitSystem): String {
        val value = if (units == UnitSystem.IMPERIAL) (meters * FT_PER_M).toInt() else meters
        return value.toString()
    }

    fun distanceValue(km: Double, units: UnitSystem): Double =
        if (units == UnitSystem.IMPERIAL) km * MI_PER_KM else km

    fun elevationValue(meters: Int, units: UnitSystem): Double =
        if (units == UnitSystem.IMPERIAL) meters * FT_PER_M else meters.toDouble()

    fun hoursPart(minutes: Int): Int = minutes / 60

    fun minutesPart(minutes: Int): Int = minutes % 60

    fun isShort(minutes: Int): Boolean = minutes < 60
}
