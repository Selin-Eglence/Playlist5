package com.practicum.playlist5.media.domain.api

import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteInteractor {
    suspend fun addTrackToFavorites(track: Track)

    suspend fun removeTrackFromFavorites(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>

    fun getID(trackId:Int): Flow<Track>

    suspend fun isFavourite(trackId: Long): Boolean

}