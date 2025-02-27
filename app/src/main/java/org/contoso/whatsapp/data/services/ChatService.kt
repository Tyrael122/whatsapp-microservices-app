package org.contoso.whatsapp.data.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.contoso.whatsapp.data.models.Chat
import org.contoso.whatsapp.data.repository.chat.ChatRepository

class ChatService {
    private val chatRepository = ChatRepository()

    suspend fun getChats(): List<Chat> {
        return withContext(Dispatchers.IO) {
            chatRepository.getChats()
        }
    }

    suspend fun createChat(name: String): Chat {
        return withContext(Dispatchers.IO) {
            chatRepository.createChat(name)
        }
    }
}