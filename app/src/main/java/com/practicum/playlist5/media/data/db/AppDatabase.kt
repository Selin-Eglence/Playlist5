package com.practicum.playlist5.media.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practicum.playlist5.media.data.dao.PlaylistDao
import com.practicum.playlist5.media.data.dao.TrackDao
import com.practicum.playlist5.media.data.entity.PlaylistEntity
import com.practicum.playlist5.media.data.entity.PlaylistTrackEntity
import com.practicum.playlist5.media.data.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class,PlaylistEntity::class,PlaylistTrackEntity::class])

abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao



}