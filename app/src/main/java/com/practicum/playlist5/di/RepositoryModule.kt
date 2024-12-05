package com.practicum.playlist5.di

import com.practicum.playlist5.audioplayer.data.AudioPlayerRepositoryImpl
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerRepository
import com.practicum.playlist5.media.data.FavouriteRepositoryImpl
import com.practicum.playlist5.media.data.converter.TracksDbConverter
import com.practicum.playlist5.media.domain.FavouriteRepository
import com.practicum.playlist5.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlist5.search.data.repository.TrackRepositoryImpl
import com.practicum.playlist5.search.domain.api.SearchHistoryRepository
import com.practicum.playlist5.search.domain.api.TrackRepository
import com.practicum.playlist5.settings.data.SettingsRepositoryImpl
import com.practicum.playlist5.settings.domain.api.SettingsRepository
import com.practicum.playlist5.sharing.data.SharingRepositoryImpl
import com.practicum.playlist5.sharing.domain.api.SharingRepository
import org.koin.android.ext.koin.androidContext

import org.koin.dsl.module


val repositoryModule = module {
    single <TrackRepository>{
        TrackRepositoryImpl(get(),get(),get())
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
    factory { TracksDbConverter() }

    single<FavouriteRepository> {
        FavouriteRepositoryImpl(get(),get())
    }

}