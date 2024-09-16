package com.practicum.playlist5.utils

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlist5.settings.domain.api.SettingsInteractor
import com.practicum.playlist5.utils.Creator.initApplication
import com.practicum.playlist5.utils.Creator.provideSharedPreferences


class APP : Application() {

    private lateinit var settingsInteractor: SettingsInteractor
    var darkTheme = false
        private set

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        initApplication(this)
        sharedPrefs = provideSharedPreferences()
        switchTheme(sharedPrefs.getBoolean(DarkTheme, darkTheme))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val DarkTheme = "darktheme_enabled"
    }


}