package io.github.mee1080.umasim.mcp

import io.github.mee1080.umasim.race.data.loadRecentEventTrackList
import io.github.mee1080.umasim.race.data2.loadSkillData
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.*
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered

fun runStdioMcpServer(simulationThreadCount: Int) {
    runBlocking {
        listOf(
            launch(Dispatchers.Default) { loadRecentEventTrackList() },
            launch(Dispatchers.Default) { loadSkillData() },
        ).joinAll()

        val server = createMcpServer(simulationThreadCount)

        val transport = StdioServerTransport(
            System.`in`.asSource().buffered(),
            System.out.asSink().buffered(),
        )
        server.createSession(transport)
        val done = Job()
        server.onClose {
            done.complete()
        }
        done.join()
    }
}