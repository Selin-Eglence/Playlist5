package com.practicum.playlist5.search.domain.api

import com.practicum.playlist5.search.domain.models.Track

interface TrackRepository {
    fun search(expression: String) : List <Track>
}