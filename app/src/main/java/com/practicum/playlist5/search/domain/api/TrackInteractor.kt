package com.practicum.playlist5.search.domain.api

import com.practicum.playlist5.search.domain.models.Track

interface TrackInteractor {
    fun search(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTrack: List<Track>)
        fun onFailure(t: Throwable)

    }
}