package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.RamenActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.ramen.RamenScenarioEvents
import io.github.mee1080.umasim.simulation2.Runner

fun simulateRamen() {
//    doRamenSimulation(StatusType.SPEED, "[永久の誓い、永久の輝き]サトノダイヤモンド")
    doRamenSimulation(StatusType.SPEED, "[天才的ユートピア]トウカイテイオー")
//    doRamenSimulation(StatusType.SPEED)
//    doRamenSimulation(StatusType.STAMINA, "[その執念は怒濤が如く]メイショウドトウ")
//    doRamenSimulation(StatusType.POWER, "[スマイル・エバーアフター]グランアレグリア")
//    doRamenSimulation(StatusType.WISDOM)
//    doRamenSimulation(StatusType.FRIEND, "[American Dream]カジノドライヴ")
//    doRamenSimulation2(StatusType.GUTS, "[単焦点でつかまえて]アストンマーチャン")
//    optimize()
}

private fun doRamenSimulation(
    targetStatus: StatusType,
    targetSupport: String? = null,
    rarity: IntRange = 2..3,
) {
    val scenario = Scenario.RAMEN
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val defaultSupport = listOf(
        "[心覚えし、京の華]エアグルーヴ" to null,
        "[天才的ユートピア]トウカイテイオー" to StatusType.SPEED,
        "[その執念は怒濤が如く]メイショウドトウ" to null,
        "[賑やかな未来を乗せて走れ！]サクラチヨノオー" to StatusType.STAMINA,
        "[Innovator]フォーエバーヤング" to StatusType.WISDOM,
        "[一杯のノスタルジア]駿川たづな" to StatusType.FRIEND,
    ).filter { it.second != targetStatus }.map { it.first to 4 }
    val support = Store.getSupportByName(*defaultSupport.toTypedArray())
        .map { it.copy(skills = emptyList()) }
    val testCount = 100

    if (targetSupport == null) {
        doSimulation2(
            scenario,
            chara,
            support.toTypedArray(),
            targetStatus, rarity = rarity, talent = 0..4,
            factor = factor(StatusType.SPEED, 6),
            testCount = testCount,
            selector = { RamenActionSelector() },
            evaluateSetting = Runner.ramenSetting,
            evaluateUpperRate = 0.2,
            scenarioEvents = { RamenScenarioEvents() },
        )
    } else {
        doSimulation2(
            scenario,
            chara,
            support.toTypedArray(),
            Store.getSupportByName(*((0..4).map { targetSupport to it }.toTypedArray())),
            factor = factor(StatusType.SPEED, 6),
            testCount = testCount,
            selector = { RamenActionSelector() },
            evaluateSetting = Runner.ramenSetting,
            evaluateUpperRate = 0.2,
            scenarioEvents = { RamenScenarioEvents() },
        )
    }
}

fun debugRamenSimulation() {
    val scenario = Scenario.RAMEN
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val defaultSupport = listOf(
        "[心覚えし、京の華]エアグルーヴ" to null,
        "[天才的ユートピア]トウカイテイオー" to StatusType.SPEED,
        "[その執念は怒濤が如く]メイショウドトウ" to null,
        "[賑やかな未来を乗せて走れ！]サクラチヨノオー" to StatusType.STAMINA,
        "[Innovator]フォーエバーヤング" to StatusType.WISDOM,
        "[一杯のノスタルジア]駿川たづな" to StatusType.FRIEND,
    ).map { it.first to 4 }
    val support = Store.getSupportByName(*defaultSupport.toTypedArray())
        .map { it.copy(skills = emptyList()) }
    debugSimulation(
        scenario = scenario,
        chara = chara,
        support = support,
        factor = factor(StatusType.SPEED, 6),
        selector = { RamenActionSelector() },
        scenarioEvents = { RamenScenarioEvents() },
    )
}