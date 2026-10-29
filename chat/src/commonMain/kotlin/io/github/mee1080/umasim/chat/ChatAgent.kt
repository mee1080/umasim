package io.github.mee1080.umasim.chat

// Corrected imports based on all findings:
import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.GraphAIAgent
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel

class ChatAgent(apiKey: String, private val modelNameString: String) {

    private val agent: GraphAIAgent<String, String>

    init {
        val currentModelId = this.modelNameString.ifBlank { GoogleModels.Gemini2_5Flash.id }

        val selectedLLModel: LLModel = GoogleModels.models.firstOrNull { it.id == currentModelId } ?: run {
            println("Warning: Model string '$currentModelId' not explicitly mapped. Using generic LLModel instance.")
            LLModel(
                provider = LLMProvider.Google, // Assuming Google
                id = currentModelId,
                capabilities = listOf(LLMCapability.Completion, LLMCapability.Temperature) // Default capabilities
            )
        }

        agent = AIAgent(
            promptExecutor = MultiLLMPromptExecutor(GoogleLLMClient(apiKey)),
            systemPrompt = "You are a helpful assistant.",
            llmModel = selectedLLModel
        )
    }

    suspend fun sendMessage(message: String): String {
        return try {
            agent.run(message)
        } catch (e: Exception) {
            println("Error calling agent: ${e.message}")
            e.printStackTrace()
            "Error: Could not connect to the chat service. Please try again later."
        }
    }
}
