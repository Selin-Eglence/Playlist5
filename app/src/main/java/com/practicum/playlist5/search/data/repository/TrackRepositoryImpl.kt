package com.practicum.playlist5.search.data.repository

import com.practicum.playlist5.search.data.dto.TrackRequest
import com.practicum.playlist5.search.data.dto.TrackResponse
import com.practicum.playlist5.search.data.network.NetworkClient
import com.practicum.playlist5.search.domain.api.Resource
import com.practicum.playlist5.search.domain.api.TrackRepository
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun search(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackRequest(expression))
        when (response.resultCode) {
            -1 -> {emit(Resource.Error("Network Error"))}
            200 -> {
                emit(Resource.Success((response as TrackResponse).results.map {
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
                }))
            }

        else -> {
            emit(Resource.Error("Network error with code: ${response.resultCode}"))
        }
    }
}}