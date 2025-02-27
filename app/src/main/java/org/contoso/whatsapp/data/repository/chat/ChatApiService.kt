package org.contoso.whatsapp.data.repository.chat

import org.contoso.whatsapp.data.models.Chat
import org.contoso.whatsapp.data.models.ChatCreationRequest
import org.contoso.whatsapp.data.models.ChatUpdateRequest
import org.contoso.whatsapp.data.models.ChatUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {

    @GET("chats")
    suspend fun getChats(): List<Chat>

    @POST("chats")
    suspend fun createChat(@Body chat: ChatCreationRequest): Chat

    @POST("chats/{chatId}")
    suspend fun updateChat(@Path("chatId") chatId: String, @Body updatedChat: ChatUpdateRequest): Chat

    @GET("chats/{chatId}/users")
    suspend fun getChatUsers(@Path("chatId") chatId: String): List<ChatUser>
}