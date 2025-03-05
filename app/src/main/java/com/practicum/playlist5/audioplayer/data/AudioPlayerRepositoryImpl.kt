package com.practicum.playlist5.audioplayer.data

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerRepository
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.search.domain.models.Track
import java.io.IOException

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {

    private var playerState: PlayerState = PlayerState.STATE_DEFAULT

    var onPlayerPrepared: (() -> Unit)? = null

    override fun preparePlayer(track: Track) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset()  // Reset player before reusing
            }
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()

            mediaPlayer.setOnPreparedListener {
                Log.d("AudioPlayer", "✅ MediaPlayer готов!")
                playerState = PlayerState.STATE_PREPARED
                onPlayerPrepared?.invoke()
            }

            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.STATE_COMPLETED
            }

        } catch (e: IOException) {
            Log.e("MediaPlayerError", " Ошибка при загрузке трека: ${e.message}")
        } catch (e: IllegalStateException) {
            Log.e("MediaPlayerError", "MediaPlayer в неправильном состоянии: ${e.message}")
            mediaPlayer.reset()
            preparePlayer(track)
        }
    }

    override fun startPlayer() {
        if (playerState == PlayerState.STATE_PREPARED || playerState == PlayerState.STATE_PAUSED  ) {
            mediaPlayer.start()
            playerState = PlayerState.STATE_PLAYING
            Log.d("AudioPlayer", "▶ MediaPlayer запущен!")
        } else {
            Log.e("AudioPlayer", "❌ Попытка запустить плеер в состоянии: $playerState")
        }
    }

    override fun pausePlayer() {
            mediaPlayer.pause()
            playerState = PlayerState.STATE_PAUSED
            Log.d("AudioPlayer", "⏸ Плеер на паузе")

    }



    override fun getCurrentPosition(): Int {
        return try {
            mediaPlayer.currentPosition
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayer", "Error getting position: ${e.message}")
            0
        }
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getPlayerState(): PlayerState {
        return playerState

    }

    override fun onPause() {
        mediaPlayer.pause()
    }


    override fun onDestroy() {
        mediaPlayer.reset()
        mediaPlayer.release()
        playerState = PlayerState.STATE_COMPLETED

    }





}

