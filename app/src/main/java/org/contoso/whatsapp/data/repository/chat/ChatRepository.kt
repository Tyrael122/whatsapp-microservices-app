package org.contoso.whatsapp.data.repository.chat

import org.contoso.whatsapp.data.models.Chat
import org.contoso.whatsapp.data.models.ChatRequest
import org.contoso.whatsapp.data.repository.networking.ChatRetrofitClient

class ChatRepository {
    private val chatApiService = ChatRetrofitClient.instance

    suspend fun getChats(): List<Chat> {
        return chatApiService.getChats()
    }

    suspend fun createChat(chat: ChatRequest): Chat {
        return chatApiService.createChat(chat)
    }
}