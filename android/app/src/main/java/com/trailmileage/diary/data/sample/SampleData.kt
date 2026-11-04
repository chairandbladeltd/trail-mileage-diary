package com.trailmileage.diary.data.sample

import com.trailmileage.diary.domain.model.GearItem
import com.trailmileage.diary.domain.model.GearList
import com.trailmileage.diary.domain.model.Hike
import com.trailmileage.diary.domain.model.Trail
import com.trailmileage.diary.domain.model.Weather

object SampleData {

    const val DEFAULT_GEAR_ID = "day_hike"

    fun trails(today: Long): List<Trail> = listOf(
        Trail("eagle_ridge_loop", "Eagle Ridge Loop", 8, 96.4, today - 4),
        Trail("birch_creek_trail", "Birch Creek Trail", 5, 41.0, today - 26),
        Trail("granite_saddle", "Granite Saddle", 3, 37.2, today - 12),
        Trail("fern_hollow", "Fern Hollow", 6, 28.8, today - 47),
        Trail("larch_pass", "Larch Pass", 2, 24.6, today - 73)
    )

    fun hikes(today: Long): List<Hike> = listOf(
        Hike(
            id = 1L,
            trailName = "Eagle Ridge Loop",
            distanceKm = 12.4,
            elevationM = 640,
            durationMin = 200,
            weather = Weather.SUN,
            difficulty = 4,
            photoSlots = listOf(1, 2, 0, 0),
            gearListId = "day_hike",
            note = "Cold start at the trailhead, clear ridge line the whole way up.",
            epochDay = today - 4
        ),
        Hike(
            id = 2L,
            trailName = "Granite Saddle",
            distanceKm = 14.8,
            elevationM = 1120,
            durationMin = 305,
            weather = Weather.WIND,
            difficulty = 5,
            photoSlots = listOf(1, 0, 0, 0),
            gearListId = "summit",
            note = "Wind on the saddle strong enough to lean into. Turned back at the cairn.",
            epochDay = today - 12
        ),
        Hike(
            id = 3L,
            trailName = "Birch Creek Trail",
            distanceKm = 8.2,
            elevationM = 210,
            durationMin = 130,
            weather = Weather.RAIN,
            difficulty = 2,
            photoSlots = listOf(2, 0, 0, 0),
            gearListId = "day_hike",
            note = "Steady rain, creek running high, boots soaked by the second crossing.",
            epochDay = today - 26
        ),
        Hike(
            id = 4L,
            trailName = "Fern Hollow",
            distanceKm = 6.0,
            elevationM = 140,
            durationMin = 95,
            weather = Weather.CLOUD,
            difficulty = 2,
            photoSlots = listOf(2, 1, 0, 0),
            gearListId = "day_hike",
            note = "Short evening loop through the ferns, moss on every stone.",
            epochDay = today - 47
        )
    )

    fun gearLists(): List<GearList> = listOf(
        GearList(
            id = "day_hike",
            title = "DAY HIKE",
            items = items(
                "Water 2L", "Trail Snacks", "Paper Map", "Compass", "Rain Shell",
                "First Aid Kit", "Headlamp", "Sunscreen", "Sun Hat", "Light Gloves",
                "Whistle", "Blister Kit"
            )
        ),
        GearList(
            id = "summit",
            title = "SUMMIT",
            items = items(
                "Water 3L", "Wind Shell", "Insulated Jacket", "Trekking Poles",
                "Helmet", "Summit Snacks", "Emergency Blanket", "Route Card",
                "Spare Socks", "Headlamp"
            )
        ),
        GearList(
            id = "winter",
            title = "WINTER",
            items = items(
                "Thermos", "Microspikes", "Snow Gaiters", "Down Jacket",
                "Wool Base Layer", "Balaclava", "Mittens", "Goggles",
                "Avalanche Whistle", "Hand Warmers"
            )
        ),
        GearList(
            id = "overnight",
            title = "OVERNIGHT",
            items = items(
                "Tent", "Sleeping Bag", "Sleeping Pad", "Stove", "Fuel Canister",
                "Cook Pot", "Dry Bag", "Water Filter", "Camp Towel", "Repair Tape"
            )
        )
    )

    private fun items(vararg names: String): List<GearItem> = names.mapIndexed { index, name ->
        GearItem(
            id = name.lowercase().replace(" ", "_"),
            name = name,
            packed = index < 3
        )
    }
}
