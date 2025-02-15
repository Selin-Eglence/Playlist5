package com.practicum.playlist5.media.ui.newplaylist

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist5.media.domain.api.PlaylistInteractor
import com.practicum.playlist5.media.ui.playlist.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {


    private val _playlistName = MutableLiveData<String>()

    private val _playlistDescription = MutableLiveData<String?>()

    private var _coverImageUri: Uri? = null
    fun setCoverImageUri(uri: Uri?) {
        _coverImageUri = uri
    }

    private val _savePlaylistResult = MutableLiveData<Result<Unit>>()
    val savePlaylistResult: LiveData<Result<Unit>> = _savePlaylistResult

    fun savePlaylist() {
        val name = _playlistName.value ?: ""
        Log.e("name", "insert")
        val description = _playlistDescription.value

        viewModelScope.launch {
            try {
                val playlist = description?.let {
                    Playlist(
                        name = name,
                        description = it,
                        imagePath = _coverImageUri?.toString() ?: "",
                        tracks = emptyList(),
                    )

                }
                if (playlist != null) {
                    playlistInteractor.addNewPlaylist(playlist)
                    Log.d("playlist", "был создан")
                    playlistInteractor.getPlaylists()
                }
                _savePlaylistResult.value = Result.success(Unit)
                Log.d("playlist","added")
            } catch (e: Exception) {
                _savePlaylistResult.value = Result.failure(e)
            }
        }
    }



    fun setPlaylistName(name: String) {
        _playlistName.value = name
    }
    fun getCurrentPlaylistName(): String {
        return _playlistName.value.orEmpty()
    }

    fun setPlaylistDescription(description: String?) {
        _playlistDescription.value = description
    }


}