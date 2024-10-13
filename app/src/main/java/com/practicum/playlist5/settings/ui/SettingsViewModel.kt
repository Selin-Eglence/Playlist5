package com.practicum.playlist5.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlist5.settings.domain.api.SettingsInteractor
import com.practicum.playlist5.settings.domain.models.Theme
import com.practicum.playlist5.sharing.domain.SharingInteractor

class SettingsViewModel  (private val settingsInteractor: SettingsInteractor,
                          private val sharingInteractor: SharingInteractor

) : ViewModel() {

    private val _themeSettings = MutableLiveData<Theme>()
    val themeSettings: LiveData<Theme> = _themeSettings

    init {
        _themeSettings.value = settingsInteractor.getTheme()
    }

    fun updateThemeSettings(isDark: Boolean) {
        val newSettings = Theme(isDark)
        settingsInteractor.updateTheme(newSettings)
        _themeSettings.value = newSettings

    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }


}