package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.ShuntaisaiActionSelector
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiPresentationResult
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.time.measureTime

/**
 * 駿大祭の動作確認・AI評価
 *
 * * 引数なし: 1回のシミュレーション結果を詳細表示
 * * `eval [回数] [Optionの配列のJSONファイル...]`: Runner.shuntaisaiSettingで評価（上位20%の平均）
 * * `dump`: デフォルト設定をJSONで出力
 */
fun debugShuntaisai(args: Array<String> = emptyArray()) {
    val time = measureTime {
        when (args.firstOrNull()) {
            "eval" -> {
                val count = args.getOrNull(1)?.toInt() ?: 10000
                if (args.size <= 2) {
                    evaluateShuntaisai(count, ShuntaisaiActionSelector.defaultOptions)
                } else {
                    args.drop(2).forEach { file ->
                        println("[$file]")
                        evaluateShuntaisai(count, ShuntaisaiActionSelector.decodeOptions(File(file).readText()))
                    }
                }
            }

            "dump" -> println(ShuntaisaiActionSelector.encodeOptions(ShuntaisaiActionSelector.defaultOptions))

            else -> debugShuntaisaiSingleSimulation()
        }
    }
    println("time: $time")
}

private val shuntaisaiDebugChara get() = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)

// simulateShuntaisai.kt の doShuntaisaiSimulationS2 と同じ編成（全て完凸）
private val shuntaisaiDebugSupport
    get() = Store.getSupportByName(
        "[時に交わる海と空]ミスターシービー" to 4,
        "[As if Guided]エフフォーリア" to 4,
        "[その執念は怒濤が如く]メイショウドトウ" to 4,
        "[Time of Departure]マヤノトップガン" to 4,
        "[Innovator]フォーエバーヤング" to 4,
        "[いとどめでたき、まつりかな]吾妻" to 4,
    ).map { it.copy(skills = emptyList()) }

private val shuntaisaiDebugFactor = factor(StatusType.STAMINA, 3) + factor(StatusType.GUTS, 3)

private val shuntaisaiCheckTurns = listOf(24, 48, 60, 72)

private class ShuntaisaiEvalResult(
    val summary: Summary,
    val score: Double,
    val great: List<Boolean>,
    val levels: List<Int>,
    val turnStatus: List<Status>,
)

fun evaluateShuntaisai(count: Int, options: Array<ShuntaisaiActionSelector.Option>) {
    val chara = shuntaisaiDebugChara
    val support = shuntaisaiDebugSupport
    val setting = Runner.shuntaisaiSetting
    val chunk = 16
    val results = runBlocking {
        (0 until chunk).map { index ->
            async(Dispatchers.Default) {
                val n = count / chunk + if (index < count % chunk) 1 else 0
                List(n) {
                    val (summary, history) = Simulator(Scenario.SHUNTAISAI, chara, support, shuntaisaiDebugFactor)
                        .simulateWithHistory(ShuntaisaiActionSelector(*options))
                    val shuntaisaiStatus = history.last().afterTurnState.shuntaisaiStatus
                    ShuntaisaiEvalResult(
                        summary,
                        setting.evaluate(summary.status),
                        (0..2).map {
                            shuntaisaiStatus?.presentationResults?.getOrNull(it) == ShuntaisaiPresentationResult.GREAT_SUCCESS
                        },
                        trainingType.map { shuntaisaiStatus?.level(it) ?: 0 },
                        shuntaisaiCheckTurns.map { history[it - 1].afterTurnState.status },
                    )
                }
            }
        }.awaitAll().flatten()
    }
    val upper = results.sortedByDescending { it.score }.take((results.size * 0.2).toInt())
    fun List<ShuntaisaiEvalResult>.statusAverage() = listOf(
        StatusType.SPEED, StatusType.STAMINA, StatusType.POWER, StatusType.GUTS, StatusType.WISDOM, StatusType.SKILL,
    ).joinToString("/") { type -> "%.0f".format(map { it.summary.status.get(type) }.average()) }
    println("score: %.1f (all: %.1f)".format(upper.map { it.score }.average(), results.map { it.score }.average()))
    println("status upper: ${upper.statusAverage()}")
    println("status all  : ${results.statusAverage()}")
    // 最初の区間の上限を必達ラインとして達成率を出力
    val required = setting.segments.mapNotNull { (type, segments) ->
        segments.first().first.takeIf { it < Int.MAX_VALUE }?.let { type to it }
    }
    fun List<ShuntaisaiEvalResult>.requiredRate(check: (Status) -> Boolean) =
        "%.3f".format(count { check(it.summary.status) } / size.toDouble())
    listOf("upper" to upper, "all  " to results).forEach { (label, list) ->
        println("required $label: all=${list.requiredRate { status -> required.all { status.get(it.first) >= it.second } }} " +
                required.joinToString(" ") { (type, value) ->
                    "$type=${list.requiredRate { it.get(type) >= value }}"
                })
    }
    shuntaisaiCheckTurns.forEachIndexed { index, turn ->
        println(
            "turn $turn: " + listOf(
                StatusType.SPEED,
                StatusType.STAMINA,
                StatusType.POWER,
                StatusType.GUTS,
                StatusType.WISDOM,
                StatusType.SKILL,
            ).joinToString("/") { type -> "%.0f".format(upper.map { it.turnStatus[index].get(type) }.average()) })
    }
    println("great: ${(0..2).joinToString("/") { i -> "%.3f".format(results.count { it.great[i] } / results.size.toDouble()) }}")
    println("level: ${(0..4).joinToString("/") { i -> "%.2f".format(results.map { it.levels[i] }.average()) }}")
    println(
        "training: ${
            trainingType.joinToString("/") { type ->
                "%.1f".format(results.map { it.summary.trainingCount[type]!! }.average())
            }
        }"
    )
    println(
        "sleep/outing/race: %.1f/%.1f/%.1f".format(
            results.map { it.summary.sleepCount }.average(),
            results.map { it.summary.outingCount }.average(),
            results.map { it.summary.raceCount }.average(),
        )
    )
}

fun debugShuntaisaiSingleSimulation() {
    val chara = shuntaisaiDebugChara
    val support = shuntaisaiDebugSupport
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    val selector = ShuntaisaiActionSelector(*ShuntaisaiActionSelector.defaultOptions)
    val result = runBlocking {
        Simulator(Scenario.SHUNTAISAI, chara, support, shuntaisaiDebugFactor)
            .simulateWithHistory(selector) { RandomEvents(it) }
    }
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        println("  開始時: ${history.beforeActionState.status.toShortString()}")
        println("  トレLv: ${history.beforeActionState.training.map { "${it.type}${it.level} " }}")
        println("  駿大祭: ${history.beforeActionState.shuntaisaiStatus?.toShortString()}")
        history.selections.forEach { (selection, selectedAction, result) ->
            println()
            selection.forEach { action ->
                println("  ・${action.name}")
                val total = action.candidates.sumOf { it.second } / 100.0
                action.candidates.forEach {
                    println("    ${it.second / total}% ${it.first}")
                }
                action.infoToString().split("/").forEach {
                    if (it.isNotEmpty()) println("    $it")
                }
                println()
            }
            println("  -> ${selectedAction.name}")
            if (selectedAction is MultipleAction) {
                println("     結果: $result")
            }
        }
        println()
        println("  終了時: ${(history.afterTurnState.status).toShortString()}")
        println("  駿大祭: ${history.afterTurnState.shuntaisaiStatus?.toShortString()}")
    }
    println(result.first)
    println(result.first.status.toShortString())
}
