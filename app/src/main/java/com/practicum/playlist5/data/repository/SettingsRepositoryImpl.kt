package com.practicum.playlist5.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlist5.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val settingsPref: SharedPreferences) :
    SettingsRepository {

    companion object {
        const val THEME_KEY = "switch_theme"
    }

    override fun darkThemeEnabled(): Boolean {
        return settingsPref.getBoolean(THEME_KEY, false)
    }

    override fun darkThemeChecked(checked: Boolean) {
        settingsPref.edit()
            .putBoolean(THEME_KEY, checked)
            .apply()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        settingsPref.edit().putBoolean(THEME_KEY, darkThemeEnabled).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}

