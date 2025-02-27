package org.contoso.whatsapp.data.models

data class ChatCreationRequest(
    val name: String,
    val users: List<String>
)
