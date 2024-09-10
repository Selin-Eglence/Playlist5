package com.practicum.playlist5.presentation

import android.app.Application
import com.practicum.playlist5.creator.Creator
import com.practicum.playlist5.domain.api.SettingsInteractor


class APP : Application() {

    private lateinit var settingsInteractor: SettingsInteractor
    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)
        settingsInteractor = Creator.provideSettingsInteractor()

        darkTheme = settingsInteractor.darkThemeEnabled()
        settingsInteractor.switchTheme(darkTheme)
    }

}