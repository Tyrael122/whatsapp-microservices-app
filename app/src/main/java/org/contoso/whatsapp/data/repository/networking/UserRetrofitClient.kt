package org.contoso.whatsapp.data.repository.networking

import org.contoso.whatsapp.data.repository.users.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserRetrofitClient {
    private const val BASE_URL = "http://192.168.0.73:8081/"

    val instance: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }
}