package org.contoso.whatsapp.data.repository.chat

import org.contoso.whatsapp.data.models.Chat
import org.contoso.whatsapp.data.models.ChatCreationRequest
import org.contoso.whatsapp.data.models.ChatUpdateRequest
import org.contoso.whatsapp.data.models.ChatUser
import org.contoso.whatsapp.data.repository.networking.ChatRetrofitClient

class ChatRepository {
    private val chatApiService = ChatRetrofitClient.instance

    suspend fun getChats(): List<Chat> {
        return chatApiService.getChats()
    }

    suspend fun createChat(chat: ChatCreationRequest): Chat {
        return chatApiService.createChat(chat)
    }

    suspend fun updateChat(chatId: String, updatedChat: ChatUpdateRequest): Chat {
        return chatApiService.updateChat(chatId, updatedChat)
    }

    suspend fun getChatUsers(chatId: String): List<ChatUser> {
        return chatApiService.getChatUsers(chatId)
    }
}