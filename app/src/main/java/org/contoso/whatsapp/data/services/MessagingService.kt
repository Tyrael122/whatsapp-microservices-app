package org.contoso.whatsapp.data.services

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.contoso.whatsapp.data.models.ChatMessageIncoming
import org.contoso.whatsapp.data.models.ChatMessageOutgoing
import org.contoso.whatsapp.data.models.ChatUser
import org.contoso.whatsapp.data.repository.users.UserRepository

class MessagingService(context: Context) {

    private val userQueueService = UserQueueService(context)

    private val userRepository = UserRepository()

    private lateinit var userLoggedIn: ChatUser

    // Use a SnapshotStateMap for reactive updates
    private val chatMessagesMap: SnapshotStateMap<String, List<ChatMessageIncoming>> = mutableStateMapOf()

    suspend fun login(username: String) {
        Log.i("MessagingService", "Fetching users")

        withContext(Dispatchers.IO) {
            // Fetch the user from the repository
            userLoggedIn = userRepository.getUsersByName(username).first()

            Log.i("MessagingService", "Finished fetching users")
            Log.i("MessagingService", "Fetched user: $userLoggedIn")

            // Create a queue for the user and wait for it to complete
            userQueueService.createQueueToListenToMessagesToUser(userLoggedIn.id)

            // Now that the queue name is saved, start listening to messages
            userQueueService.listenToMessages { message ->
                Log.i("MessageService", "Received message: $message")

                CoroutineScope(Dispatchers.IO).launch {
                    val chatUser = userRepository.getUserById(message.sender)

                    val parsedChatMessage = message.copy(sender = chatUser.name)

                    storeIncomingMessage(parsedChatMessage)
                }
            }
        }
    }

    private fun storeIncomingMessage(message: ChatMessageIncoming) {
        val chatId = message.chatId
        val currentMessages = chatMessagesMap[chatId].orEmpty()
        val newMessages = currentMessages + message

        // Update the state map (triggers recomposition in Compose)
        chatMessagesMap[chatId] = newMessages

        Log.i("MessageService", "Stored message: $message in map $chatMessagesMap")
    }

    // Expose the chat messages as a State
    fun getChatMessages(chatId: String): List<ChatMessageIncoming> {
        return chatMessagesMap[chatId].orEmpty()
    }

    fun sendMessage(chatId: String, content: String) {
        val sampleMessage = ChatMessageOutgoing(chatId, userLoggedIn.id, content)
        userQueueService.sendMessage(Gson().toJson(sampleMessage))
    }
}