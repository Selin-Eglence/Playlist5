package com.practicum.playlist5.settings.data

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.practicum.playlist5.settings.domain.models.Theme

class SharedPrefStorage (private val sharedPreferences: SharedPreferences) :
    ThemeStorage {

        override fun getTheme(): Theme {
            val isDark = sharedPreferences.getBoolean(THEME_KEY, false)
            Log.d("SharedPrefStorage", "Loaded theme: isDark = $isDark")
            return Theme(isDark)
        }


        override fun saveTheme(settings: Theme) {
            sharedPreferences.edit {
                putBoolean(THEME_KEY, settings.darkTheme)

            }
            Log.d("SharedPrefStorage", "Saved theme: isDark = $settings.darkTheme")
        }

        companion object {
            const val THEME_KEY = "dark_theme"
        }
    }
