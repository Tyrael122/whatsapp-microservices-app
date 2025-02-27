package org.contoso.whatsapp.data.repository.users

import org.contoso.whatsapp.data.models.ChatUser
import org.contoso.whatsapp.data.models.ChatUserCreationRequest
import org.contoso.whatsapp.data.repository.networking.UserRetrofitClient

class UserRepository {
    private val userApiService = UserRetrofitClient.instance

    suspend fun getUsersByName(username: String): List<ChatUser> {
        return userApiService.getUsersByName(username)
    }

    suspend fun getUserById(userId: String): ChatUser {
        return userApiService.getUserById(userId)
    }

    suspend fun getAllUsers(): List<ChatUser> {
        return userApiService.getAllUsers()
    }

    suspend fun createUser(chatUserCreationRequest: ChatUserCreationRequest): ChatUser {
        return userApiService.createUser(chatUserCreationRequest)
    }
}