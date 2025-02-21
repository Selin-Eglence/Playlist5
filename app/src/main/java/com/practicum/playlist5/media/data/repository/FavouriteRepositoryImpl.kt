package com.practicum.playlist5.media.data.repository

import com.practicum.playlist5.media.data.converter.TracksDbConverter
import com.practicum.playlist5.media.data.db.AppDatabase
import com.practicum.playlist5.media.data.entity.TrackEntity
import com.practicum.playlist5.media.domain.api.FavouriteRepository
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteRepositoryImpl(private val appDatabase: AppDatabase,
    private val tracksDbConverter: TracksDbConverter): FavouriteRepository {

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }
     private fun convertFromTrackEntity(tracks:List<TrackEntity>): List<Track> {
         return tracks.map{ track -> tracksDbConverter.map(track)}
     }

    override suspend fun addTrackToFavorites(track: Track) {
        val trackEntity = tracksDbConverter.map(track)
        appDatabase.trackDao().insertTracks(trackEntity)
    }
    override suspend fun removeTrackFromFavorites(track: Track) {
        appDatabase.trackDao().deleteTracks(tracksDbConverter.map(track))
    }

    override fun getID(trackId:Int): Flow<Track> = flow {
            val track = appDatabase.trackDao().getId(trackId)
            emit(tracksDbConverter.map(track))
        }

    override suspend fun isFavourite(trackId: Long): Boolean {
       return appDatabase.trackDao().isFavourite(trackId)
    }
}

