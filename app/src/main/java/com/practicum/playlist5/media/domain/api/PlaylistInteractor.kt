package com.practicum.playlist5.media.domain.api

import com.practicum.playlist5.media.ui.model.Playlist
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addNewPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylistsFlow(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlistId: Playlist, track: Track)
}