package com.practicum.playlist5.domain.impi


import com.practicum.playlist5.presentation.APP
import com.practicum.playlist5.domain.api.SettingsInteractor
import com.practicum.playlist5.domain.repository.SettingsRepository


class SettingsInteractorImpl(private val repository: SettingsRepository, private val app: APP):
    SettingsInteractor {
    override fun darkThemeEnabled(): Boolean {
        return repository.darkThemeEnabled()
    }

    override fun switchTheme(checked: Boolean) {
        repository.darkThemeChecked(checked)
        app.switchTheme(checked)
    }
}