package io.github.mee1080.umasim.mcp

import io.github.mee1080.umasim.race.data.loadRecentEventTrackList
import io.github.mee1080.umasim.race.data2.loadSkillData
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.modelcontextprotocol.kotlin.sdk.server.mcp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        listOf(
            launch(Dispatchers.Default) { loadRecentEventTrackList() },
            launch(Dispatchers.Default) { loadSkillData() },
        ).joinAll()
    }

    embeddedServer(CIO, port = 22223) {
        mcp {
            return@mcp createMcpServer(4)
        }
    }.start(wait = true)
}