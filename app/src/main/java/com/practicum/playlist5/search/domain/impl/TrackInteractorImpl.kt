package com.practicum.playlist5.search.domain.impl

import com.practicum.playlist5.search.domain.api.Resource
import com.practicum.playlist5.search.domain.api.TrackInteractor
import com.practicum.playlist5.search.domain.api.TrackRepository
import com.practicum.playlist5.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun search(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.search(expression).map {result ->
            when (result){
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }

            }
        }

    }
}