package com.practicum.playlist5.audioplayer.domain.api

import android.media.MediaPlayer.OnCompletionListener
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.search.domain.models.Track

interface AudioPlayerInteractor {

    fun preparePlayer(track: Track )

    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int

    fun isPlaying(): Boolean

    fun getPlayerState():PlayerState





}