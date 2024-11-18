package com.practicum.playlist5.search.domain.api

import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun search(expression: String): Flow<Pair<List<Track>?, String?>>


}