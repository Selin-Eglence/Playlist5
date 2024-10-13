package com.practicum.playlist5.search.data.network

import com.practicum.playlist5.search.data.dto.Response
import com.practicum.playlist5.search.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitNetworkClient(private val trackService: TrackAPI) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        return  try {
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
        catch (e: IOException) {
            Response().apply {
                resultCode = 500
            }
    }
    }

}