package com.practicum.playlist5.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlist5.presentation.APP
import com.practicum.playlist5.data.network.RetrofitNetworkClient
import com.practicum.playlist5.data.repository.AudioPlayerRepositoryImpl
import com.practicum.playlist5.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlist5.data.repository.SettingsRepositoryImpl
import com.practicum.playlist5.data.repository.TrackRepositoryImpl
import com.practicum.playlist5.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.domain.repository.AudioPlayerRepository
import com.practicum.playlist5.domain.api.SearchHistoryInteractor
import com.practicum.playlist5.domain.api.SettingsInteractor
import com.practicum.playlist5.domain.api.TrackInteractor
import com.practicum.playlist5.domain.repository.TrackRepository
import com.practicum.playlist5.domain.impi.AudioPlayerInteractorImpl
import com.practicum.playlist5.domain.impi.SearchHistoryInteractorImpl
import com.practicum.playlist5.domain.impi.SettingsInteractorImpl
import com.practicum.playlist5.domain.impi.TrackInteractorImpl
import com.practicum.playlist5.domain.repository.SettingsRepository

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

    private fun getSettingsRepository() : SettingsRepository {
        return SettingsRepositoryImpl(provideSharedPreferences())
    }

    fun provideSettingsInteractor(app: APP): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(),app)
    }

    private fun provideAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(provideAudioPlayerRepository())
    }





}


