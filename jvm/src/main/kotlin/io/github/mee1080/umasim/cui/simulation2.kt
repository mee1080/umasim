package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.ActionSelector
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.simulation2.Summary
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

fun doSimulation2(
    scenario: Scenario,
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    targetStatus: StatusType,
    talent: IntRange = 4..4,
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
    turn: Int,
    testCount: Int,
    selector: () -> ActionSelector,
    vararg output: suspend (card: SupportCard, summaries: List<Summary>) -> Unit = arrayOf({ card, summaries ->
        stdoutOutput(card, summaries)
    })
) {
    println(chara.name)
    defaultSupport.forEach { println(it.name) }
    println("start ${LocalDateTime.now()}")
    runBlocking {
        target.map { card ->
            launch(context) {
                val useSupport = listOf(*defaultSupport, card)
                val summary = mutableListOf<Summary>()
                val option = Simulator.Option()
                repeat(testCount) {
                    summary.add(Simulator(scenario, chara, useSupport, option).simulate(turn, selector()))
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