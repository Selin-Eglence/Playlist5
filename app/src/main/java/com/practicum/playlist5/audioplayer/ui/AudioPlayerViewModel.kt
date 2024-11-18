package com.practicum.playlist5.audioplayer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private var timerJob: Job?= null


    private val _trackData = MutableLiveData<Track>()

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private val _playbackState = MutableLiveData(
        ScreenState(progressText = dateFormat.format(audioPlayerInteractor.getCurrentPosition()),
            playerState = audioPlayerInteractor.getPlayerState())
    )
    val playbackState: LiveData<ScreenState> = _playbackState

    fun setTrack(track: Track) {
        _trackData.value = track
        audioPlayerInteractor.preparePlayer(track)
    }


    fun playbackControl() {
        if (audioPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }


    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        _playbackState.value = ScreenState(
            progressText = dateFormat.format(audioPlayerInteractor.getCurrentPosition()),
            playerState = PlayerState.STATE_PLAYING
        )
        startTimer()
    }


    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        _playbackState.value = ScreenState(
            progressText = dateFormat.format(audioPlayerInteractor.getCurrentPosition()),
            playerState = PlayerState.STATE_PAUSED
        )
        timerJob?.cancel()
    }


    fun onDestroy() {
        _playbackState.value = ScreenState(
            progressText = dateFormat.format(audioPlayerInteractor.getCurrentPosition()),
            playerState = PlayerState.STATE_COMPLETED
        )
        timerJob?.cancel()
        timerJob = null
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = null
        timerJob=viewModelScope.launch {
            while  (audioPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                val currentTime = dateFormat.format(audioPlayerInteractor.getCurrentPosition())
                _playbackState.value = ScreenState(
                    progressText = currentTime,
                    playerState = PlayerState.STATE_PLAYING
                )
                delay(TIMER_UPDATE_DELAY)

        }
    }}



    companion object {
        private const val TIMER_UPDATE_DELAY = 300L


    }
}

