package com.practicum.playlist5.media.ui.playlist

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist5.R
import com.practicum.playlist5.media.domain.api.PlaylistInteractor
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class OnePlaylistViewModel(private val context: Context,
                           private val playlistInteractor: PlaylistInteractor
):ViewModel() {
    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> get() = _playlist

    private val _totalDuration = MutableLiveData<String>()
    val totalDuration: LiveData<String> get() = _totalDuration

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _trackCount = MutableLiveData<Int>()
    val trackCount: LiveData<Int> get() = _trackCount




    fun loadPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            _playlist.value = playlistInteractor.getPlaylistById(playlistId)
            _playlist.value?.let { playlist ->
                loadTracks(playlist.tracks)
            }
                }
        }


    private fun loadTracks(trackIds: List<Long>) {
        viewModelScope.launch {
            val tracks = playlistInteractor.getTracks(trackIds)
            _tracks.value = tracks
            _trackCount.value = tracks.size
            _totalDuration.value = getTotalDuration(tracks)
    }}

    fun removeTrack(trackId: Long, playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.removeTrackFromPlaylist(trackId, playlistId)
            val updatedPlaylist = playlistInteractor.getPlaylistById(playlistId)
            _playlist.postValue(updatedPlaylist)
            updatedPlaylist?.let { loadTracks(it.tracks) }
        }

    }

    private fun getTotalDuration(tracks: List<Track>): String {
        val durationMillis = tracks.sumOf { it.trackTimeMillis }
        val durationMinutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis)
        return durationTextFormater(durationMinutes.toInt())
    }



    fun removePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
        }
    }


    private fun durationTextFormater(duration: Int): String {
        val lastDigit = duration % 10
        val lastTwoDigits = duration % 100

        return when {
            lastTwoDigits in 11..14 -> String.format(context.getString(R.string.minutes), duration)
            lastDigit == 1 -> String.format(context.getString(R.string.minute), duration)
            lastDigit in 2..4 -> String.format(context.getString(R.string.minutes_s), duration)
            else -> String.format(context.getString(R.string.minutes), duration)
        }
    }







}