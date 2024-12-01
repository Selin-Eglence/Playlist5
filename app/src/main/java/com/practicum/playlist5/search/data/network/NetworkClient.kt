package com.practicum.playlist5.search.data.network

import com.practicum.playlist5.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}