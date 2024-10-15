package com.practicum.playlist5.audioplayer.data

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerRepository
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.search.domain.models.Track
import java.io.IOException
import kotlin.concurrent.timer

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {



    private var playerState: PlayerState = PlayerState.STATE_DEFAULT


    @SuppressLint("ResourceType")
    override fun preparePlayer(track: Track) {
        try {
            if (mediaPlayer!= null) {
                mediaPlayer!!.reset()
            }
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = PlayerState.STATE_PREPARED

            }}
        catch (e: IllegalStateException) {
            mediaPlayer?.release()
            preparePlayer(track)
        }
        catch (e: IOException) {
            Log.e("MediaPlayerError", "Error setting data source: ${e.message}")
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
        mediaPlayer.pause()
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