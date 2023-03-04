package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.GmActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.StoreLoader
import io.github.mee1080.umasim.simulation2.ApproximateSimulationEvents
import io.github.mee1080.umasim.simulation2.Simulator

fun main() {
    StoreLoader.load()
    gmSingleSimulation()
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
        .simulateWithHistory(78, selector, ApproximateSimulationEvents())
    result.second.forEachIndexed { index, history ->
        println("${index + 1}:")
        history.selections.forEach { (selection, selectedAction) ->
//            selection.forEach {
//                println("  ${it.scenarioActionParam?.toShortString()} : ${it.toShortString()}")
//            }
            println("  -> ${selectedAction.action?.toShortString() ?: ""}${selectedAction.scenarioAction ?: ""}")
        }
        println("  ${history.state.training.map { "${it.type}${it.level} " }}")
        println("  ${history.state.gmStatus}")
        println("  ${history.status + history.state.status}")
    }
    println(result.first)
    println(result.first.status)
}
