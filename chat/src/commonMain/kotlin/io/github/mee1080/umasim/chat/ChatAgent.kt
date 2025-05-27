package io.github.mee1080.umasim.chat

import ai.koog.koog_agents.*
import ai.koog.koog_agents.api.*
import ai.koog.koog_agents.model.*
import io.github.mee1080.umasim.chat.BuildKonfig

class ChatAgent {

    private val client: GeminiApi

    init {
        val apiKey = BuildKonfig.GEMINI_API_KEY
        this.client = GeminiApi(apiKey)
    }

    suspend fun sendMessage(message: String): String {
        val modelName = "gemini-1.5-flash-latest"
        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(Part(text = message)),
                    role = "user"
                )
            )
        )

        return try {
            val response = client.generateContent(modelName, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response from model."
        } catch (e: Exception) {
            // Log the exception or handle it more gracefully
            println("Error calling Gemini API: ${e.message}")
            "Error: Could not connect to the chat service. Please try again later."
        }
    }
}
