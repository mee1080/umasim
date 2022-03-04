package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

fun doSimulation2(
    scenario: Scenario,
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    targetStatus: StatusType,
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
            talent.contains(it.talent) && it.rarity >= 2 && (it.type == targetStatus)
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
                    selector = selector,
                )
                println("${card.id},${card.name},${card.talent},${evaluator.toSummaryString()},$score")
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