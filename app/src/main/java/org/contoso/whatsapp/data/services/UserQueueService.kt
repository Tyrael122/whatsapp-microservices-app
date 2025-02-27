package org.contoso.whatsapp.data.services

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
        val queueName = "queue-" + UUID.randomUUID().toString()

        rabbitMQManager.bindQueue(queueName)
        rabbitMQManager.bindQueueToExchange(queueName, "chat-messages-exchange", userId)

        // Save the queue name to DataStore
        withContext(Dispatchers.IO) {
            dataStoreHelper.saveQueueName(queueName)
        }
    }

    suspend fun listenToMessages(messageHandler: (ChatMessageIncoming) -> Unit) {
        withContext(Dispatchers.IO) {
            Log.i("UserQueueService", "Retrieving queue name.")

            val queueName = dataStoreHelper.getQueueName().first()

            Log.i("UserQueueService", "Listening to queue: $queueName")

            if (queueName != null) {
                rabbitMQManager.listenToQueue(queueName) { message ->
                    Log.i("UserQueueService", "Received message: $message")

                    val chatMessageIncoming = Gson().fromJson(message, ChatMessageIncoming::class.java)
                    messageHandler.invoke(chatMessageIncoming)
                }
            } else {
                Log.e("UserQueueService", "Queue name is null.")
            }
        }
    }

    fun sendMessage(sampleMessage: String) {
        rabbitMQManager.sendMessage("message-sending", sampleMessage)
    }
}