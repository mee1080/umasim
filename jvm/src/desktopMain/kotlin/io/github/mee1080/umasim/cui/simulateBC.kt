package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.BCActionSelector
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.bc.BCRoute
import io.github.mee1080.umasim.scenario.bc.BCScenarioEvents
import io.github.mee1080.umasim.simulation2.Runner

fun simulateBC() {
//    doBCSimulation(StatusType.SPEED, "[心覚えし、京の華]エアグルーヴ")
    doBCSimulation(StatusType.SPEED)
    doBCSimulation(StatusType.STAMINA)
    doBCSimulation(StatusType.POWER)
    doBCSimulation(StatusType.WISDOM)
//    optimize()
}

private fun doBCSimulation(
    targetStatus: StatusType,
    targetSupport: String? = null,
) {
    val scenario = Scenario.BC
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val defaultSupport = listOf(
        "[心覚えし、京の華]エアグルーヴ" to StatusType.SPEED,
        "[天才的ユートピア]トウカイテイオー" to null,
        "[ぬくもりのノエル]フェノーメノ" to StatusType.STAMINA,
        "[星跨ぐメッセージ]ネオユニヴァース" to StatusType.POWER,
        "[Innovator]フォーエバーヤング" to StatusType.WISDOM,
        "[American Dream]カジノドライヴ" to null,
    ).filter { it.second != targetStatus }.map { it.first to 4 }
    val support = Store.getSupportByName(*defaultSupport.toTypedArray())
    val testCount = 100000

    if (targetSupport == null) {
        doSimulation2(
            scenario,
            chara,
            support.toTypedArray(),
            targetStatus, rarity = 2..3, talent = 0..4,
            factor = factor(StatusType.SPEED, 6),
            testCount = testCount,
            selector = { BCActionSelector() },
            evaluateSetting = Runner.bcSetting,
            evaluateUpperRate = 0.2,
            scenarioEvents = { BCScenarioEvents(BCRoute.Classic) },
        )
    } else {
        doSimulation2(
            scenario,
            chara,
            support.toTypedArray(),
            Store.getSupportByName(*((0..4).map { targetSupport to it }.toTypedArray())),
            factor = factor(StatusType.SPEED, 6),
            testCount = testCount,
            selector = { BCActionSelector() },
            evaluateSetting = Runner.bcSetting,
            evaluateUpperRate = 0.2,
            scenarioEvents = { BCScenarioEvents(BCRoute.Classic) },
        )
    }
}

private fun optimize() {
    val scenario = Scenario.BC
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[心覚えし、京の華]エアグルーヴ" to 4,
        "[天才的ユートピア]トウカイテイオー" to 4,
        "[ぬくもりのノエル]フェノーメノ" to 4,
        "[星跨ぐメッセージ]ネオユニヴァース" to 4,
        "[Innovator]フォーエバーヤング" to 4,
        "[American Dream]カジノドライヴ" to 4,
    )
    val selectors = buildList {
        for (hp1 in 600..650 step 50) {
            for (hp2 in 750..850 step 50) {
                for (hp3 in 650..750 step 50) {
                    for (hp4 in 550..600 step 50) {
                        val hp = listOf(hp1, hp2, hp3, hp4)
                        val options = BCActionSelector.deafultOptions.mapIndexed { index, option ->
                            option.copy(hp = hp[index])
                        }
                        add(hp.joinToString("_") to { BCActionSelector(*options.toTypedArray()) })
                    }
                }
            }
        }
    }
    compareSelector(
        scenario,
        chara,
        support,
        factor = factor(StatusType.SPEED, 6),
        testCount = 10000,
        selectors = selectors,
        evaluateSetting = Runner.bcSetting,
        evaluateUpperRate = 0.2,
        scenarioEvents = { BCScenarioEvents(BCRoute.Classic) },
    )
}
