package com.practicum.playlist5

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlist5.data.network.RetrofitNetworkClient
import com.practicum.playlist5.data.network.SearchHistoryRepositoryImpl
import com.practicum.playlist5.data.network.TrackRepositoryImpl
import com.practicum.playlist5.domain.api.TrackInteractor
import com.practicum.playlist5.domain.api.TrackRepository
import com.practicum.playlist5.domain.impi.TrackInteractorImpl

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
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


    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

}


