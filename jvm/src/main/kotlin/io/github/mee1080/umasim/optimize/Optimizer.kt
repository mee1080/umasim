package io.github.mee1080.umasim.optimize

import io.github.mee1080.umasim.cui.context
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.simulation2.ActionSelector
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.simulation2.Summary
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class Optimizer<T : ActionSelector>(
    private val scenario: Scenario,
    private val chara: Chara,
    private val support: List<SupportCard>,
    private val option: Simulator.Option,
    private val turn: Int,
    private val selectors: List<T>,
    private val calcScore: (Evaluator) -> Double,
) {

    companion object {
        private const val DEBUG = true
    }

    fun optimize(): List<Pair<T, List<Summary>>> {
        var targets = selectors.toMutableList()
        targets = filterByScore(100, 128, targets).toMutableList()
        targets = filterByScore(1000, 32, targets).toMutableList()
        targets = filterByScore(10000, 12, targets).toMutableList()
        return sortByScore(50000, targets).map { it.first }
    }

    private fun filterByScore(
        testCount: Int,
        size: Int,
        selectors: List<T>,
    ): List<T> {
        return if (selectors.size > size) {
            sortByScore(testCount, selectors).subList(0, size).map { it.first.first }
        } else selectors
    }

    private fun sortByScore(
        testCount: Int,
        selectors: List<T>,
    ): List<Pair<Pair<T, List<Summary>>, Double>> {
        val result = simulate(testCount, selectors).map {
            it to calcScore(Evaluator(it.second))
        }.sortedByDescending {
            it.second
        }
        if (DEBUG) result.forEach {
            println("$testCount: ${it.second} ${it.first.first}")
        }
        return result
    }

    private fun simulate(
        testCount: Int,
        selectors: List<T>,
    ): List<Pair<T, List<Summary>>> {
        val result = Collections.synchronizedList(mutableListOf<Pair<T, List<Summary>>>())
        runBlocking {
            selectors.forEachIndexed { index, target ->
                launch(context) {
                    val summary = mutableListOf<Summary>()
                    repeat(testCount) {
                        summary.add(Simulator(scenario, chara, support, option).simulate(turn, target))
                    }
                    result.add(target to summary)
                    if (DEBUG) println("$testCount: ${index + 1}/${selectors.size}")
                }
            }
        }
        return result
    }
}