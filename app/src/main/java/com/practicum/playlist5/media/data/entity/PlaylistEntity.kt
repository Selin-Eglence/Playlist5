package com.practicum.playlist5.media.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val name: String,
    val description: String,
    val imagePath: String?,
    val tracks: String = Gson().toJson(emptyList<Long>()),
    val tracksCount: Int = 0)
