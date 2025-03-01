package com.practicum.playlist5.audioplayer.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist5.audioplayer.domain.api.AudioPlayerInteractor
import com.practicum.playlist5.audioplayer.domain.models.PlayerState
import com.practicum.playlist5.di.repositoryModule
import com.practicum.playlist5.media.domain.api.FavouriteInteractor
import com.practicum.playlist5.media.domain.api.PlaylistInteractor
import com.practicum.playlist5.media.ui.playlist.Playlist
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.log

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favouriteTrackInteractor: FavouriteInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var timerJob: Job?= null

    private val _trackData = MutableLiveData<Track>()

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private val _playbackState = MutableLiveData(
        ScreenState(progressText = dateFormat.format(audioPlayerInteractor.getCurrentPosition()),
            playerState = audioPlayerInteractor.getPlayerState())
    )
    val playbackState: LiveData<ScreenState> = _playbackState

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean>get() = _isFavourite

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists


    private val _currentPlaylistName = MutableLiveData<String?>()
    val currentPlaylistName: LiveData<String?> = _currentPlaylistName

    private val _addedToPlaylistState = MutableLiveData<AddToPlaylist>()
    val addedToPlaylistState: LiveData<AddToPlaylist> = _addedToPlaylistState



    fun onFavouriteClicked(track: Track) {

        viewModelScope.launch {
            if (track.isFavorite) {
                favouriteTrackInteractor.addTrackToFavorites(track)
            } else {
                favouriteTrackInteractor.removeTrackFromFavorites(track)

            }
            _isFavourite.postValue(track.isFavorite)
        }
        updateIsFavourite(track.trackId)

    }

    private fun updateIsFavourite(trackId: Long) {
        viewModelScope.launch {
            val isFavourite = favouriteTrackInteractor.isFavourite(trackId)
            _isFavourite.postValue(isFavourite)
        }
    }


    fun setTrack(track: Track) {
        timerJob?.cancel()
        timerJob = null
        _trackData.value = track
        audioPlayerInteractor.preparePlayer(track)
        _playbackState.value = ScreenState(
            progressText = dateFormat.format(0),
            playerState = PlayerState.STATE_DEFAULT
        )
        if (audioPlayerInteractor.getPlayerState() == PlayerState.STATE_PREPARED) {
            startPlayer()
        }
        updateIsFavourite(track.trackId)
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


    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        _playbackState.value = ScreenState(
            progressText = dateFormat.format(audioPlayerInteractor.getCurrentPosition()),
            playerState = PlayerState.STATE_PAUSED
        )
        timerJob?.cancel()
    }




    fun onDestroy(track: Track) {
        audioPlayerInteractor.pausePlayer()
        timerJob?.cancel()
        _playbackState.value = ScreenState(
            progressText = dateFormat.format(0),
            playerState = PlayerState.STATE_DEFAULT
        )
        updateIsFavourite(track.trackId)

    }


    private fun startTimer() {
        timerJob?.cancel()
        timerJob = null
        timerJob=viewModelScope.launch {
            while  (audioPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                val currentTime = dateFormat.format(audioPlayerInteractor.getCurrentPosition())
                delay(TIMER_UPDATE_DELAY)
                _playbackState.value = ScreenState(
                    progressText = currentTime,
                    playerState = PlayerState.STATE_PLAYING
                )

            }
        }}






    fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            val isInPlaylist = isTrackInPlaylist(playlist, track)
            try {
                Log.d("PlaylistViewModel", "Трек ${track.trackId} уже в плейлисте? $isInPlaylist")
                if (!isInPlaylist) {
                    val updatedPlaylist = playlist.copy(
                        tracks = playlist.tracks + listOf(track.trackId),
                        trackNum = playlist.tracks.size+1
                    )
                    playlistInteractor.addTrackToPlaylist(updatedPlaylist, track)
                    Log.d("track", "добавлен в плейлист")
                    _addedToPlaylistState.value= AddToPlaylist(true,playlist)
                } else {
                    _addedToPlaylistState.value =AddToPlaylist(false,playlist)
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun isTrackInPlaylist(playlist: Playlist, track: Track): Boolean {
        return playlist.tracks.contains(track.trackId)

    }


    fun loadPlaylists() {
        Log.d("playlist", "загружен")
        viewModelScope.launch {
            playlistInteractor.getPlaylists()
            try {
                playlistInteractor.getPlaylists().collect { playlists ->
                    _playlists.postValue(playlists)

                }
            } catch (e: Exception) {
                throw e
            }
        }
    }






    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
    }
}


