package io.github.mee1080.umasim.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@Composable
fun ChatScreen() {
    val chatAgent = remember { ChatAgent() }
    val messages = remember { mutableStateListOf<String>() }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f).padding(8.dp)) {
            items(messages) { message ->
                Text(text = message, modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") },
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val userMessage = inputText
                    if (userMessage.isNotBlank()) {
                        messages.add("You: $userMessage")
                        inputText = ""
                        isLoading = true
                        messages.add("Bot: Typing...") // Add typing indicator

                        coroutineScope.launch {
                            try {
                                val response = chatAgent.sendMessage(userMessage)
                                messages.remove("Bot: Typing...") // Remove typing indicator
                                messages.add("Bot: $response")
                            } catch (e: Exception) {
                                messages.remove("Bot: Typing...") // Remove typing indicator
                                messages.add("Error: Could not get response")
                                // Log the exception if necessary
                                println("Error sending message: ${e.message}")
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text("Send")
            }
        }
    }
}
