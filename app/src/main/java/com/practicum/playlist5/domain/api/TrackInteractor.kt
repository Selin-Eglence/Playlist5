package com.practicum.playlist5.domain.api

import com.practicum.playlist5.domain.models.Track

interface TrackInteractor {
    fun search(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTrack: List<Track>)
        fun onFailure(t: Throwable)

    }
}