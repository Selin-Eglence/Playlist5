package com.practicum.playlist5.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlist5.di.dataModule
import com.practicum.playlist5.di.interactorModule
import com.practicum.playlist5.di.repositoryModule
import com.practicum.playlist5.di.viewModelModule
import com.practicum.playlist5.settings.data.ThemeStorage
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin


class APP : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin{
           androidLogger()
           androidContext(this@APP)
           modules(listOf(dataModule, interactorModule, repositoryModule, viewModelModule))
       }

        val themeSwitch: ThemeStorage by inject()
        val switcher= themeSwitch.getTheme()


        AppCompatDelegate.setDefaultNightMode(
            if (switcher.darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }}

