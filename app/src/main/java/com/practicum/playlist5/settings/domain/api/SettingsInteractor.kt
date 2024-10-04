package com.practicum.playlist5.settings.domain.api

import com.practicum.playlist5.settings.domain.models.Theme

interface SettingsInteractor {
    fun getTheme(): Theme
    fun updateTheme(settings: Theme)
}