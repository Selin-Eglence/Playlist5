package com.practicum.playlist5.media.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlist5.media.data.entity.PlaylistEntity
import com.practicum.playlist5.media.data.entity.PlaylistTrackEntity
import com.practicum.playlist5.media.ui.playlist.Playlist
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylistsFlow(): Flow<List<PlaylistEntity>>

    @Query("DELETE FROM playlist_table WHERE id = :id")
    suspend fun deletePlaylistById(id: Long)


    @Query("SELECT * FROM playlist_tracks_table WHERE trackId = :trackId")
    suspend fun getTracks(trackId: Long): PlaylistTrackEntity

    @Query("DELETE FROM playlist_tracks_table WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)

    @Query("SELECT COUNT(*) > 0 FROM playlist_tracks_table WHERE trackId = :trackId")
    suspend fun checkTrackInOtherPlaylists(trackId: Long): Boolean










}