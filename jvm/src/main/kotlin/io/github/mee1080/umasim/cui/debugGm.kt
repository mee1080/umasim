package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.GmActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.simulation2.GmActivateWisdom
import io.github.mee1080.umasim.simulation2.RandomEvents
import io.github.mee1080.umasim.simulation2.Runner
import io.github.mee1080.umasim.simulation2.Simulator

fun main() {
    StoreLoader.load()
    gmSingleSimulation()
//    gmRunSimulation()
}

val gmMileEvaluateSetting = mapOf(
    StatusType.SPEED to (1.2 to 1800),
    StatusType.STAMINA to (1.4 to 700),
    StatusType.POWER to (1.2 to 1500),
    StatusType.GUTS to (0.6 to 1200),
    StatusType.WISDOM to (0.9 to 1200),
    StatusType.SKILL to (0.4 to Int.MAX_VALUE),
)

fun gmRunSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[泥まみれのコンパネーロ]エルコンドルパサー",
        "[///WARNING GATE///]シンコウウインディ",
        "[ほっと♪きゅーとメモリー]カレンチャン",
        "[Dear Mr. C.B.]ミスターシービー",
        "[おてんば魔女、修行中。]スイープトウショウ",
        "[嗚呼華麗ナル一族]ダイイチルビー",
    )
    println(chara)
    println(support)
    val selector = { GmActionSelector(GmActionSelector.speedWisdom) }
    val factor = listOf(
        StatusType.STAMINA to 3, StatusType.STAMINA to 3, StatusType.STAMINA to 3,
        StatusType.STAMINA to 3, StatusType.POWER to 3, StatusType.POWER to 3,
    )
    val summary = Runner.runAndEvaluate(
        1000,
        Scenario.GM,
        chara,
        support,
        factor,
        gmMileEvaluateSetting,
        selector = selector,
    )
    println("0,test,0,${summary.second.toSummaryString()},${summary.first}")
}

fun gmSingleSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[泥まみれのコンパネーロ]エルコンドルパサー",
        "[///WARNING GATE///]シンコウウインディ",
        "[ほっと♪きゅーとメモリー]カレンチャン",
        "[Dear Mr. C.B.]ミスターシービー",
        "[おてんば魔女、修行中。]スイープトウショウ",
        "[嗚呼華麗ナル一族]ダイイチルビー",
    )
    println(chara)
    println(support)
    val selector = GmActionSelector(
        GmActionSelector.speedWisdom
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
