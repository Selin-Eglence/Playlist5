package com.practicum.playlist5.domain.api

import com.practicum.playlist5.domain.models.Track

interface TrackRepository {
    fun search(expression: String) : List <Track>
}