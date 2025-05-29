package io.github.mee1080.umasim.chat

// Corrected imports based on all findings:
import ai.koog.agents.core.agent.AIAgent // Corrected package for AIAgent
import ai.koog.agents.ext.agent.simpleSingleRunAgent
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor // Correct way to get a PromptExecutor for Google
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.executor.clients.google.GoogleModels // For predefined Google LLModel instances
import ai.koog.prompt.llm.LLMCapability // For constructing a generic LLModel if needed
import ai.koog.prompt.llm.LLMProvider // For constructing a generic LLModel

class ChatAgent(apiKey: String, private val modelNameString: String) {

    private val agent: AIAgent

    init {
        val currentModelId = if (this.modelNameString.isNotBlank()) this.modelNameString else "gemini-1.5-flash-latest"

        val selectedLLModel: LLModel = when (currentModelId) {
            "gemini-1.5-flash-latest" -> GoogleModels.Gemini1_5FlashLatest
            "gemini-1.5-pro-latest" -> GoogleModels.Gemini1_5ProLatest
            // Add more mappings as needed
            else -> {
                println("Warning: Model string '$currentModelId' not explicitly mapped. Using generic LLModel instance.")
                LLModel(
                    provider = LLMProvider.Google, // Assuming Google
                    id = currentModelId,
                    capabilities = listOf(LLMCapability.Completion, LLMCapability.Temperature) // Default capabilities
                )
            }
        }

        agent = simpleSingleRunAgent(
            executor = simpleGoogleAIExecutor(apiToken = apiKey), // Use the helper function
            systemPrompt = "You are a helpful assistant.",
            llmModel = selectedLLModel
        )
    }

    suspend fun sendMessage(message: String): String {
        return try {
            agent.runAndGetResult(message) ?: "No response from agent."
        } catch (e: Exception) {
            println("Error calling agent: ${e.message}")
            e.printStackTrace()
            "Error: Could not connect to the chat service. Please try again later."
        }
    }
}
