package org.contoso.whatsapp.data.services

import org.contoso.whatsapp.data.models.ChatUser
import org.contoso.whatsapp.data.models.ChatUserCreationRequest
import org.contoso.whatsapp.data.repository.users.UserRepository

class UserService {

    private val userRepository = UserRepository()

    // Store the logged-in user as a nullable variable
    private var userLoggedIn: ChatUser? = null

    // Set the logged-in user
    fun setLoggedInUser(user: ChatUser) {
        userLoggedIn = user
    }

    // Get the logged-in user (nullable)
    fun getLoggedInUser(): ChatUser? {
        return userLoggedIn
    }

    suspend fun createUser(username: String): ChatUser {
        return userRepository.createUser(ChatUserCreationRequest(username))
    }

    suspend fun getUsersByName(username: String): List<ChatUser> {
        return userRepository.getUsersByName(username)
    }

    suspend fun getUserById(sender: String): ChatUser {
        return userRepository.getUserById(sender)
    }

    suspend fun getAllUsersExceptLoggedIn(): List<ChatUser> {
        val users = getAllUsers()

        return users.filter { it.id != userLoggedIn?.id }
    }

    suspend fun getAllUsers(): List<ChatUser> {
        return userRepository.getAllUsers()
    }
}