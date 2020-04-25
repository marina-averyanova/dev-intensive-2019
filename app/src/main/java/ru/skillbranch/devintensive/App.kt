package ru.skillbranch.devintensive

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class App: Application() {
    companion object {
        private var instance: App? = null

        fun applicationContext(): Context {
            Log.d("M_App", "test applicationContext called")
            return instance!!.applicationContext
        }
    }

    init {
        Log.d("M_App", " App init")
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        PreferencesRepository.getAppTheme().also {
            AppCompatDelegate.setDefaultNightMode(it)
        }
        Log.d("M_App", "test App called")
        // TODO call once when app created
    }
}