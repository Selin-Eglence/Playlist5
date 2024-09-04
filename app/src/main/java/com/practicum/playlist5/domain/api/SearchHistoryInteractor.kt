package com.practicum.playlist5.domain.api


import com.practicum.playlist5.domain.models.Track
import com.practicum.playlist5.domain.repository.SearchHistoryRepository
interface SearchHistoryInteractor {

    fun getTrackHistory(): List<Track>

    fun addTrack(track: Track)

    fun clearTrackHistory()

    fun updateHistory()
}
