package org.contoso.whatsapp.data.models

data class ChatRequest(
    val name: String,
    val users: List<String>
)
