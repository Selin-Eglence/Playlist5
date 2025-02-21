package com.practicum.playlist5.media.domain.impl

import com.practicum.playlist5.media.domain.api.PlaylistInteractor
import com.practicum.playlist5.media.domain.api.PlaylistRepository
import com.practicum.playlist5.media.ui.playlist.Playlist
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository):PlaylistInteractor {
    override suspend fun addNewPlaylist(playlist: Playlist) {
        playlistRepository.addNewPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun getPlaylistsFlow(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylistsFlow()
    }

    override suspend fun addTrackToPlaylist(playlistId: Playlist, track: Track) {
        return playlistRepository.addTrackToPlaylist(playlistId, track)
    }





}