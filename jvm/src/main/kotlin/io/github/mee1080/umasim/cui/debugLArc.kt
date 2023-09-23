package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.LArcActionSelector
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.RandomEvents
import io.github.mee1080.umasim.simulation2.Runner
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.util.applyIf
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
        lArcRunSimulation()
//        lArcSingleSimulation()
    }
}

context(CoroutineContext)
fun lArcRunSimulation() {
    val chara = Store.getChara("[プリンセス・オブ・ピンク]カワカミプリンセス", 5, 5)
    val support = Store.getSupportByName(
        "[大望は飛んでいく]エルコンドルパサー",
        "[The frontier]ジャングルポケット",
        "[迫る熱に押されて]キタサンブラック",
        "[ロード・オブ・ウオッカ]ウオッカ",
        "[Dear Mr. C.B.]ミスターシービー",
        "[L'aubeは迫りて]佐岳メイ",
    )
    println(chara)
    println(support)
    val factor = listOf(
        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.STAMINA to 3,
        StatusType.STAMINA to 3, StatusType.GUTS to 3, StatusType.GUTS to 3,
    )
    runBlocking {
        repeat(9) { index ->
//            val selector = { LArcActionSelector(LArcActionSelector.Option(hpFactor = hpFactor)) }
            val adjust = 0.05 * index - 0.2
            val selector = {
                LArcActionSelector(LArcActionSelector.speed3Power1Wisdom1MiddleOptions.map {
                    it.copy(hpFactor = it.hpFactor + adjust)
                })
            }
            launch(this@CoroutineContext) {
                val summary = Runner.run(
                    10000,
                    Scenario.LARC,
                    chara,
                    support,
                    factor,
                    selector = selector,
                )
                val evaluator = Evaluator(summary)
                val score = (evaluator.upperSum(0.2, Runner.lArcMiddleEvaluateSetting) * 1000).roundToInt() / 1000.0
                println("0,$adjust,0,${evaluator.toSummaryString()},$score")
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
    val result = Simulator(Scenario.LARC, chara, support, factor)
        .simulateWithHistory(selector) { RandomEvents(it) }
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        println("  開始時: ${history.state.status.toShortString()}")
        history.selections.forEach { (selection, selectedAction) ->
            selection.forEach {
                println("  ${it.scenarioActionParam?.toShortString()} : ${it.toShortString()}")
            }
            println("  -> ${selectedAction.action?.toShortString() ?: ""}${selectedAction.scenarioAction ?: ""}")
        }
        println("  上昇量: ${history.status.toShortString()}")
        println("  トレLv: ${history.state.training.map { "${it.type}${it.level} " }}")
        println("  LArc: ${history.state.lArcStatus?.toShortString()}")
        println("  終了時: ${(history.status + history.state.status).toShortString()}")
        history.state.member
            .sortedByDescending { (it.scenarioState as LArcMemberState).supporterPt }
            .applyIf(index != 2 && index != 66) { take(3) }
            .forEach {
                println("  ${it.charaName}${if (it.guest) "(ゲスト)" else ""} ${it.scenarioState.toShortString()}")
            }
        println("  スターゲージ: " + history.state.member.map { it.scenarioState as LArcMemberState }
            .joinToString("/") { "${it.starLevel}:${it.starGauge}" })
    }
    println(result.first)
    println(result.first.status.toShortString())
}
