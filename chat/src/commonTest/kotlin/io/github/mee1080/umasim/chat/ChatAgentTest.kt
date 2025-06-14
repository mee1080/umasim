package io.github.mee1080.umasim.chat

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class ChatAgentTest {

    @Test
    fun testSendMessage_apiErrorWithInvalidKey() = runBlocking {
        val chatAgent = ChatAgent("error", "")
        val testMessage = "Hello, world!"
        val response = chatAgent.sendMessage(testMessage)
        // We expect an error message because the API key "TESTTESTTESTTESTTESTTEST" is invalid.
        // The ChatAgent's error handling should return a string containing "Error" or "Could not connect".
        assertTrue(
            response.contains("Error", ignoreCase = true) || response.contains(
                "Could not connect",
                ignoreCase = true
            ), "Response should indicate an error due to invalid API key. Response was: $response"
        )
    }

    @Test
    fun testSendMessage_emptyMessage() = runBlocking {
        val chatAgent = ChatAgent(BuildKonfig.GEMINI_API_KEY, "")
        val emptyMessage = ""
        val response = chatAgent.sendMessage(emptyMessage)
        // The Gemini API will likely return an error for an empty message,
        // or the ChatAgent's error handling for API errors will catch it.
        // We expect a response indicating an error or a non-successful state.
        assertTrue(
            response.contains("Error", ignoreCase = true) || response.contains(
                "Could not connect",
                ignoreCase = true
            ) || response.contains("invalid", ignoreCase = true) || response.contains("empty", ignoreCase = true),
            "Response for empty message should indicate an error or invalid input. Response was: $response"
        )
    }
}
