package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.GmActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
fun main() {
    with(Dispatchers.Default.limitedParallelism(4)) {
        StoreLoader.load()
    gmSingleSimulation()
//        gmRunSimulation()
    }
}

context(CoroutineContext)
fun gmRunSimulation() {
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
//    val support = Store.getSupportByName(
//        "[泥まみれのコンパネーロ]エルコンドルパサー",
//        "[///WARNING GATE///]シンコウウインディ",
//        "[ほっと♪きゅーとメモリー]カレンチャン",
//        "[Dear Mr. C.B.]ミスターシービー",
//        "[おてんば魔女、修行中。]スイープトウショウ",
//        "[嗚呼華麗ナル一族]ダイイチルビー",
//    )
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック",
        "[おセンチ注意報♪]マルゼンスキー",
        "[うらら～な休日]ハルウララ",
        "[Dear Mr. C.B.]ミスターシービー",
        "[燦爛]メジロラモーヌ",
        "[嗚呼華麗ナル一族]ダイイチルビー",
    )
    println(chara)
    println(support)
    val selector = { GmActionSelector(GmActionSelector.speed2Power1Guts1Wisdom2SP) }
    val factor = listOf(
        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.STAMINA to 3,
        StatusType.STAMINA to 3, StatusType.POWER to 3, StatusType.POWER to 3,
    )
    val results = runBlocking {
        List(4) {
            async(this@CoroutineContext) {
                println("start")
                Runner.run(
                    1000,
                    Scenario.GM,
                    chara,
                    support,
                    factor,
                    selector = selector,
                )
            }
        }.map {
            it.await()
        }
    }
    val evaluator = Evaluator(results.flatten())
    val score = (evaluator.upperSum(0.2, Runner.gmMileEvaluateSetting) * 1000).roundToInt() / 1000.0
    println("0,test,0,${evaluator.toSummaryString()},$score")
}

fun gmSingleSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[泥まみれのコンパネーロ]エルコンドルパサー",
        "[///WARNING GATE///]シンコウウインディ",
        "[永劫続く栄光へ]祖にして導く者",
        "[Dear Mr. C.B.]ミスターシービー",
        "[おてんば魔女、修行中。]スイープトウショウ",
        "[嗚呼華麗ナル一族]ダイイチルビー",
    )
    println(chara)
    println(support)
    val selector = GmActionSelector(
        GmActionSelector.speed3Power1Wisdom2SR
    )
    val factor = listOf(
        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.STAMINA to 3,
        StatusType.STAMINA to 3, StatusType.POWER to 3, StatusType.POWER to 3,
    )
    val result = Simulator(Scenario.GM, chara, support, factor)
        .simulateWithHistory(78, selector) { RandomEvents(it) }
    val founders = mutableListOf<String>()
    result.second.forEachIndexed { index, history ->
        println()
        println("${index + 1}:")
        history.selections.forEach { (selection, selectedAction) ->
//            selection.forEach {
//                println("  ${it.scenarioActionParam?.toShortString()} : ${it.toShortString()}")
//            }
            println("  -> ${selectedAction.action?.toShortString() ?: ""}${selectedAction.scenarioAction ?: ""}")
            (selectedAction.scenarioAction as? GmActivateWisdom)?.let {
                founders += "${index + 1}: ${it.founder}"
            }
        }
        println("  ${history.state.training.map { "${it.type}${it.level} " }}")
        println("  ${history.state.gmStatus?.toShortString()}")
        println("  ${history.status + history.state.status}")
    }
    println(result.first)
    println(result.first.status)
    founders.forEach { println(it) }
}
