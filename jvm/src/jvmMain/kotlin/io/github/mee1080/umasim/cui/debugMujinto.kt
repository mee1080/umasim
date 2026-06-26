package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.MujintoActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt
import kotlin.time.measureTime

fun debugMujinto() {
    val time = measureTime {
//        debugMujintoSingleSimulation()
        debugMujintoRunSimulation()
    }
    println("time: $time")
}

fun debugMujintoRunSimulation() {
    val chara = Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5) // Placeholder character
    val support = Store.getSupportByName(
        "[Cocoon]エアシャカール" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    )
    println(chara)
    println(support)
    val factor = listOf(
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
    )
    runBlocking {
        repeat(8) { index ->
            val selector = MujintoActionSelector.defaultOption
            launch(context) {
                val summary = Runner.run(
                    10000,
                    Scenario.MUJINTO, // Changed to Mujinto Scenario
                    chara,
                    support,
                    factor,
                    selector = { selector.generateSelector() },
                )
                val evaluator = Evaluator(summary, Runner.mujintoSetting, 0.2)
                val score = (evaluator.upperSum(1.0, Runner.mujintoSetting) * 1000).roundToInt() / 1000.0
                println("0,$index,0,${evaluator.toSummaryString()},$score")
            }
        }
    }
}

fun debugMujintoSingleSimulation() {
    val chara = Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5) // Placeholder character
    val support = Store.getSupportByName(
        "[Cocoon]エアシャカール" to 4,
        "[Take Them Down!]ナリタタイシン" to 4,
        "[白き稲妻の如く]タマモクロス" to 4,
        "[只、君臨す。]オルフェーヴル" to 4,
        "[世界を変える眼差し]アーモンドアイ" to 4,
        "[本能は吼えているか！？]タッカーブライン" to 4,
    )
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    val selector = MujintoActionSelector.defaultOption.generateSelector()
    val factor = listOf(
        // Placeholder factors
        StatusType.SPEED to 3, StatusType.STAMINA to 3, StatusType.POWER to 3,
        StatusType.POWER to 3, StatusType.POWER to 3, StatusType.POWER to 3,
    )
    val result = runBlocking {
        Simulator(Scenario.MUJINTO, chara, support, factor) // Changed to Mujinto Scenario
            .simulateWithHistory(selector) { RandomEvents(it) }
    }
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        println("  開始時: ${history.beforeActionState.status.toShortString()}")
        println("  トレLv: ${history.beforeActionState.training.map { "${it.type}${it.level} " }}")
        println("  無人島: ${history.beforeActionState.mujintoStatus?.toShortString()}")
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
        println("  無人島: ${history.afterTurnState.mujintoStatus?.toShortString()}")
    }
    println(result.first)
    println(result.first.status.toShortString())
}
