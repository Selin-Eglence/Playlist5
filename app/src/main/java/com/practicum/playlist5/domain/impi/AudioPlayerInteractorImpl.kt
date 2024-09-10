package com.practicum.playlist5.domain.impi


import com.practicum.playlist5.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.domain.repository.AudioPlayerRepository
import com.practicum.playlist5.domain.models.Track

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


    override fun getCurrentPosition(): Int {
        return audioPlayerRepository.getCurrentPosition()
    }


}
