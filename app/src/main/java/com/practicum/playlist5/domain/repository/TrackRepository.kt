package com.practicum.playlist5.domain.repository

import com.practicum.playlist5.domain.models.Track

interface TrackRepository {
    fun search(expression: String) : List <Track>
}