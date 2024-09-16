package com.practicum.playlist5.settings.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.practicum.playlist5.settings.domain.models.Theme

class SharedPrefStorage (private val sharedPreferences: SharedPreferences) :
    ThemeStorage {

        override fun getTheme(): Theme {
            val isDark = sharedPreferences.getBoolean(THEME_KEY, false)
            return Theme(isDark)
        }


        override fun saveTheme(settings: Theme) {
            sharedPreferences.edit {
                putBoolean(THEME_KEY, settings.darkTheme)
            }
        }

        companion object {
            const val THEME_KEY = "dark_theme"
        }
    }
