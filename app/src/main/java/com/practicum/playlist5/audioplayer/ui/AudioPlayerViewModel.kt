package com.practicum.playlist5.audioplayer.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.search.domain.models.Track
import com.practicum.playlist5.utils.Creator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val _trackData = MutableLiveData<Track>()
    val trackData: LiveData<Track> = _trackData

    private val _playbackState = MutableLiveData<Int>(STATE_DEFAULT)
    val playbackState: LiveData<Int> get() = _playbackState

    private val _progressText = MutableLiveData<String>()
    val progressText: LiveData<String> = _progressText

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    fun setTrack(track: Track) {
        _trackData.value = track
        audioPlayerInteractor.preparePlayer(track)
    }

    fun playbackControl() {
        if (_playbackState.value == STATE_PLAYING) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        _playbackState.value = STATE_PLAYING
        handler.post(timer)
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        _playbackState.value = STATE_PAUSED
        handler.removeCallbacks(timer)
    }

    fun onDestroy()
    {
        _playbackState.value = STATE_COMPLETED
        handler.removeCallbacks(timer)
    }

    private val timer by lazy {
        object : Runnable {
            override fun run() {
                if (_playbackState.value == STATE_PLAYING) {
                    _progressText.value = dateFormat.format(audioPlayerInteractor.getCurrentPosition())
                    handler.postDelayed(this, TIMER_UPDATE_DELAY)
                }
            }
        }
    }


    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val STATE_COMPLETED = 4

        private const val TIMER_UPDATE_DELAY = 250L


        fun getViewModelFactory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    AudioPlayerViewModel(
                        audioPlayerInteractor = Creator.provideAudioPlayerInteractor()
                    )
                }
            }

        }
    }

}
