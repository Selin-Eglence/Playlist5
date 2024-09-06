package com.practicum.playlist5.data.network

import com.practicum.playlist5.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface TrackAPI {
    @GET ("search?entity=song")
    fun search(@Query("term") text: String?) : Call <TrackResponse>
}