package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.LArcActionSelector
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
fun main() {
    with(Dispatchers.Default.limitedParallelism(10)) {
        StoreLoader.load()
//        lArcRunSimulation()
        lArcSingleSimulation()
    }
}

context(CoroutineContext)
fun lArcRunSimulation() {
    val chara = Store.getChara("[うららん一等賞♪]ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[大望は飛んでいく]エルコンドルパサー",
        "[The frontier]ジャングルポケット",
        "[迫る熱に押されて]キタサンブラック",
        "[一粒の安らぎ]スーパークリーク",
        "[君と見る泡沫]マンハッタンカフェ",
        "[L'aubeは迫りて]佐岳メイ",
    )
    println(chara)
    println(support)
    val factor = listOf(
        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.STAMINA to 3,
        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.GUTS to 3,
    )
    runBlocking {
        repeat(9) { index ->
//            val selector = { LArcActionSelector(LArcActionSelector.Option(hpFactor = hpFactor)) }
            val riskFactor = 0.5 * index + 1.0
            val selector = LArcActionSelector.speed3Stamina1Wisdom1Long
            launch(this@CoroutineContext) {
                val summary = Runner.run(
                    10000,
                    Scenario.LARC,
                    chara,
                    support,
                    factor,
                    selector = selector,
                )
                val evaluator = Evaluator(summary, Runner.lArcLongEvaluateSetting, 0.2)
                val score = (evaluator.upperSum(0.2, Runner.lArcLongEvaluateSetting) * 1000).roundToInt() / 1000.0
                println("0,$riskFactor,0,${evaluator.toSummaryString()},$score")
            }
        }
    }
}

fun lArcSingleSimulation() {
    val chara = Store.getChara("[うららん一等賞♪]ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[大望は飛んでいく]エルコンドルパサー",
        "[The frontier]ジャングルポケット",
        "[見習い魔女と長い夜]スイープトウショウ",
        "[ハネ退け魔を退け願い込め]スペシャルウィーク",
        "[君と見る泡沫]マンハッタンカフェ",
        "[L'aubeは迫りて]佐岳メイ",
    )
    println(chara.name)
    println(support.joinToString(", ") { it.name })
    val selector = LArcActionSelector.speed3Stamina1Wisdom1Long()
    val factor = listOf(
        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.STAMINA to 3,
        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.WISDOM to 3,
    )
    val result = runBlocking {
        Simulator(Scenario.LARC, chara, support, factor)
            .simulateWithHistory(selector) { RandomEvents(it) }
    }
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        println("  開始時: ${history.beforeActionState.status.toShortString()}")
        println("  トレLv: ${history.beforeActionState.training.map { "${it.type}${it.level} " }}")
        println("  LArc: ${history.beforeActionState.lArcStatus?.toShortString()}")
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
//        history.afterTurnState.member
//            .sortedByDescending { (it.scenarioState as LArcMemberState).supporterPt }
//            .applyIf(index != 2 && index != 66) { take(3) }
//            .forEach {
//                println("  ${it.charaName}${if (it.guest) "(ゲスト)" else ""} ${it.scenarioState.toShortString()}")
//            }
        println("  スターゲージ: " + history.afterTurnState.member.map { it.scenarioState as LArcMemberState }
            .joinToString("/") { "${it.starLevel}:${it.starGauge}" })
    }
    println(result.first)
    println(result.first.status.toShortString())
}
