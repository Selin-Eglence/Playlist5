package com.practicum.playlist5.search.data.network

import com.practicum.playlist5.search.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface TrackAPI {
    @GET ("search?entity=song")
    suspend fun search(@Query("term") text: String?) : TrackResponse
}