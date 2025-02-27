package org.contoso.whatsapp.data.services

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RabbitMQManager(
    private val host: String = "192.168.0.73",
    private val port: Int = 8083,
    private val username: String = "guest",
    private val password: String = "guest"
) {
    private var connection: Connection? = null
    private var channel: Channel? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            initializeConnection()
        }
    }

    /**
     * Initializes the RabbitMQ connection and channel.
     */
    private fun initializeConnection() {
        val factory = ConnectionFactory()
        factory.host = host
        factory.port = port
        factory.username = username
        factory.password = password

        connection = factory.newConnection()
        channel = connection?.createChannel()
    }

    /**
     * Binds to a queue. Creates the queue if it doesn't exist.
     *
     * @param queueName The name of the queue to bind to.
     */
    fun bindQueue(queueName: String) {
        channel?.queueDeclare(queueName, true, false, false, null)
        println(" [x] Bound to queue '$queueName'")
    }

    /**
     * Sends a message to a specific queue.
     *
     * @param queueName The name of the queue to send the message to.
     * @param message The message to send.
     */
    fun sendMessage(queueName: String, message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            channel?.basicPublish("", queueName, null, message.toByteArray())
            println(" [x] Sent message '$message' to queue '$queueName'")
        }
    }

    /**
     * Binds a queue to an exchange with a specific routing key.
     *
     * @param queueName The name of the queue to bind.
     * @param exchangeName The name of the exchange to bind to.
     * @param routingKey The routing key to use for binding.
     */
    fun bindQueueToExchange(queueName: String, exchangeName: String, routingKey: String) {
        channel?.exchangeDeclare(exchangeName, "direct", true) // Declare the exchange
        channel?.queueBind(queueName, exchangeName, routingKey)
        println(" [x] Bound queue '$queueName' to exchange '$exchangeName' with routing key '$routingKey'")
    }

    /**
     * Listens to messages from a specific queue.
     *
     * @param queueName The name of the queue to listen to.
     * @param messageHandler A lambda function to handle incoming messages.
     */
    fun listenToQueue(queueName: String, messageHandler: (String) -> Unit) {
        val deliverCallback = DeliverCallback { _, delivery ->
            val message = String(delivery.body, Charsets.UTF_8)
            messageHandler(message)
        }

        channel?.basicConsume(queueName, true, deliverCallback) { _ -> }
        println(" [*] Listening to queue '$queueName'. To exit press CTRL+C")
    }

    /**
     * Closes the RabbitMQ connection and channel.
     */
    fun close() {
        channel?.close()
        connection?.close()
        println(" [x] Connection and channel closed.")
    }
}