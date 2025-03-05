package com.practicum.playlist5.audioplayer.data

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerRepository
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.search.domain.models.Track
import java.io.IOException


class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    private var mediaPlayer: MediaPlayer? = null
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT

    var onPlayerPrepared: (() -> Unit)? = null

    init {
        initializeMediaPlayer()
    }

    private fun initializeMediaPlayer() {
        mediaPlayer?.release()  // Освобождаем старый экземпляр, если он есть
        mediaPlayer = MediaPlayer().apply {
            setOnPreparedListener {
                Log.d("AudioPlayer", "✅ MediaPlayer готов!")
                playerState = PlayerState.STATE_PREPARED
                onPlayerPrepared?.invoke()
            }
            setOnCompletionListener {
                playerState = PlayerState.STATE_COMPLETED
            }
            setOnErrorListener { mp, what, extra ->
                Log.e("AudioPlayer", "Ошибка MediaPlayer: what=$what, extra=$extra")
                resetMediaPlayer()
                true
            }
        }
    }

    override fun preparePlayer(track: Track) {
        try {
            resetMediaPlayer()
            mediaPlayer?.apply {
                setDataSource(track.previewUrl)
                prepareAsync()
            }
        } catch (e: IOException) {
            Log.e("MediaPlayerError", "Ошибка при загрузке трека: ${e.message}")
        } catch (e: IllegalStateException) {
            Log.e("MediaPlayerError", "MediaPlayer в неправильном состоянии: ${e.message}")
            resetMediaPlayer()
            preparePlayer(track)
        }
    }

    override fun startPlayer() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                playerState = PlayerState.STATE_PLAYING
                Log.d("AudioPlayer", "▶ MediaPlayer запущен!")
            }
        } ?: Log.e("AudioPlayer", "❌ MediaPlayer = null при попытке запуска!")
    }

    override fun pausePlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                playerState = PlayerState.STATE_PAUSED
                Log.d("AudioPlayer", "⏸ Плеер на паузе")
            }
        }
    }

    override fun onPause() {
        mediaPlayer?.pause()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        playerState = PlayerState.STATE_DEFAULT
        Log.d("AudioPlayer", "🛑 MediaPlayer уничтожен!")
    }

    private fun resetMediaPlayer() {
        mediaPlayer?.apply {
            reset()
            playerState = PlayerState.STATE_DEFAULT
        } ?: initializeMediaPlayer()
    }
}
