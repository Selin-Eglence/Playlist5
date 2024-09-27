package com.practicum.playlist5.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlist5.settings.domain.api.SettingsRepository
import com.practicum.playlist5.settings.domain.models.Theme

class SettingsRepositoryImpl(private val storage: ThemeStorage) :
    SettingsRepository {

    override fun getTheme(): Theme {
        val isDark = storage.getTheme().darkTheme
        return Theme(isDark)
    }

    override fun updateTheme(settings: Theme) {
        storage.saveTheme(settings)
    }
}

