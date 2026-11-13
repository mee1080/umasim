package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.ShuntaisaiActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.shuntaisai.ShuntaisaiScenarioEvents
import io.github.mee1080.umasim.simulation2.Runner

fun simulateShuntaisai() {
//    doShuntaisaiSimulationS2(StatusType.SPEED)
//    doShuntaisaiSimulationS2(StatusType.STAMINA)
    doShuntaisaiSimulationS2(StatusType.POWER)
    doShuntaisaiSimulationS2(StatusType.WISDOM)
//    doShuntaisaiSimulationS2(StatusType.POWER, "[Time of Departure]マヤノトップガン")
//    doShuntaisaiSimulationS2(StatusType.FRIEND, "[いとどめでたき、まつりかな]吾妻")
}

private fun doShuntaisaiSimulationS2(
    targetStatus: StatusType,
    targetSupport: String? = null,
    rarity: IntRange = 2..3,
) {
    val scenario = Scenario.SHUNTAISAI
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val defaultSupport = listOf(
        "[時に交わる海と空]ミスターシービー" to null,
        "[As if Guided]エフフォーリア" to StatusType.SPEED,
        "[その執念は怒濤が如く]メイショウドトウ" to StatusType.STAMINA,
        "[Time of Departure]マヤノトップガン" to StatusType.POWER,
        "[Innovator]フォーエバーヤング" to StatusType.WISDOM,
        "[いとどめでたき、まつりかな]吾妻" to StatusType.FRIEND,
    ).filter { it.second != targetStatus }.map { it.first to 4 }
    val support = Store.getSupportByName(*defaultSupport.toTypedArray())
    val testCount = 100000
    val ignoreDefaultSupportHint = true

    if (targetSupport == null) {
        doSimulation2(
            scenario,
            chara,
            support.toTypedArray(),
            targetStatus, rarity = rarity, talent = 0..4,
            factor = factor(StatusType.GUTS, 3) + factor(StatusType.WISDOM, 3),
            testCount = testCount,
            selector = { ShuntaisaiActionSelector() },
            evaluateSetting = Runner.shuntaisaiSetting,
            evaluateUpperRate = 0.2,
            scenarioEvents = { ShuntaisaiScenarioEvents() },
            ignoreDefaultSupportHint = ignoreDefaultSupportHint,
        )
    } else {
        doSimulation2(
            scenario,
            chara,
            support.toTypedArray(),
            Store.getSupportByName(*((0..4).map { targetSupport to it }.toTypedArray())),
            factor = factor(StatusType.GUTS, 3) + factor(StatusType.WISDOM, 3),
            testCount = testCount,
            selector = { ShuntaisaiActionSelector() },
            evaluateSetting = Runner.shuntaisaiSetting,
            evaluateUpperRate = 0.2,
            scenarioEvents = { ShuntaisaiScenarioEvents() },
            ignoreDefaultSupportHint = ignoreDefaultSupportHint,
        )
    }
}
