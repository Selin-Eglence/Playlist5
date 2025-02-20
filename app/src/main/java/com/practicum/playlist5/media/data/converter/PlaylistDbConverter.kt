package com.practicum.playlist5.media.data.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist5.media.data.entity.PlaylistEntity
import com.practicum.playlist5.media.ui.playlist.Playlist


class PlaylistDbConverter {
    private val gson = Gson()
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imagePath = playlist.imagePath,
            tracks = gson.toJson(playlist.tracks),
            tracksCount = playlist.trackNum,
        )
    }

    fun map(entity: PlaylistEntity): Playlist {
        val trackListType = object : TypeToken<List<Long>>() {}.type
        return Playlist(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            imagePath = entity.imagePath,
            tracks = gson.fromJson(entity.tracks, trackListType),
            trackNum = entity.tracksCount
        )
    }

    fun createJsonFromTracks(tracks: List<Long>): String {
        return Gson().toJson(tracks)
    }

}
