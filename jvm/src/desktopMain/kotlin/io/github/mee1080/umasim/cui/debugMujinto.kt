package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.MujintoActionSelector // Assuming a MujintoActionSelector exists or will be created
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
// import io.github.mee1080.umasim.scenario.uaf.ColorFactor // Removed UAF specific import
// import io.github.mee1080.umasim.scenario.uaf.UafAthleticsLevelCalculator // Removed UAF specific import
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun debugMujinto() {
    val time = measureTime {
        debugMujintoSingleSimulation()
//        debugMujintoRunSimulation()
    }
    println("time: $time")
}

fun debugMujintoRunSimulation() {
    val chara = Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5) // Placeholder character
    val support = Store.getSupportByName( // Placeholder supports
        "[血脈の胎動]ドゥラメンテ",
        "[大望は飛んでいく]エルコンドルパサー",
        "[冬溶かす熾火]メジロラモーヌ",
        "[只、君臨す。]オルフェーヴル",
        "[かっとばせー！ですわ！？]メジロマックイーン",
        "[共に描くキラメキ]都留岐涼花",
    )
    println(chara)
    println(support)
    val factor = listOf( // Placeholder factors
        StatusType.SPEED to 3, StatusType.STAMINA to 3, StatusType.POWER to 3,
        StatusType.POWER to 3, StatusType.POWER to 3, StatusType.POWER to 3,
    )
    runBlocking {
        repeat(8) { index ->
            // Assuming a MujintoActionSelector exists or will be created
            val selector = MujintoActionSelector.speed2Power1Guts1Wisdom1Mile
            launch(context) {
                val summary = Runner.run(
                    1000,
                    Scenario.Mujinto, // Changed to Mujinto Scenario
                    chara,
                    support,
                    factor,
                    selector = selector,
                )
                // Assuming Runner.mujintoMileEvaluateSetting exists or will be created
                val evaluator = Evaluator(summary, Runner.mujintoMileEvaluateSetting, 0.2)
                val score = (evaluator.upperSum(1.0, Runner.mujintoMileEvaluateSetting) * 1000).roundToInt() / 1000.0
                println("0,$index,0,${evaluator.toSummaryString()},$score")
            }
        }
    }
}

fun debugMujintoSingleSimulation() {
    val chara = Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5) // Placeholder character
    val support = Store.getSupportByName( // Placeholder supports
        "[血脈の胎動]ドゥラメンテ",
        "[大望は飛んでいく]エルコンドルパサー",
        "[冬溶かす熾火]メジロラモーヌ",
        "[只、君臨す。]オルフェーヴル",
        "[かっとばせー！ですわ！？]メジロマックイーン",
        "[共に描くキラメキ]都留岐涼花",
    )
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    // Assuming a MujintoActionSelector exists or will be created
    val selector = MujintoActionSelector.speed2Power1Guts1Wisdom1Mile()
    val factor = listOf( // Placeholder factors
        StatusType.SPEED to 3, StatusType.STAMINA to 3, StatusType.POWER to 3,
        StatusType.POWER to 3, StatusType.POWER to 3, StatusType.POWER to 3,
    )
    val result = runBlocking {
        Simulator(Scenario.Mujinto, chara, support, factor) // Changed to Mujinto Scenario
            .simulateWithHistory(selector) { RandomEvents(it) }
    }
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        println("  開始時: ${history.beforeActionState.status.toShortString()}")
        println("  トレLv: ${history.beforeActionState.training.map { "${it.type}${it.level} " }}")
        // Removed UAF specific status print
        // println("  UAF: ${history.beforeActionState.uafStatus?.toShortString()}")
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
        // Removed UAF specific status print
        // println("  UAF: ${history.beforeActionState.uafStatus?.toShortString()}")
    }
    println(result.first)
    println(result.first.status.toShortString())
}

// Removed debugUafAthleticsLevelCalculator function as it's UAF specific
