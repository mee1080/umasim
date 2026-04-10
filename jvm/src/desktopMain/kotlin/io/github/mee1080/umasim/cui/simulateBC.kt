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
//    doBCSimulation(StatusType.SPEED, "[天才的ユートピア]トウカイテイオー")
//    doBCSimulation(StatusType.SPEED)
//    doBCSimulation(StatusType.STAMINA)
//    doBCSimulation(StatusType.POWER)
//    doBCSimulation(StatusType.WISDOM)
//    doBCSimulation(StatusType.FRIEND, "[American Dream]カジノドライヴ")
    doBCSimulation2(StatusType.GUTS, "[単焦点でつかまえて]アストンマーチャン")
//    optimize()
}

private fun doBCSimulation(
    targetStatus: StatusType,
    targetSupport: String? = null,
    rarity: IntRange = 2..3,
) {
    val scenario = Scenario.BC
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val defaultSupport = listOf(
        "[心覚えし、京の華]エアグルーヴ" to StatusType.SPEED,
        "[天才的ユートピア]トウカイテイオー" to null,
        "[ぬくもりのノエル]フェノーメノ" to StatusType.STAMINA,
        "[星跨ぐメッセージ]ネオユニヴァース" to StatusType.POWER,
        "[Innovator]フォーエバーヤング" to StatusType.WISDOM,
        "[American Dream]カジノドライヴ" to StatusType.FRIEND,
    ).filter { it.second != targetStatus }.map { it.first to 4 }
    val support = Store.getSupportByName(*defaultSupport.toTypedArray())
        .map { it.copy(skills = emptyList()) }
    val testCount = 100000
    val route = BCRoute.Classic

    if (targetSupport == null) {
        doSimulation2(
            scenario,
            chara,
            support.toTypedArray(),
            targetStatus, rarity = rarity, talent = 0..4,
            factor = factor(StatusType.SPEED, 6),
            testCount = testCount,
            selector = { BCActionSelector() },
            evaluateSetting = Runner.bcSetting,
            evaluateUpperRate = 0.2,
            scenarioEvents = { BCScenarioEvents(route) },
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
            scenarioEvents = { BCScenarioEvents(route) },
        )
    }
}

private val dreamTrainingTargetGuts = listOf(
    StatusType.WISDOM, StatusType.WISDOM,
    StatusType.SPEED, StatusType.SPEED,
    StatusType.SPEED, StatusType.GUTS,
    StatusType.WISDOM, StatusType.GUTS,
    StatusType.GUTS, StatusType.GUTS,
    StatusType.GUTS, StatusType.POWER, StatusType.POWER, StatusType.POWER,
)

private fun doBCSimulation2(
    targetStatus: StatusType,
    targetSupport: String? = null,
    rarity: IntRange = 2..3,
) {
    val scenario = Scenario.BC
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val defaultSupport = listOf(
        "[心覚えし、京の華]エアグルーヴ" to StatusType.SPEED,
        "[響け、二人の凱歌]マルシュロレーヌ" to null,
        "[気まぐれ渡り星]ステイゴールド" to StatusType.GUTS,
        "[星跨ぐメッセージ]ネオユニヴァース" to StatusType.POWER,
        "[Innovator]フォーエバーヤング" to StatusType.WISDOM,
        "[American Dream]カジノドライヴ" to StatusType.FRIEND,
    ).filter { it.second != targetStatus }.map { it.first to 4 }
    val support = Store.getSupportByName(*defaultSupport.toTypedArray())
        .map { it.copy(skills = emptyList()) }
    val testCount = 100000
    val route = BCRoute.Classic
    val factor = factor(StatusType.STAMINA, 6)

    val wisdom = listOf(100, 100, 90, 100)
    val hp = listOf(650, 750, 750, 600)
    val options = BCActionSelector.deafultOptions.mapIndexed { index, option ->
        option.copy(
            dreamTrainingTarget = dreamTrainingTargetGuts,
            wisdom = wisdom[index],
            hp = hp[index],
        )
    }.toTypedArray()

    if (targetSupport == null) {
        doSimulation2(
            scenario,
            chara,
            support.toTypedArray(),
            targetStatus, rarity = rarity, talent = 0..4,
            factor = factor,
            testCount = testCount,
            selector = { BCActionSelector(*options) },
            evaluateSetting = Runner.bcSetting,
            evaluateUpperRate = 0.2,
            scenarioEvents = { BCScenarioEvents(route) },
        )
    } else {
        doSimulation2(
            scenario,
            chara,
            support.toTypedArray(),
            Store.getSupportByName(*((0..4).map { targetSupport to it }.toTypedArray())),
            factor = factor,
            testCount = testCount,
            selector = { BCActionSelector(*options) },
            evaluateSetting = Runner.bcSetting,
            evaluateUpperRate = 0.2,
            scenarioEvents = { BCScenarioEvents(route) },
        )
    }
}

private fun optimize() {
    val scenario = Scenario.BC
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[心覚えし、京の華]エアグルーヴ" to 4,
        "[響け、二人の凱歌]マルシュロレーヌ" to 4,
        "[気まぐれ渡り星]ステイゴールド" to 4,
        "[星跨ぐメッセージ]ネオユニヴァース" to 4,
        "[Innovator]フォーエバーヤング" to 4,
        "[American Dream]カジノドライヴ" to 4,
    )
    val selectors = buildList {
        val wisdomList = listOf(90, 90, 100, 100)
        val hpList = listOf(650, 700, 700, 600)
        repeat(4) { target ->
            val wisdomBase = wisdomList[target]
            val hpBase = hpList[target]
            for (wisdom in (wisdomBase - 20)..(wisdomBase + 20) step 10) {
                for (hp in (hpBase - 100)..(hpBase + 100) step 50) {
                    val options = BCActionSelector.deafultOptions.mapIndexed { index, option ->
                        if (index == target) option.copy(
                            dreamTrainingTarget = dreamTrainingTargetGuts,
                            wisdom = wisdom,
                            hp = hp,
                        ) else option.copy(
                            dreamTrainingTarget = dreamTrainingTargetGuts,
                        )
                    }
                    add("$target/$wisdom/$hp" to { BCActionSelector(*options.toTypedArray()) })
                }
            }
        }
    }
    compareSelector(
        scenario,
        chara,
        support,
        factor = factor(StatusType.STAMINA, 6),
        testCount = 50000,
        selectors = selectors,
        evaluateSetting = Runner.bcSetting,
        evaluateUpperRate = 0.2,
        scenarioEvents = { BCScenarioEvents(BCRoute.Classic) },
    )
}
