package io.github.mee1080.umasim.cui


import io.github.mee1080.umasim.ai.LArcActionSelector
import io.github.mee1080.umasim.ai.UafActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

fun debugUaf() {
    debugUafSingleSimulation()
}

context(CoroutineContext)
fun debugUafRunSimulation() {
    val chara = Store.getChara("[超特急！フルカラー特殊PP]アグネスデジタル", 5, 5)
    val support = Store.getSupportByName(
        "[大望は飛んでいく]エルコンドルパサー",
        "[The frontier]ジャングルポケット",
        "[迫る熱に押されて]キタサンブラック",
        "[やったれハロウィンナイト！]タマモクロス",
        "[燦爛]メジロラモーヌ",
        "[L'aubeは迫りて]佐岳メイ",
    )
    println(chara)
    println(support)
    val factor = listOf(
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.POWER to 3,
        StatusType.POWER to 3, StatusType.STAMINA to 3, StatusType.STAMINA to 3,
    )
    runBlocking {
        repeat(9) { index ->
            val selector = LArcActionSelector.speed3Power1Wisdom1Middle
            launch(this@CoroutineContext) {
                val summary = Runner.run(
                    10000,
                    Scenario.LARC,
                    chara,
                    support,
                    factor,
                    selector = selector,
                )
                val evaluator = Evaluator(summary, Runner.lArcMiddleEvaluateSetting, 0.2)
                val score = (evaluator.upperSum(0.2, Runner.lArcMiddleEvaluateSetting) * 1000).roundToInt() / 1000.0
                println("0,$index,0,${evaluator.toSummaryString()},$score")
            }
        }
    }
}

fun debugUafSingleSimulation() {
    val chara = Store.getChara("[プラタナス・ウィッチ]スイープトウショウ", 5, 5)
    val support = Store.getSupportByName(
        "[血脈の胎動]ドゥラメンテ",
        "[大望は飛んでいく]エルコンドルパサー",
        "[冬溶かす熾火]メジロラモーヌ",
        "[只、君臨す。]オルフェーヴル",
        "[かっとばせー！ですわ！？]メジロマックイーン",
        "[共に描くキラメキ]都留岐涼花",
    )
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    val selector = UafActionSelector.speed2Power1Guts1Wisdom1Long()
    val factor = listOf(
        StatusType.SPEED to 3, StatusType.STAMINA to 3, StatusType.POWER to 3,
        StatusType.POWER to 3, StatusType.POWER to 3, StatusType.POWER to 3,
    )
    val result = runBlocking {
        Simulator(Scenario.UAF, chara, support, factor)
            .simulateWithHistory(selector) { RandomEvents(it) }
    }
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        println("  開始時: ${history.beforeActionState.status.toShortString()}")
        println("  トレLv: ${history.beforeActionState.training.map { "${it.type}${it.level} " }}")
        println("  UAF: ${history.beforeActionState.uafStatus?.toShortString()}")
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
        println("  UAF: ${history.beforeActionState.uafStatus?.toShortString()}")
    }
    println(result.first)
    println(result.first.status.toShortString())
}

private fun debugUafAthleticsLevelCalculator() {
    @Suppress("BooleanLiteralArgument")
    val factors = listOf(
        ColorFactor(2, false, true, 0.306, 0.154),
        ColorFactor(4, false, true, 0.231, 0.171),
        ColorFactor(0, false, true, 0.328, 0.149),
        ColorFactor(0, false, true, 0.268, 0.162),
        ColorFactor(3, false, true, 0.286, 0.159),
        ColorFactor(5, true, false, 0.167, 0.167),
//        ColorFactor(5, false, false, 0.18, 0.18),
//        ColorFactor(5, false, false, 0.18, 0.18),
    )
    val time = measureTimeMillis {
        val list = UafAthleticsLevelCalculator.calc(factors, false)
        var expected = 0.0
        list.forEachIndexed { index, rate ->
            if (rate > 0.0) {
                println("$index: $rate")
                expected += index * rate
            }
        }
        println(expected)
        println(list.sum())
    }
    println("time: ${time / 1000.0} s")
}