package com.practicum.playlist5.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer

import com.google.gson.Gson
import com.practicum.playlist5.media.MediaActivity
import com.practicum.playlist5.search.data.network.NetworkClient
import com.practicum.playlist5.search.data.network.RetrofitNetworkClient
import com.practicum.playlist5.search.data.network.TrackAPI
import com.practicum.playlist5.settings.data.SharedPrefStorage
import com.practicum.playlist5.settings.data.ThemeStorage
import com.practicum.playlist5.sharing.data.ExternalNavigatorImpl
import com.practicum.playlist5.sharing.domain.ExternalNavigator
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext


val dataModule = module {
    single<TrackAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackAPI::class.java)
    }
    single<SharedPreferences> {
        androidContext()
        .getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    factory{
        Gson()
    }

    single <ThemeStorage>{
        SharedPrefStorage(get())

    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory { MediaPlayer() }

    factory{ MediaActivity()}
    }




