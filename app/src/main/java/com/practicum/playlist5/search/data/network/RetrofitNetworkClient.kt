package com.practicum.playlist5.search.data.network

import com.practicum.playlist5.search.data.dto.Response
import com.practicum.playlist5.search.data.dto.TrackRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitNetworkClient(private val trackService: TrackAPI) : NetworkClient {


    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TrackRequest) {
            return Response().apply {
                resultCode = 400

            }}

        return withContext(Dispatchers.IO) {
            try {
                val response = trackService.search(dto.expression)
                response.apply { resultCode = 200 }
            }
            catch (e:Throwable) {
            Response().apply {
                resultCode = 500
            }
    }
    }

}}