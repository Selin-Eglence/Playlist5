package com.practicum.playlist5.audioplayer.domain.api

import com.practicum.playlist5.search.domain.models.Track

interface AudioPlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun onPause()
    fun playbackControl()
    fun onDestroy()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean

}