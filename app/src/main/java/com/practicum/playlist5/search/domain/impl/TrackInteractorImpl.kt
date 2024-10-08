package com.practicum.playlist5.search.domain.impl

import com.practicum.playlist5.search.domain.api.TrackInteractor
import com.practicum.playlist5.search.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun search(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            try {
                consumer.consume(repository.search(expression))
            } catch (t: Throwable) {
                consumer.onFailure(t)
            }
        }

    }
}