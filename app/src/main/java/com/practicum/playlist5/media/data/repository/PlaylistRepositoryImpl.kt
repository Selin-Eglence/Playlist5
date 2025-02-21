package com.practicum.playlist5.media.data.repository


import com.practicum.playlist5.media.data.converter.PlaylistDbConverter
import com.practicum.playlist5.media.data.converter.PlaylistTrackDbConverter
import com.practicum.playlist5.media.data.db.AppDatabase
import com.practicum.playlist5.media.data.entity.PlaylistEntity
import com.practicum.playlist5.media.domain.api.PlaylistRepository
import com.practicum.playlist5.media.ui.playlist.Playlist
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val playlistTrackDbConverter: PlaylistTrackDbConverter
) : PlaylistRepository {

    override suspend fun addNewPlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist)
        try {
            appDatabase.playlistDao().insertPlaylist(playlistEntity)
        } catch (e: Exception) {
            throw e
        }
    }


    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getAllPlaylists()
        emit(convertFromEntity(playlists))
    }

    override fun getPlaylistsFlow(): Flow<List<Playlist>> = flow {
    appDatabase.playlistDao().getAllPlaylistsFlow().collect { playlists ->
        emit(convertFromEntity(playlists))}}



    private fun convertFromEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { track ->
            playlistDbConverter.map(track)
        }
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        withContext(Dispatchers.IO) {
            val updatedTracks = (playlist.tracks + track.trackId).distinct()
            val updatedPlaylist = playlist.copy(tracks = updatedTracks, trackNum = updatedTracks.size)

            try {

            appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(updatedPlaylist))
            appDatabase.playlistDao().insertPlaylistTrack(playlistTrackDbConverter.map(track))
        } catch (e: Exception) {
            throw e
        }
    }}}
