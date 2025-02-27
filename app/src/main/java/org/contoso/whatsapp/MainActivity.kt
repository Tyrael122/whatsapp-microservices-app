package org.contoso.whatsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.contoso.whatsapp.data.services.ChatService
import org.contoso.whatsapp.data.services.MessagingService
import org.contoso.whatsapp.data.services.UserService
import org.contoso.whatsapp.ui.screens.ChatScreen
import org.contoso.whatsapp.ui.screens.CreateChatScreen
import org.contoso.whatsapp.ui.screens.EditParticipantsScreen
import org.contoso.whatsapp.ui.screens.HomeScreen
import org.contoso.whatsapp.ui.theme.WhatsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val userService = remember { UserService() }
    val chatService = remember { ChatService(userService) }
    val messagingService = remember { MessagingService(context, userService) }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController, messagingService)
        }
        composable("home") {
            HomeScreen(
                chatService = chatService,
                onChatClick = { chat ->
                    navController.navigate("chat/${chat.id}")
                },
                onCreateChat = {
                    navController.navigate("createChat")
                }
            )
        }
        composable("chat/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(
                messagingService = messagingService,
                chatId = chatId,
                onEditParticipants = {
                    navController.navigate("editParticipants/${chatId}")
                }
            )
        }
        composable("editParticipants/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            EditParticipantsScreen(
                chatService = chatService,
                userService = userService,
                chatId = chatId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("createChat") {
            CreateChatScreen(
                chatService = chatService,
                userService = userService,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun LoginScreen(
    navController: androidx.navigation.NavController,
    messagingService: MessagingService
) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Username TextField
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                isError = it.text.isEmpty() // Reset error state when typing
            },
            label = { Text("Username") },
            isError = isError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (isError) {
            Text(
                text = "Username cannot be empty",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                if (username.text.isEmpty()) {
                    isError = true // Show error if username is empty
                } else {
                    // Launch a coroutine to call the suspend function
                    coroutineScope.launch {
                        try {
                            messagingService.login(username.text)
                            navController.navigate("home")

                        } catch (e: Exception) {
                            // Handle any errors that occur during login
                            // For example, show a snackbar or error message
                            Log.e("LoginScreen", "Error logging in", e)
                            isError = true
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}