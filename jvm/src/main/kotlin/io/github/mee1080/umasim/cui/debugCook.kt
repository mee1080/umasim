package io.github.mee1080.umasim.cui


import io.github.mee1080.umasim.ai.CookActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt
import kotlin.time.measureTime

fun debugCook() {
    val time = measureTime {
        debugCookSingleSimulation()
//        debugCookRunSimulation()
    }
    println("time: $time")
}

fun debugCookRunSimulation() {
    val chara = Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5)
    val support = Store.getSupportByName(
        "[アルストロメリアの夢]ヴィブロス",
        "[朝焼け苺の畑にて]ニシノフラワー",
        "[うらら～な休日]ハルウララ",
        "[只、君臨す。]オルフェーヴル",
        "[百花の願いをこの胸に]サトノダイヤモンド",
        "[謹製ッ！特大夢にんじん！]秋川理事長",
    )
    println(chara)
    println(support)
    val factor = listOf(
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
    )
    runBlocking {
        repeat(8) { index ->
            val selector = CookActionSelector.speed2Power1Guts1Wisdom1Mile
            launch(context) {
                val summary = Runner.run(
                    1000,
                    Scenario.COOK,
                    chara,
                    support,
                    factor,
                    selector = selector,
                )
                val evaluator = Evaluator(summary, Runner.cookMileEvaluateSetting, 0.2)
                val score = (evaluator.upperSum(1.0, Runner.cookMileEvaluateSetting) * 1000).roundToInt() / 1000.0
                println("0,$index,0,${evaluator.toSummaryString()},$score")
            }
        }
    }
}

fun debugCookSingleSimulation() {
    val chara = Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5)
    val support = Store.getSupportByName(
        "[アルストロメリアの夢]ヴィブロス",
        "[朝焼け苺の畑にて]ニシノフラワー",
        "[うらら～な休日]ハルウララ",
        "[只、君臨す。]オルフェーヴル",
        "[百花の願いをこの胸に]サトノダイヤモンド",
        "[謹製ッ！特大夢にんじん！]秋川理事長",
    )
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    val selector = CookActionSelector.speed2Power1Guts1Wisdom1Mile()
    val factor = listOf(
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
    )
    val result = runBlocking {
        Simulator(Scenario.COOK, chara, support, factor)
            .simulateWithHistory(selector) { RandomEvents(it) }
    }
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        println("  開始時: ${history.beforeActionState.status.toShortString()}")
        println("  トレLv: ${history.beforeActionState.training.map { "${it.type}${it.level} " }}")
        println("  Cook: ${history.beforeActionState.cookStatus?.toString()}")
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
        println("  Cook: ${history.beforeActionState.cookStatus?.toString()}")
    }
    println(result.first)
    println(result.first.status.toShortString())
}
