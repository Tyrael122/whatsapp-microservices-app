package org.contoso.whatsapp.data.services

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.contoso.whatsapp.data.DataStoreHelper
import org.contoso.whatsapp.data.models.ChatMessageIncoming
import java.util.UUID

class UserQueueService(context: Context) {
    private val rabbitMQManager = RabbitMQManager()
    private val dataStoreHelper = DataStoreHelper(context)

    init {
        rabbitMQManager.bindQueue("message-sending")
    }

    suspend fun createQueueToListenToMessagesToUser(userId: String) {
        val existingQueueName = dataStoreHelper.getQueueNameForUser(userId)
        val queueName = existingQueueName ?: ("queue-" + UUID.randomUUID().toString())

        rabbitMQManager.bindQueue(queueName)
        rabbitMQManager.bindQueueToExchange(queueName, "chat-messages-exchange", userId)

        Log.i("UserQueueService", "Created queue: $queueName")
        dataStoreHelper.saveUserQueueMapping(userId, queueName)
    }

    suspend fun listenToMessages(userId: String, messageHandler: (ChatMessageIncoming) -> Unit) {
        withContext(Dispatchers.IO) {
            Log.i("UserQueueService", "Retrieving queue name for user: $userId")

            val queueName = dataStoreHelper.getQueueNameForUser(userId)

            Log.i("UserQueueService", "Listening to queue: $queueName")

            if (queueName != null) {
                rabbitMQManager.listenToQueue(queueName) { message ->
                    Log.i("UserQueueService", "Received message: $message")

                    val chatMessageIncoming = Gson().fromJson(message, ChatMessageIncoming::class.java)
                    messageHandler.invoke(chatMessageIncoming)
                }
            } else {
                Log.e("UserQueueService", "Queue name is null for user: $userId")
            }
        }
    }

    fun sendMessage(sampleMessage: String) {
        rabbitMQManager.sendMessage("message-sending", sampleMessage)
    }
}