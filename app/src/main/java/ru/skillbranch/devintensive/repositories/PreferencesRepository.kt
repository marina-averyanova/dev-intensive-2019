package ru.skillbranch.devintensive.repositories

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.models.Profile

object PreferencesRepository {

    private const val FIRST_NAME = "FIRST_NAME"
    private const val LAST_NAME = "LAST_NAME"
    private const val ABOUT = "ABOUT"
    private const val REPOSITORY = "REPOSITORY"
    private const val RATING = "RATING"
    private const val RESPECT = "RESPECT"
    private const val APP_THEME = "APP_THEME"

    val prefs: SharedPreferences by lazy {
        val ctx = App.applicationContext()
        PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun saveAppTheme(theme: Int) {
        putValue(APP_THEME to theme)
    }

    fun getAppTheme(): Int = prefs.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_NO)

    fun saveProfile(profile: Profile) {
        Log.d("M_PreferencesRepository", "profile in prefs: $profile")
        Log.d("M_PreferencesRepository",
            "prefs before: ${prefs.getString(FIRST_NAME, "default")}")
        with(profile) {
            putValue(FIRST_NAME to firstName)
            putValue(LAST_NAME to lastName)
            putValue(ABOUT to about)
            putValue(REPOSITORY to repository)
            putValue(RATING to rating)
            putValue(RESPECT to respect)
        }

        Log.d("M_PreferencesRepository",
            "prefs after: ${prefs.getString(FIRST_NAME, "default")}")
    }

    fun getProfile(): Profile {
        Log.d("M_PreferencesRepository", "get profile: ${prefs.all}")
        return Profile(
            prefs.getString(FIRST_NAME, "")!!,
            prefs.getString(LAST_NAME, "")!!,
            prefs.getString(ABOUT, "")!!,
            prefs.getString(REPOSITORY, "")!!,
            prefs.getInt(RATING, 0),
            prefs.getInt(RESPECT, 0)
        )
    }

    private fun putValue(pair: Pair<String, Any>) = with(prefs.edit()) {
        val key = pair.first

        when (val value = pair.second) {

            is String -> {
                Log.d("M_PreferencesRepository", "string rating: ${value}")
                putString(key, value)
            }
            is Int -> {
                Log.d("M_PreferencesRepository", "int value: ${value}")
                putInt(key, value)
            }
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("only primitives can be stored in shared preferences")
        }

        apply()
    }
}