package com.practicum.playlist5.domain.repository

import com.practicum.playlist5.domain.models.Track

interface SearchHistoryRepository {
    fun getTrackHistory(): List<Track>
    fun addTrack(track: Track)
    fun clearTrackHistory()
}