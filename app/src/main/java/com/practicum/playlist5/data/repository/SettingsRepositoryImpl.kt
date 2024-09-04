package com.practicum.playlist5.data.repository

import android.content.SharedPreferences
import com.practicum.playlist5.presentation.THEME_KEY
import com.practicum.playlist5.domain.repository.SettingsRepository

class SettingsRepositoryImpl ( private val settingsPref: SharedPreferences):
    SettingsRepository {
    override fun darkThemeEnabled(): Boolean {
           return settingsPref.getBoolean(THEME_KEY, false)
        }
    override fun darkThemeChecked(checked:Boolean) {
        settingsPref.edit()
        .putBoolean(THEME_KEY,checked)
        .apply()
    }
}

