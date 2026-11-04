package com.trailmileage.diary.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.trailmileage.diary.R
import java.time.LocalDate
import java.util.Locale

@Composable
fun distanceUnit(imperial: Boolean): String =
    stringResource(if (imperial) R.string.unit_mi else R.string.unit_km)

@Composable
fun elevationUnit(imperial: Boolean): String =
    stringResource(if (imperial) R.string.unit_ft else R.string.unit_m)

@Composable
fun durationLabel(hours: Int, minutes: Int): String =
    if (hours == 0) {
        stringResource(R.string.fmt_duration_m, minutes)
    } else {
        stringResource(R.string.fmt_duration_hm, hours, minutes)
    }

@Composable
fun measure(value: String, unit: String): String =
    stringResource(R.string.fmt_measure, value, unit)

@Composable
fun metaLine(elevation: String, imperial: Boolean, hours: Int, minutes: Int): String =
    stringResource(
        R.string.fmt_meta,
        elevation,
        elevationUnit(imperial),
        durationLabel(hours, minutes)
    )

@Composable
fun dateLabel(epochDay: Long): String {
    val months = stringArrayResource(R.array.month_short)
    val date = LocalDate.ofEpochDay(epochDay)
    return stringResource(
        R.string.fmt_date,
        date.dayOfMonth,
        months[date.monthValue - 1],
        date.year
    )
}

@Composable
fun monthInitials(): List<String> = stringArrayResource(R.array.month_initial).toList()

fun decimalText(value: Float): String = String.format(Locale.US, "%.1f", value)

fun integerText(value: Float): String = value.toInt().toString()
