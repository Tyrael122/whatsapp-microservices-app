package org.contoso.whatsapp.data.models

data class ChatMessageOutgoing(
    val chatId: String,
    val sender: String,
    val content: String
)