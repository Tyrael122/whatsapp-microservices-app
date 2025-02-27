package org.contoso.whatsapp.data.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.contoso.whatsapp.data.models.Chat
import org.contoso.whatsapp.data.models.ChatCreationRequest
import org.contoso.whatsapp.data.models.ChatUpdateRequest
import org.contoso.whatsapp.data.models.ChatUser
import org.contoso.whatsapp.data.repository.chat.ChatRepository

class ChatService(
    private val userService: UserService
) {
    private val chatRepository = ChatRepository()

    suspend fun getChats(): List<Chat> {
        return withContext(Dispatchers.IO) {
            chatRepository.getChats()
        }
    }

    suspend fun updateChatUsers(chatId: String, selectedUsers: List<ChatUser>): Chat {
        return withContext(Dispatchers.IO) {
            val users = selectedUsers.map { it.id }

            val updatedChat = ChatUpdateRequest(
                users = users
            )

            Log.i("ChatService", "Updating chat: $updatedChat")

            chatRepository.updateChat(chatId, updatedChat)
        }
    }

    suspend fun createChat(name: String, selectedUsers: List<ChatUser>): Chat {
        return withContext(Dispatchers.IO) {
            val users = selectedUsers + userService.getLoggedInUser()!!

            val chat = ChatCreationRequest(
                name = name,
                users = users.map { it.id }
            )

            Log.i("ChatService", "Creating chat: $chat")

            chatRepository.createChat(chat)
        }
    }

    suspend fun getChatUsers(chatId: String): List<ChatUser> {
        val users = chatRepository.getChatUsers(chatId)

        Log.i("ChatService", "Chat users: $users")

        return users.map { user -> userService.getUserById(user.id) }
    }
}