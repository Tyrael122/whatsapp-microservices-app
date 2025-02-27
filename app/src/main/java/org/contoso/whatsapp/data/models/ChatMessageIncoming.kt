package org.contoso.whatsapp.data.models

data class ChatMessageIncoming(
    val id: String,
    val chatId: String,
    val sender: String,
    val content: String
)
