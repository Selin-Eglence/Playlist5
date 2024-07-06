package com.practicum.playlist5

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface TrackAPI {
    @GET ("search?entity=song")
    fun search(@Query("term") text: String?) : Call <TrackResponse>
}