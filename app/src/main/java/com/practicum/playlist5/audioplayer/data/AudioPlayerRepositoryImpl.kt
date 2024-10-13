package com.practicum.playlist5.audioplayer.data

import android.annotation.SuppressLint
import android.media.MediaPlayer
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerRepository
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.search.domain.models.Track

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {



    private var playerState: PlayerState = PlayerState.STATE_DEFAULT


    @SuppressLint("ResourceType")
    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED

        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED  -> {
                startPlayer()
            }

            else -> {}
        }
    }


    override fun onPause() {
        pausePlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getPlayerState(): PlayerState {
        return playerState

    }


}