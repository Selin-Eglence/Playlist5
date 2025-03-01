package com.practicum.playlist5.media.domain.api

import com.practicum.playlist5.media.ui.playlist.Playlist
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addNewPlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylistsFlow(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlistId: Playlist, track: Track)

    suspend fun getPlaylistById(playlistId: Long): Playlist?

    suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun getTracks(ids: List<Long>): List<Track>

    suspend fun deletePlaylist(playlist: Playlist)


}


