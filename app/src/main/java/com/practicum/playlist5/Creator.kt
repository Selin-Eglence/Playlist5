package com.practicum.playlist5

import com.practicum.playlist5.data.network.RetrofitNetworkClient
import com.practicum.playlist5.data.network.TrackRepositoryImpl
import com.practicum.playlist5.domain.api.TrackInteractor
import com.practicum.playlist5.domain.api.TrackRepository
import com.practicum.playlist5.domain.impi.TrackInteractorImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }
}