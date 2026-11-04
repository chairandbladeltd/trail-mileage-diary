package com.trailmileage.diary.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStorage(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun read(key: String): String? = prefs.getString(key, null)

    fun write(key: String, value: String) {
        prefs.edit().putString(key, value).commit()
    }

    fun readLong(key: String, fallback: Long): Long = prefs.getLong(key, fallback)

    fun writeLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).commit()
    }

    fun clear() {
        prefs.edit().clear().commit()
    }

    companion object {
        private const val STORE_NAME = "trail_mileage_diary"
        const val KEY_HIKES = "hikes_v1"
        const val KEY_TRAILS = "trails_v1"
        const val KEY_GEAR = "gear_v1"
        const val KEY_UNITS = "units_v1"
        const val KEY_THEME = "theme_v1"
        const val KEY_SEQUENCE = "sequence_v1"
    }
}
