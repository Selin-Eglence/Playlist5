package com.practicum.playlist5.media.domain.impl

import com.practicum.playlist5.media.domain.api.FavouriteInteractor
import com.practicum.playlist5.media.domain.api.FavouriteRepository
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouriteInteractorImpl(private val favouriteRepository: FavouriteRepository):
    FavouriteInteractor {
    override suspend fun addTrackToFavorites(track: Track) {
        return favouriteRepository.addTrackToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        return favouriteRepository.removeTrackFromFavorites(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favouriteRepository.getFavoriteTracks()
    }

    override fun getID(trackId: Int): Flow<Track> {
        return getID(trackId)
    }

    override suspend fun isFavourite(trackId: Int): Boolean {
        return favouriteRepository.isFavourite(trackId)
    }


}