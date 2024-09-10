package com.practicum.playlist5.domain.api

import com.practicum.playlist5.domain.models.Track

interface AudioPlayerInteractor {

    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int

    fun isPlaying(): Boolean


}