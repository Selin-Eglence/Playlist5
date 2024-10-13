package com.practicum.playlist5.settings.data

import com.practicum.playlist5.settings.domain.models.Theme

interface ThemeStorage {
    fun saveTheme(settings: Theme)
    fun getTheme(): Theme
}
