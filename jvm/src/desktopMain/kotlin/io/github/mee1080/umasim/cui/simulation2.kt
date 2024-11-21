package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

fun doSimulation2(
    scenario: Scenario,
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    targetStatus: StatusType,
    rarity: IntRange = 2..3,
    talent: IntRange = 4..4,
    factor: List<Pair<StatusType, Int>>,
    testCount: Int,
    option: ActionSelectorGenerator,
    evaluateSetting: Map<StatusType, Pair<Double, Int>> = Runner.mileEvaluateSetting,
) {
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        Store.supportList.filter {
            talent.contains(it.talent) && it.rarity in rarity && (it.type == targetStatus)
        },
        factor,
        testCount,
        { option.generateSelector() },
        evaluateSetting,
    )
}

fun doSimulation2(
    scenario: Scenario,
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    targetStatus: StatusType,
    rarity: IntRange = 2..3,
    talent: IntRange = 4..4,
    factor: List<Pair<StatusType, Int>>,
    testCount: Int,
    selector: () -> ActionSelector,
    evaluateSetting: Map<StatusType, Pair<Double, Int>> = Runner.mileEvaluateSetting,
) {
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        Store.supportList.filter {
            talent.contains(it.talent) && it.rarity in rarity && (it.type == targetStatus)
        }.sortedBy { -it.rarity * 1000000 + it.id * 10 + it.talent },
        factor,
        testCount,
        selector,
        evaluateSetting,
    )
}

fun doSimulation2(
    scenario: Scenario,
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    target: List<SupportCard>,
    factor: List<Pair<StatusType, Int>>,
    testCount: Int,
    option: ActionSelectorGenerator,
    evaluateSetting: Map<StatusType, Pair<Double, Int>> = Runner.mileEvaluateSetting,
) {
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        target,
        factor,
        testCount,
        { option.generateSelector() },
        evaluateSetting,
    )
}

fun doSimulation2(
    scenario: Scenario,
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    target: List<SupportCard>,
    factor: List<Pair<StatusType, Int>>,
    testCount: Int,
    selector: () -> ActionSelector,
    evaluateSetting: Map<StatusType, Pair<Double, Int>> = Runner.mileEvaluateSetting,
) {
    println(chara.name)
    defaultSupport.forEach { println(it.name) }
    println(factor.joinToString(",") { "${it.first} ${it.second}" })
    println("start ${LocalDateTime.now()}")
    runBlocking {
        target.map { card ->
            launch(context) {
                val useSupport = listOf(*defaultSupport, card)
                val (score, evaluator) = Runner.runAndEvaluate(
                    testCount,
                    scenario,
                    chara,
                    useSupport,
                    factor,
                    evaluateSetting,
//                    events = ApproximateSimulationEvents(
//                        beforeActionEvents = {
//                            return@ApproximateSimulationEvents if (it.turn == 13) {
//                                it.copy(condition = it.condition + "愛嬌○")
//                            } else it
//                        }
//                    ),
                    selector = selector,
                )
                println("${card.id},\"${card.name}\",${card.talent},${evaluator.toSummaryString()},$score")
                // ヒストグラム
//                evaluator.getStatusSum(evaluateSetting)
//                    .map { it.roundToInt() }
//                    .fold(mutableMapOf<Int, Int>()) { map, value ->
//                        map[value] = map.getOrPut(value) { 0 } + 1
//                        map
//                    }.toSortedMap().forEach { (value, count) ->
//                        println("$value\t$count")
//                    }
            }
        }.forEach {
            it.join()
        }
    }
    println("finished ${LocalDateTime.now()}")
}

val stdoutOutput = { card: SupportCard, summaries: List<Summary> ->
    println("${card.id},${card.name},${card.talent},${Evaluator(summaries).toSummaryString()}")
}