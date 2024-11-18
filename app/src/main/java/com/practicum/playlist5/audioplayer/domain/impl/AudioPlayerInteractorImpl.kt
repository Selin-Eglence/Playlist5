package com.practicum.playlist5.audioplayer.domain.impl


import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerRepository
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.search.domain.models.Track

class AudioPlayerInteractorImpl(
    private val audioPlayerRepository: AudioPlayerRepository
) : AudioPlayerInteractor {
    override fun preparePlayer(track: Track) {
        audioPlayerRepository.preparePlayer(track)

    }

    override fun startPlayer() {
        audioPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }

    override fun isPlaying(): Boolean {
        return audioPlayerRepository.isPlaying()
    }

    override fun getPlayerState(): PlayerState {
        return audioPlayerRepository.getPlayerState()
    }


    override fun getCurrentPosition(): Int {
        return audioPlayerRepository.getCurrentPosition()
    }


}
