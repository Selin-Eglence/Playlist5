package com.practicum.playlist5.audioplayer.domain.api

import com.practicum.playlist5.search.domain.models.Track

interface AudioPlayerInteractor {

    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int

    fun isPlaying(): Boolean


}