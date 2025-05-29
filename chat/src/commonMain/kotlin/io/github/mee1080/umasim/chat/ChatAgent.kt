package io.github.mee1080.umasim.chat

import ai.koog.koog_agents.SimpleSingleRunAgent
import ai.koog.koog_agents.simpleSingleRunAgent
import ai.koog.koog_agents.text.SimpleGoogleExecutor

class ChatAgent(apiKey: String, private val modelName: String) {

    private val agent: SimpleSingleRunAgent

    init {
        val currentModelName = if (this.modelName.isNotBlank()) this.modelName else "gemini-1.5-flash-latest"
        agent = simpleSingleRunAgent(
            executor = SimpleGoogleExecutor(apiKey),
            systemPrompt = "You are a helpful assistant.",
            llmModel = currentModelName
        )
    }

    suspend fun sendMessage(message: String): String {
        return try {
            agent.runAndGetResult(message)
        } catch (e: Exception) {
            // Log the exception or handle it more gracefully
            println("Error calling Gemini API: ${e.message}")
            "Error: Could not connect to the chat service. Please try again later."
        }
    }
}
