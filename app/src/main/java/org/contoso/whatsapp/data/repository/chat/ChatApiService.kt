package org.contoso.whatsapp.data.repository.chat

import org.contoso.whatsapp.data.models.Chat
import org.contoso.whatsapp.data.models.ChatRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApiService {

    @GET("chats")
    suspend fun getChats(): List<Chat>

    @POST("chats")
    suspend fun createChat(@Body chat: ChatRequest): Chat
}