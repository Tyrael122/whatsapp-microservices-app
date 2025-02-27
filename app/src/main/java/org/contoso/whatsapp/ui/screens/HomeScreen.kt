package org.contoso.whatsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.contoso.whatsapp.data.models.Chat
import org.contoso.whatsapp.data.services.ChatService

@Composable
fun HomeScreen(chatService: ChatService, onChatClick: (Chat) -> Unit) {
    var chats by remember { mutableStateOf<List<Chat>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) } // Loading state
    var showDialog by remember { mutableStateOf(false) } // Dialog visibility
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Fetch chats when the screen is first displayed
        coroutineScope.launch {
            chats = chatService.getChats()
            isLoading = false // Update loading state
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            // Show a loading indicator
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (chats.isEmpty()) {
            // Show a message if no chats are available
            Text("No chats available", modifier = Modifier.align(Alignment.Center))
        } else {
            // Display the list of chats
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(chats) { chat ->
                    ChatItem(chat = chat, onClick = { onChatClick(chat) })
                }
            }
        }

        // Floating Action Button (FAB)
        FloatingActionButton(
            onClick = { showDialog = true }, // Show the dialog when clicked
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Chat")
        }
    }

    // Dialog to create a new chat
    if (showDialog) {
        CreateChatDialog(
            onDismiss = { showDialog = false },
            onCreate = { chatName ->
                coroutineScope.launch {
                    val newChat = chatService.createChat(chatName)
                    chats = chats + newChat // Update the chat list
                }
            }
        )
    }
}

@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // You can add an icon or avatar here if needed
            Text(
                text = chat.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CreateChatDialog(onDismiss: () -> Unit, onCreate: (String) -> Unit) {
    var chatName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Chat") },
        text = {
            Column {
                OutlinedTextField(
                    value = chatName,
                    onValueChange = { chatName = it },
                    label = { Text("Chat Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (chatName.isNotBlank()) {
                        onCreate(chatName)
                        onDismiss()
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}