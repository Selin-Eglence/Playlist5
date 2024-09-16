package com.practicum.playlist5.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlist5.search.data.network.RetrofitNetworkClient
import com.practicum.playlist5.audioplayer.data.AudioPlayerRepositoryImpl
import com.practicum.playlist5.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlist5.settings.data.SettingsRepositoryImpl
import com.practicum.playlist5.search.data.repository.TrackRepositoryImpl
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerRepository
import com.practicum.playlist5.search.domain.api.SearchHistoryInteractor
import com.practicum.playlist5.settings.domain.api.SettingsInteractor
import com.practicum.playlist5.search.domain.api.TrackInteractor
import com.practicum.playlist5.search.domain.api.TrackRepository
import com.practicum.playlist5.audioplayer.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlist5.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlist5.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlist5.search.domain.impl.TrackInteractorImpl
import com.practicum.playlist5.settings.data.SharedPrefStorage
import com.practicum.playlist5.settings.data.ThemeStorage
import com.practicum.playlist5.settings.domain.api.SettingsRepository
import com.practicum.playlist5.sharing.data.ExternalNavigatorImpl
import com.practicum.playlist5.sharing.data.SharingRepositoryImpl
import com.practicum.playlist5.sharing.domain.ExternalNavigator
import com.practicum.playlist5.sharing.domain.SharingInteractor
import com.practicum.playlist5.sharing.domain.SharingInteractorImpl
import com.practicum.playlist5.sharing.domain.api.SharingRepository

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        Creator.application = application
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideSearchHistoryRepository(): SearchHistoryRepositoryImpl {
        return SearchHistoryRepositoryImpl(provideSharedPreferences())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository())
    }



    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    private fun provideSettingStorage(): ThemeStorage {
        return SharedPrefStorage(provideSharedPreferences())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(storage = provideSettingStorage())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    private fun provideAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(provideAudioPlayerRepository())
    }
    private fun getNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    private fun getSharingRep(): SharingRepository{
        return SharingRepositoryImpl(application)
    }

    fun provideSharingRepositoryInteractor(): SharingInteractor {
        return SharingInteractorImpl(getNavigator(), getSharingRep())
    }




}


