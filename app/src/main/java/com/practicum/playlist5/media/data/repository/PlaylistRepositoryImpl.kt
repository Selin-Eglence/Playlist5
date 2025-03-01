package com.practicum.playlist5.media.data.repository


import android.util.Log
import com.google.gson.Gson
import com.practicum.playlist5.media.data.converter.PlaylistDbConverter
import com.practicum.playlist5.media.data.converter.PlaylistTrackDbConverter
import com.practicum.playlist5.media.data.db.AppDatabase
import com.practicum.playlist5.media.data.entity.PlaylistEntity
import com.practicum.playlist5.media.data.entity.PlaylistTrackEntity
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
    }}

    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        val playlistEntity = appDatabase.playlistDao().getPlaylistById(playlistId)
        return playlistEntity?.let { playlistDbConverter.map(it) }
    }


    override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long) {
        val playlist = getPlaylistById(playlistId) ?: return
        val tracks = playlist.tracks.toMutableList()
        tracks.remove(trackId)
        val updatedPlaylist = playlist.copy(tracks = tracks.toList(), trackNum = tracks.size)
        Log.d("MY_TAG", tracks.size.toString())
        updatePlaylist(updatedPlaylist)

        val playlists = appDatabase.playlistDao().getAllPlaylists()
        var isTrackInOtherPlaylist = false
        for (playlist in playlists) {
            if (playlist.tracks.contains(trackId.toString())) {
                isTrackInOtherPlaylist = true
                break
            }
        }
        if (!isTrackInOtherPlaylist) {
            appDatabase.playlistDao().deleteTrackById(trackId)
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val gson = Gson()
        val playlistEntity =  PlaylistEntity (
        id = playlist.id,
        name = playlist.name,
        description = playlist.description,
        imagePath = playlist.imagePath,
        tracks = gson.toJson(playlist.tracks),
        tracksCount = playlist.trackNum,
        )
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    override suspend fun getTracks(ids: List<Long>): List<Track> {
        val tracks = ids.map { id ->
            appDatabase.playlistDao().getTracks(id)
        }
        return convertFromPlaylistTrackEntity(tracks.reversed())

    }

    private fun convertFromPlaylistTrackEntity(tracks: List<PlaylistTrackEntity>): List<Track> {
        return tracks.map { track ->
            playlistTrackDbConverter.map(track)
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist)
        appDatabase.playlistDao().deletePlaylistById(playlistEntity.id)
        for (trackId in playlist.tracks) {
            isTrackInOtherPlaylists(trackId)
        } }



    private suspend fun isTrackInOtherPlaylists(trackId:Long){
        val playlists = appDatabase.playlistDao().getAllPlaylists()
        var isTrackInOtherPlaylist = false
        for (playlist in playlists) {
            if (playlist.tracks.contains(trackId.toString())) {
                isTrackInOtherPlaylist = true
                break
            }
        }

        if (!isTrackInOtherPlaylist) {
            appDatabase.playlistDao().deleteTrackById(trackId)
        }
    }


}
