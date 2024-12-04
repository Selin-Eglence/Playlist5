package com.practicum.playlist5.di

import com.practicum.playlist5.audioplayer.ui.AudioPlayerViewModel
import com.practicum.playlist5.media.FavouriteViewModel
import com.practicum.playlist5.search.ui.SearchViewModel
import com.practicum.playlist5.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { AudioPlayerViewModel(get(),get()) }

    viewModel { SearchViewModel(get(), get()) }

    viewModel { SettingsViewModel(get(), get()) }

    viewModel { FavouriteViewModel(androidContext(),get()) }
}

