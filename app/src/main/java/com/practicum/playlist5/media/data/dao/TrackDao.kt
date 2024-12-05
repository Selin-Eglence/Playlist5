package com.practicum.playlist5.media.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlist5.media.data.entity.TrackEntity


@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(track:TrackEntity)

    @Delete(entity=TrackEntity::class)
    suspend fun deleteTracks(track:TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId = :trackId"
    )
    suspend fun getId(trackId:Int):TrackEntity

    @Query("SELECT COUNT(*) > 0 FROM track_table WHERE trackId = :trackId")
    suspend fun isFavourite(trackId: Int): Boolean

}