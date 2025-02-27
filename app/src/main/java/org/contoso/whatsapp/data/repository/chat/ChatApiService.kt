package org.contoso.whatsapp.data.repository.chat

import org.contoso.whatsapp.data.models.Chat
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApiService {

    @GET("chats")
    suspend fun getChats(): List<Chat>

    @POST("chats")
    suspend fun createChat(name: String): Chat
}