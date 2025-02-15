package com.practicum.playlist5.media.ui.playlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist5.R
import com.practicum.playlist5.media.domain.api.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val context: Context,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData


    fun fillData() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists()
                .collect { playlists ->
                    Log.d("PlaylistViewModel", "Получено ${playlists.size} плейлистов")
                    processResult(playlists)
                }
        }
    }


    private fun processResult(playlists: List<Playlist>) {
        Log.d("PlaylistViewModel", "Пришло плейлистов: ${playlists.size}")
        playlists.forEach { Log.d("PlaylistViewModel", "Плейлист: ${it.name}") }

        if (playlists.isEmpty()) {
            renderState(PlaylistState.Empty(context.getString(R.string.no_playlist)))
        } else {
            renderState(PlaylistState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }}
