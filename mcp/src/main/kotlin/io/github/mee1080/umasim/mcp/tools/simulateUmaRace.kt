package io.github.mee1080.umasim.mcp.tools

import io.github.mee1080.umasim.mcp.textResult
import io.github.mee1080.umasim.race.calc2.*
import io.github.mee1080.umasim.race.data.Condition
import io.github.mee1080.umasim.race.data.Style
import io.github.mee1080.umasim.race.data2.findSkills
import io.github.mee1080.utility.averageOf
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.types.ToolSchema
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.*

private const val defaultSimulateCount = 10000

fun Server.simulateUmaRace(threadCount: Int) {
    val sampleStatus = UmaStatus()
    addTool(
        name = "simulate_uma_race",
        description = "ウマ娘のレースをシミュレートして、平均タイム、最速タイム、最遅タイム、平均余剰体力、持久力温存発生率を取得します",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                putJsonObject("speed") {
                    put("type", "number")
                    put("description", "スピード、省略時は${sampleStatus.speed}")
                }
                putJsonObject("stamina") {
                    put("type", "number")
                    put("description", "スタミナ、省略時は${sampleStatus.stamina}")
                }
                putJsonObject("power") {
                    put("type", "number")
                    put("description", "パワー、省略時は${sampleStatus.power}")
                }
                putJsonObject("guts") {
                    put("type", "number")
                    put("description", "根性、省略時は${sampleStatus.guts}")
                }
                putJsonObject("wisdom") {
                    put("type", "number")
                    put("description", "賢さ、省略時は${sampleStatus.wisdom}")
                }
                putJsonObject("condition") {
                    putJsonArray("enum") {
                        Condition.entries.forEach { add(it.label) }
                    }
                    put("description", "やる気、省略時は${sampleStatus.condition.label}")
                }
                putJsonObject("style") {
                    putJsonArray("enum") {
                        Style.entries.forEach { add(it.text) }
                    }
                    put("description", "作戦、省略時は${sampleStatus.style.text}")
                }
                putJsonObject("skill_names") {
                    put("description", "所持しているスキルの名称、省略時はスキルなし")
                    put("type", "array")
                    put("items", buildJsonObject {
                        put("type", "string")
                    })
                }
                putJsonObject("count") {
                    put("description", "シミュレートする回数、省略時は$defaultSimulateCount")
                    put("type", "number")
                }
            },
        ),
    ) { request ->
        var status = UmaStatus()
        val errors = mutableListOf<String>()
        val arguments = request.arguments ?: return@addTool textResult("入力パラメータがありません")
        arguments["speed"]?.let { status = status.copy(speed = it.jsonPrimitive.int) }
        arguments["stamina"]?.let { status = status.copy(stamina = it.jsonPrimitive.int) }
        arguments["power"]?.let { status = status.copy(power = it.jsonPrimitive.int) }
        arguments["guts"]?.let { status = status.copy(guts = it.jsonPrimitive.int) }
        arguments["wisdom"]?.let { status = status.copy(wisdom = it.jsonPrimitive.int) }
        arguments["condition"]?.let { e ->
            val condition = Condition.entries.firstOrNull { it.label == e.jsonPrimitive.content }
            if (condition == null) {
                errors += "やる気の設定が不正です"
            } else {
                status = status.copy(condition = condition)
            }
        }
        arguments["style"]?.let { e ->
            val style = Style.entries.firstOrNull { it.text == e.jsonPrimitive.content }
            if (style == null) {
                errors += "作戦の設定が不正です"
            } else {
                status = status.copy(style = style)
            }
        }
        arguments["skill_names"]?.let { e ->
            val hasSkills = e.jsonArray.mapNotNull {
                val name = it.jsonPrimitive.content
                val skills = findSkills(name)
                if (skills == null) {
                    errors += "スキル「$name」は存在しません"
                    null
                } else {
                    skills.first()
                }
            }
            status = status.copy(hasSkills = hasSkills)
        }
        val setting = RaceSetting(status)

        val count = arguments["count"]?.jsonPrimitive?.int ?: defaultSimulateCount
        val calculator = RaceCalculator(SystemSetting())

        val results = mutableListOf<RaceSimulationResult>()
        val mutex = Mutex()
        val scope = CoroutineScope(Dispatchers.Default.limitedParallelism(threadCount))
        List(threadCount) { index ->
            scope.launch {
                repeat(count / threadCount + (if (index < count % threadCount) 1 else 0)) {
                    val result = calculator.simulate(setting)
                    mutex.withLock {
                        results += result.first
                    }
                }
            }
        }.joinAll()

        val result = buildJsonObject {
            put("平均タイム", results.averageOf { it.raceTime })
            put("最速タイム", results.minOf { it.raceTime })
            put("最遅タイム", results.maxOf { it.raceTime })
            put("平均余剰体力", results.averageOf { it.spDiff })
            put("持久力温存発生率", results.count { it.staminaKeepDistance > 0.0 } / results.size.toDouble())
            if (errors.isNotEmpty()) {
                put("errors", buildJsonArray { errors.forEach { add(it) } })
            }
        }
        textResult(result.toString())
    }
}