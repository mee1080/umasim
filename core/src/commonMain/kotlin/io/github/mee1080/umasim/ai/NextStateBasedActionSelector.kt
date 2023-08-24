package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.min

class NextStateBasedActionSelector(val option: Option = Option()) : ActionSelector {

    companion object {
        private const val DEBUG = true
    }

    data class Option(
        val speedFactor: Double = 1.0,
        val staminaFactor: Double = 1.0,
        val powerFactor: Double = 1.0,
        val gutsFactor: Double = 0.0,
        val wisdomFactor: Double = 0.0,
        val skillPtFactor: Double = 0.4,
        val hpFactor: Double = 0.5,
        val motivationFactor: Double = 15.0,
        val aoharuFactor: (Int) -> Double = {
            when {
                it <= 24 -> 15.0
                it <= 36 -> 10.0
                it <= 48 -> 5.0
                else -> 0.0
            }
        },
    ) {
        fun generateSelector() = NextStateBasedActionSelector(this)
    }

    override fun select(state: SimulationState, selection: List<Action>): Action {
        return selection.maxByOrNull { calcScore(state, it) } ?: selection.first()
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (DEBUG) println("${state.turn}: $action")
        val total = action.resultCandidate.sumOf { it.second }.toDouble()
        val score = action.resultCandidate.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            calcScore(state, action, it.first) * it.second / total
        }
        if (DEBUG) println("total $score")
        return score
    }

    private fun calcScore(state: SimulationState, action: Action, result: Status): Double {
        val next = state.applyAction(action, result)
        return calcStatusScore(next) + calcRelationScore(next) + calcAoharuScore(next)
    }

    private fun calcStatusScore(state: SimulationState): Double {
        val status = state.status
        val score = status.speed * option.speedFactor +
                status.stamina * option.staminaFactor +
                status.power * option.powerFactor +
                status.guts * option.gutsFactor +
                status.wisdom * option.wisdomFactor +
                status.skillPt * option.skillPtFactor +
                min(70, status.hp) * option.hpFactor +
                status.motivation * option.motivationFactor
        if (DEBUG) println("  status $score $status")
        return score
    }

    private fun calcRelationScore(state: SimulationState): Double {
        val score = state.support.sumOf { min(81, it.supportState?.relation ?: 0) } * 0.5
        if (DEBUG) println("  relation $score ${state.support.joinToString { it.supportState.toString() }}")
        return score
    }

    private fun calcAoharuScore(state: SimulationState): Double {
        if (state.scenario != Scenario.AOHARU) return 0.0
        val memberState = state.member.mapNotNull { it.scenarioState as? AoharuMemberState }
        val score = memberState.sumOf { min(5, it.aoharuTrainingCount) } * option.aoharuFactor(state.turn)
        if (DEBUG) println("  aoharu $score ${memberState.joinToString { it.toString() }}")
        return score
    }

    override fun toString(): String {
        return "NextStateBasedActionSelector $option"
    }
}