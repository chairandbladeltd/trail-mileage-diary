package com.trailmileage.diary.data.local

import com.trailmileage.diary.domain.model.GearItem
import com.trailmileage.diary.domain.model.GearList
import com.trailmileage.diary.domain.model.Hike
import com.trailmileage.diary.domain.model.Trail
import com.trailmileage.diary.domain.model.Weather

private const val FIELD = "\u0001"
private const val RECORD = "\u0002"
private const val GROUP = "\u0003"

object RecordCodec {

    fun encodeHikes(hikes: List<Hike>): String = hikes.joinToString(RECORD) { hike ->
        listOf(
            hike.id.toString(),
            clean(hike.trailName),
            hike.distanceKm.toString(),
            hike.elevationM.toString(),
            hike.durationMin.toString(),
            hike.weather.name,
            hike.difficulty.toString(),
            hike.photoSlots.joinToString(GROUP),
            clean(hike.gearListId),
            clean(hike.note),
            hike.epochDay.toString()
        ).joinToString(FIELD)
    }

    fun decodeHikes(raw: String): List<Hike> = split(raw).mapNotNull { row ->
        val parts = row.split(FIELD)
        if (parts.size < 11) return@mapNotNull null
        val id = parts[0].toLongOrNull() ?: return@mapNotNull null
        Hike(
            id = id,
            trailName = parts[1],
            distanceKm = parts[2].toDoubleOrNull() ?: 0.0,
            elevationM = parts[3].toIntOrNull() ?: 0,
            durationMin = parts[4].toIntOrNull() ?: 0,
            weather = weatherOf(parts[5]),
            difficulty = parts[6].toIntOrNull() ?: 1,
            photoSlots = parts[7].split(GROUP).mapNotNull { it.toIntOrNull() },
            gearListId = parts[8],
            note = parts[9],
            epochDay = parts[10].toLongOrNull() ?: 0L
        )
    }

    fun encodeTrails(trails: List<Trail>): String = trails.joinToString(RECORD) { trail ->
        listOf(
            clean(trail.id),
            clean(trail.name),
            trail.visitCount.toString(),
            trail.totalKm.toString(),
            trail.lastHikedEpochDay.toString()
        ).joinToString(FIELD)
    }

    fun decodeTrails(raw: String): List<Trail> = split(raw).mapNotNull { row ->
        val parts = row.split(FIELD)
        if (parts.size < 5) return@mapNotNull null
        Trail(
            id = parts[0],
            name = parts[1],
            visitCount = parts[2].toIntOrNull() ?: 0,
            totalKm = parts[3].toDoubleOrNull() ?: 0.0,
            lastHikedEpochDay = parts[4].toLongOrNull() ?: 0L
        )
    }

    fun encodeGear(lists: List<GearList>): String = lists.joinToString(RECORD) { list ->
        val items = list.items.joinToString(GROUP) { item ->
            clean(item.id) + "~" + clean(item.name) + "~" + if (item.packed) "1" else "0"
        }
        listOf(clean(list.id), clean(list.title), items).joinToString(FIELD)
    }

    fun decodeGear(raw: String): List<GearList> = split(raw).mapNotNull { row ->
        val parts = row.split(FIELD)
        if (parts.size < 3) return@mapNotNull null
        val items = parts[2].split(GROUP).mapNotNull { chunk ->
            val fields = chunk.split("~")
            if (fields.size < 3) null else GearItem(fields[0], fields[1], fields[2] == "1")
        }
        GearList(parts[0], parts[1], items)
    }

    private fun split(raw: String): List<String> =
        if (raw.isEmpty()) emptyList() else raw.split(RECORD).filter { it.isNotEmpty() }

    private fun weatherOf(name: String): Weather =
        Weather.entries.firstOrNull { it.name == name } ?: Weather.SUN

    private fun clean(value: String): String =
        value.replace(FIELD, " ").replace(RECORD, " ").replace(GROUP, " ").replace("~", "-")
}
