package io.github.mee1080.umasim.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator // Keep if used, though not in final snippet
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
// ApiKeyStore should be auto-imported if in the same package, or explicitly imported
// import io.github.mee1080.umasim.chat.ApiKeyStore // Assuming this path

@Composable
fun ChatScreen() {
    // State for the API key currently in use by ChatAgent
    var currentApiKey by remember { mutableStateOf("") }
    // State for the text field where user types the API key
    var apiKeyInput by remember { mutableStateOf("") }

    // Load the API key when the composable is first launched
    LaunchedEffect(Unit) {
        val storedApiKey = ApiKeyStore.getApiKey()
        if (storedApiKey != null) {
            currentApiKey = storedApiKey
            apiKeyInput = storedApiKey // Pre-fill input field if key exists
        }
    }

    // Re-initialize ChatAgent if currentApiKey changes
    val chatAgent = remember(currentApiKey) {
        if (currentApiKey.isNotBlank()) ChatAgent(currentApiKey) else null
    }

    val messages = remember { mutableStateListOf<String>() }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // API Key Input Section
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = apiKeyInput,
                onValueChange = { apiKeyInput = it },
                label = { Text("API Key") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (apiKeyInput.isNotBlank()) {
                    ApiKeyStore.saveApiKey(apiKeyInput)
                    currentApiKey = apiKeyInput
                    messages.add("System: API Key saved successfully.")
                    // println("API Key Saved!") 
                }
            }) {
                Text("Save Key")
            }
        }

        // Display a message if API key is not set
        if (currentApiKey.isBlank()) {
            Text(
                "Please enter and save your API Key to use the chat.",
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                textAlign = TextAlign.Center // Import androidx.compose.ui.text.style.TextAlign
            )
        }

        LazyColumn(modifier = Modifier.weight(1f).padding(8.dp)) {
            items(messages) { message ->
                Text(text = message, modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        // Message Input Section - Enable only if API key is present and ChatAgent is not null
        val chatEnabled = currentApiKey.isNotBlank() && chatAgent != null

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") },
                enabled = !isLoading && chatEnabled // Updated enabled state
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val userMessage = inputText
                    if (userMessage.isNotBlank() && chatAgent != null) { // Check chatAgent not null
                        messages.add("You: $userMessage")
                        inputText = ""
                        isLoading = true
                        messages.add("Bot: Typing...")

                        coroutineScope.launch {
                            try {
                                // Safe call for sendMessage as chatAgent can be null
                                val response = chatAgent.sendMessage(userMessage)
                                messages.remove("Bot: Typing...")
                                messages.add("Bot: $response")
                            } catch (e: Exception) {
                                messages.remove("Bot: Typing...")
                                messages.add("Error: Could not get response")
                                println("Error sending message: ${e.message}")
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                enabled = !isLoading && chatEnabled // Updated enabled state
            ) {
                if (isLoading && chatEnabled) { // Show progress only when chat is enabled and loading
                    CircularProgressIndicator()
                } else {
                    Text("Send")
                }
            }
        }
    }
}
