package com.practicum.playlist5.data.repository

import com.practicum.playlist5.data.dto.TrackRequest
import com.practicum.playlist5.data.dto.TrackResponse
import com.practicum.playlist5.domain.repository.TrackRepository
import com.practicum.playlist5.domain.models.Track
import java.io.IOException

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun search(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            throw IOException("Network error with code: ${response.resultCode}")
        }
    }
}