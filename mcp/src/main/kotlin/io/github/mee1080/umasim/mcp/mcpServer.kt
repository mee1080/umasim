package io.github.mee1080.umasim.mcp

import io.github.mee1080.umasim.mcp.tools.getUmaRaceSkillData
import io.github.mee1080.umasim.mcp.tools.simulateUmaRace
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.types.TextContent

fun createMcpServer(simulationThreadCount: Int): Server {
    val server = Server(
        serverInfo = Implementation(
            name = "umasim",
            version = "1.0.0"
        ),
        options = ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(
                    listChanged = true,
                ),
            )
        )
    )

    server.simulateUmaRace(simulationThreadCount)
    server.getUmaRaceSkillData()

    return server
}

fun textResult(text: String): CallToolResult {
    return CallToolResult(
        content = listOf(TextContent(text)),
    )
}

fun errorResult(message: String): CallToolResult {
    return CallToolResult(
        content = listOf(TextContent(message)),
        isError = true,
    )
}