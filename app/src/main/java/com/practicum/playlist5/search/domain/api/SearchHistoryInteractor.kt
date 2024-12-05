package com.practicum.playlist5.search.domain.api


import com.practicum.playlist5.search.domain.models.Track

interface SearchHistoryInteractor {

    fun getTrackHistory(): MutableList<Track>

    fun addTrack(track: Track)

    fun clearTrackHistory()

    fun updateHistory()
}
