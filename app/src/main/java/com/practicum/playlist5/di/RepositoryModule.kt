package com.practicum.playlist5.di

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlist5.audioplayer.data.AudioPlayerRepositoryImpl
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerRepository
import com.practicum.playlist5.audioplayer.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlist5.search.data.network.RetrofitNetworkClient
import com.practicum.playlist5.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlist5.search.data.repository.TrackRepositoryImpl
import com.practicum.playlist5.search.domain.api.SearchHistoryInteractor
import com.practicum.playlist5.search.domain.api.SearchHistoryRepository
import com.practicum.playlist5.search.domain.api.TrackInteractor
import com.practicum.playlist5.search.domain.api.TrackRepository
import com.practicum.playlist5.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlist5.search.domain.impl.TrackInteractorImpl
import com.practicum.playlist5.settings.data.SettingsRepositoryImpl
import com.practicum.playlist5.settings.data.SharedPrefStorage
import com.practicum.playlist5.settings.data.ThemeStorage
import com.practicum.playlist5.settings.domain.api.SettingsInteractor
import com.practicum.playlist5.settings.domain.api.SettingsRepository
import com.practicum.playlist5.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlist5.sharing.data.ExternalNavigatorImpl
import com.practicum.playlist5.sharing.data.SharingRepositoryImpl
import com.practicum.playlist5.sharing.domain.ExternalNavigator
import com.practicum.playlist5.sharing.domain.SharingInteractor
import com.practicum.playlist5.sharing.domain.SharingInteractorImpl
import com.practicum.playlist5.sharing.domain.api.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.get

import org.koin.dsl.module


val repositoryModule = module {
    single <TrackRepository>{
        TrackRepositoryImpl(get())
    }
    single <SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(),get())
    }
    factory <SettingsRepository>{
        SettingsRepositoryImpl(get())
    }
    single <AudioPlayerRepository>{
        AudioPlayerRepositoryImpl(get())
    }
    single <SharingRepository>{
        SharingRepositoryImpl(androidContext())
    }

}