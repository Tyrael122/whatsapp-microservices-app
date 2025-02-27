package org.contoso.whatsapp.data.repository.networking

import org.contoso.whatsapp.data.repository.chat.ChatApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChatRetrofitClient {
    private const val BASE_URL = "http://192.168.0.73:8082/"

    val instance: ChatApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApiService::class.java)
    }
}