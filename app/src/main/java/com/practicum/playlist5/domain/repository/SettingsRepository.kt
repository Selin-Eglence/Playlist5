package com.practicum.playlist5.domain.repository

interface SettingsRepository {


    fun darkThemeEnabled(): Boolean
    fun darkThemeChecked(checked: Boolean)

    fun switchTheme(darkThemeEnabled: Boolean)
}