package com.practicum.playlist5.domain.api

interface SettingsInteractor {
    fun darkThemeEnabled(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}