package org.contoso.whatsapp.data.services

import org.contoso.whatsapp.data.models.ChatUser
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

    // Clear the logged-in user (for logout)
    fun clearLoggedInUser() {
        userLoggedIn = null
    }

    suspend fun getUsersByName(username: String): List<ChatUser> {
        return userRepository.getUsersByName(username)
    }

    suspend fun getUserById(sender: String): ChatUser {
        return userRepository.getUserById(sender)
    }
}