package com.practicum.playlist5.data

import com.practicum.playlist5.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}