package com.practicum.playlist5.data.repository

import com.practicum.playlist5.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}