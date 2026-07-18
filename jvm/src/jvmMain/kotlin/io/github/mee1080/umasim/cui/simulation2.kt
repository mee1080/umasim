package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.ScenarioEvents
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.joinAll
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
    evaluateUpperRate: Double = 0.2,
    scenarioEvents: ((SimulationState) -> ScenarioEvents)? = null,
    events: (SimulationState) -> SimulationEvents = { RandomEvents(it) },
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
        evaluateUpperRate,
        scenarioEvents,
        events,
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
    evaluateUpperRate: Double = 0.2,
    scenarioEvents: ((SimulationState) -> ScenarioEvents)? = null,
    events: (SimulationState) -> SimulationEvents = { RandomEvents(it) },
) {
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        Store.supportList.filter {
            talent.contains(it.talent) && it.rarity in rarity && (it.type == targetStatus)
        }.sortedBy { -it.rarity * 1000000 + -it.id * 10 + it.talent },
        factor,
        testCount,
        selector,
        evaluateSetting,
        evaluateUpperRate,
        scenarioEvents,
        events,
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
    evaluateUpperRate: Double = 0.2,
    scenarioEvents: ((SimulationState) -> ScenarioEvents)? = null,
    events: (SimulationState) -> SimulationEvents = { RandomEvents(it) },
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
        evaluateUpperRate,
        scenarioEvents,
        events,
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
    evaluateUpperRate: Double = 0.2,
    scenarioEvents: ((SimulationState) -> ScenarioEvents)? = null,
    events: (SimulationState) -> SimulationEvents = { RandomEvents(it) },
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
                    evaluateUpperRate,
                    scenarioEvents,
                    events = events,
                    selector = selector,
                )
                println("${card.id}\t\"${card.name}\"\t${card.talent}\t${evaluator.toSummaryString("\t")}\t$score")
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
        }.joinAll()
    }
    println("finished ${LocalDateTime.now()}")
}

val stdoutOutput = { card: SupportCard, summaries: List<Summary> ->
    println("${card.id},${card.name},${card.talent},${Evaluator(summaries).toSummaryString()}")
}

fun compareSelector(
    scenario: Scenario,
    chara: Chara,
    support: List<SupportCard>,
    factor: List<Pair<StatusType, Int>>,
    testCount: Int,
    selectors: List<Pair<String, () -> ActionSelector>>,
    evaluateSetting: Map<StatusType, Pair<Double, Int>>,
    evaluateUpperRate: Double = 0.2,
    scenarioEvents: ((SimulationState) -> ScenarioEvents)? = null,
    events: (SimulationState) -> SimulationEvents = { RandomEvents(it) },
) {
    println(chara.name)
    support.forEach { println(it.name) }
    println(factor.joinToString(",") { "${it.first} ${it.second}" })
    println("start ${LocalDateTime.now()}")
    runBlocking {
        selectors.mapIndexed { index, (name, selector) ->
            launch(context) {
                val (score, evaluator) = Runner.runAndEvaluate(
                    testCount,
                    scenario,
                    chara,
                    support,
                    factor,
                    evaluateSetting,
                    evaluateUpperRate,
                    scenarioEvents,
                    events = events,
                    selector = selector,
                )
                println("$index,\"$name\",0,${evaluator.toSummaryString()},$score")
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
        }.joinAll()
    }
    println("finished ${LocalDateTime.now()}")
}

fun debugSimulation(
    scenario: Scenario,
    chara: Chara,
    support: List<SupportCard>,
    factor: List<Pair<StatusType, Int>>,
    selector: () -> ActionSelector,
    scenarioEvents: ((SimulationState) -> ScenarioEvents)? = null,
    events: (SimulationState) -> SimulationEvents = { RandomEvents(it) },
) {
    val result = runBlocking {
        Simulator(scenario, chara, support, factor)
            .simulateWithHistory(selector(), scenarioEvents, events)
    }
    result.second.forEachIndexed { index, history ->
        println()
        println("ターン ${index + 1}:")
        println("  開始時: ${history.beforeActionState.status.toShortString()}")
        println("  トレLv: ${history.beforeActionState.training.map { "${it.type}${it.level} " }}")
        println("  シナリオ: ${history.beforeActionState.scenarioStatus?.toShortString()}")
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
        println("  シナリオ: ${history.afterTurnState.scenarioStatus?.toShortString()}")
    }
    println(result.first)
    println(result.first.status.toShortString())
}