package org.contoso.whatsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.contoso.whatsapp.data.models.ChatUser
import org.contoso.whatsapp.data.services.ChatService
import org.contoso.whatsapp.data.services.UserService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChatScreen(
    chatService: ChatService,
    userService: UserService,
    onBack: () -> Unit // Callback to navigate back
) {
    var chatName by remember { mutableStateOf("") }
    var users by remember { mutableStateOf<List<ChatUser>>(emptyList()) }
    val selectedUsers = remember { mutableStateListOf<ChatUser>() }
    var isLoading by remember { mutableStateOf(true) } // Loading state
    val coroutineScope = rememberCoroutineScope()

    // Fetch users when the screen is first displayed
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            users = userService.getAllUsersExceptLoggedIn()
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Chat") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (chatName.isNotBlank() && selectedUsers.isNotEmpty()) {
                        coroutineScope.launch {
                            chatService.createChat(chatName, selectedUsers)
                            onBack() // Navigate back after saving
                        }
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Chat name input
            OutlinedTextField(
                value = chatName,
                onValueChange = { chatName = it },
                label = { Text("Chat Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // List of users to select
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(users) { user ->
                        UserSelectionItem(
                            user = user,
                            isSelected = selectedUsers.contains(user),
                            onUserSelected = { isSelected ->
                                if (isSelected) {
                                    selectedUsers.add(user)
                                } else {
                                    selectedUsers.remove(user)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserSelectionItem(
    user: ChatUser,
    isSelected: Boolean,
    onUserSelected: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserSelected(!isSelected) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = onUserSelected
        )
        Text(
            text = user.name,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}