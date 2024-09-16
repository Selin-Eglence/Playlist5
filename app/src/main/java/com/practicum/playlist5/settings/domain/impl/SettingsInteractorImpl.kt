package com.practicum.playlist5.settings.domain.impl


import com.practicum.playlist5.settings.domain.api.SettingsInteractor
import com.practicum.playlist5.settings.domain.api.SettingsRepository
import com.practicum.playlist5.settings.domain.models.Theme


class SettingsInteractorImpl(private val repository: SettingsRepository) :
    SettingsInteractor {
    override fun getTheme(): Theme {
        return repository.getTheme()
    }

    override fun updateTheme(settings: Theme) {
        repository.updateTheme(settings)
    }
}