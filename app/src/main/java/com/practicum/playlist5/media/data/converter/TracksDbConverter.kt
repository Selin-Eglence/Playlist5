package com.practicum.playlist5.media.data.converter

import com.practicum.playlist5.media.data.entity.TrackEntity
import com.practicum.playlist5.search.data.dto.TrackDto
import com.practicum.playlist5.search.domain.models.Track

class TracksDbConverter {
    fun map(trackDto:TrackDto): TrackEntity {
        return TrackEntity(
            trackDto.trackId,
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl,
            false,
        )
    }

    fun map(track: TrackEntity):Track{
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavourite
            )
    }

    fun map (track: Track):TrackEntity{
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite,
        )
    }
}