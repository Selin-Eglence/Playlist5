package com.practicum.playlist5.domain.impi

import com.practicum.playlist5.domain.api.SearchHistoryInteractor
import com.practicum.playlist5.domain.models.Track
import com.practicum.playlist5.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository):
    SearchHistoryInteractor {

    override fun getTrackHistory(): List<Track> {
        return repository.getTrackHistory()
    }

    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun clearTrackHistory() {
        repository.clearTrackHistory()
    }

    override fun updateHistory() {
        repository.updateHistory()
    }
}