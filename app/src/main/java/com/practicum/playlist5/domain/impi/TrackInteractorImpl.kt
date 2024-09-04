package com.practicum.playlist5.domain.impi

import com.practicum.playlist5.domain.api.TrackInteractor
import com.practicum.playlist5.domain.repository.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun search(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            try {
            consumer.consume(repository.search(expression))}

            catch (t:Throwable){
                consumer.onFailure(t) }
        }

    }
}