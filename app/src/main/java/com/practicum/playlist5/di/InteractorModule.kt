package com.practicum.playlist5.di

import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.audioplayer.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlist5.media.domain.api.FavouriteInteractor
import com.practicum.playlist5.media.domain.api.PlaylistInteractor
import com.practicum.playlist5.media.domain.impl.FavouriteInteractorImpl
import com.practicum.playlist5.media.domain.impl.PlaylistInteractorImpl

import com.practicum.playlist5.search.domain.api.SearchHistoryInteractor
import com.practicum.playlist5.search.domain.api.TrackInteractor
import com.practicum.playlist5.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlist5.search.domain.impl.TrackInteractorImpl
import com.practicum.playlist5.settings.domain.api.SettingsInteractor
import com.practicum.playlist5.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlist5.sharing.domain.SharingInteractor
import com.practicum.playlist5.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module


val interactorModule = module {
    single <TrackInteractor> {
        TrackInteractorImpl(get())
}
    single<SearchHistoryInteractor>{
        SearchHistoryInteractorImpl(get())
    }

    factory <SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    single <AudioPlayerInteractor>{
        AudioPlayerInteractorImpl(get())
    }
    single<SharingInteractor>{
        SharingInteractorImpl(get(),get())
    }

    single<FavouriteInteractor> {
        FavouriteInteractorImpl(get())
    }
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

}


