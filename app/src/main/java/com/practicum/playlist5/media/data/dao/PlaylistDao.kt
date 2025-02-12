package com.practicum.playlist5.media.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlist5.media.data.entity.PlaylistEntity
import com.practicum.playlist5.media.data.entity.PlaylistTrackEntity
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
    fun getPlaylistById(id: Int): PlaylistEntity

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
     fun updatePlaylist(playlist: PlaylistEntity)


    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylistsFlow(): Flow<List<PlaylistEntity>>
}