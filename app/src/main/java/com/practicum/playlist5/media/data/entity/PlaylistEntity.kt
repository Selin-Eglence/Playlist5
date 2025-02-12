package com.practicum.playlist5.media.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val imagePath: String?,
    val tracks: String,
    val tracksCount: Int)
