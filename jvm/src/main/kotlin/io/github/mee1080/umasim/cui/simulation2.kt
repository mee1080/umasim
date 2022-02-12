package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
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
    option: FactorBasedActionSelector2.Option,
    vararg output: suspend (card: SupportCard, summaries: List<Summary>) -> Unit = arrayOf({ card, summaries ->
        stdoutOutput(card, summaries)
    })
) {
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        Store.supportList.filter {
            talent.contains(it.talent) && it.rarity >= 2 && (it.type == targetStatus)
        },
        factor,
        78,
        testCount,
        { FactorBasedActionSelector2(option) },
        *output
    )
}

fun doSimulation2(
    scenario: Scenario,
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    target: List<SupportCard>,
    factor: List<Pair<StatusType, Int>>,
    testCount: Int,
    option: FactorBasedActionSelector2.Option,
    vararg output: suspend (card: SupportCard, summaries: List<Summary>) -> Unit = arrayOf({ card, summaries ->
        stdoutOutput(card, summaries)
    })
) {
    doSimulation2(
        scenario,
        chara,
        defaultSupport,
        target,
        factor,
        78,
        testCount,
        { FactorBasedActionSelector2(option) },
        *output
    )
}

fun doSimulation2(
    scenario: Scenario,
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    target: List<SupportCard>,
    factor: List<Pair<StatusType, Int>>,
    turn: Int,
    testCount: Int,
    selector: () -> ActionSelector,
    vararg output: suspend (card: SupportCard, summaries: List<Summary>) -> Unit = arrayOf({ card, summaries ->
        stdoutOutput(card, summaries)
    })
) {
    println(chara.name)
    defaultSupport.forEach { println(it.name) }
    println(factor.joinToString(",") { "${it.first} ${it.second}" })
    println("start ${LocalDateTime.now()}")
    runBlocking {
        target.map { card ->
            launch(context) {
                val useSupport = listOf(*defaultSupport, card)
                val summary = mutableListOf<Summary>()
                repeat(testCount) {
                    summary.add(
                        Simulator(scenario, chara, useSupport, factor).simulate(
                            turn,
                            selector(),
                            ApproximateSimulationEvents(),
                        )
                    )
                }
                output.forEach { it(card, summary) }
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