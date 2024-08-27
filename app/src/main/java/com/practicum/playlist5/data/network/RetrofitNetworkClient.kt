package com.practicum.playlist5.data.network

import com.practicum.playlist5.data.NetworkClient
import com.practicum.playlist5.data.dto.Response
import com.practicum.playlist5.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val ItunesBaseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TrackAPI::class.java)
    override fun doRequest(dto: Any): Response {
        if (dto is TrackRequest) {
            val resp = trackService.search(dto.expression).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        }
        else {
            return Response().apply {
                resultCode = 400

            }
        }
    }
}