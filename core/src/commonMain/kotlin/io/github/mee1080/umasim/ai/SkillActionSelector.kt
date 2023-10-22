package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.*

class SkillActionSelector(targetSkills: Array<String>) : ActionSelector {

    companion object {
        val deBuffSkills = arrayOf(
            "トリック（前）",
            "トリック（後）",
            "逃げけん制",
            "逃げ焦り",
            "逃げためらい",
            "先行けん制",
            "先行焦り",
            "先行ためらい",
            "差しけん制",
            "差し焦り",
            "差しためらい",
            "追込けん制",
            "追込焦り",
            "追込ためらい",
            "後方釘付",
            "抜け駆け禁止",
            "スピードイーター",
            "布石",
            "束縛",
            "ささやき",
            "スタミナイーター",
            "目くらまし",
            "リスタート",
            "かく乱",
            "鋭い眼光",
            "まなざし",
        )

        val jigatame = arrayOf("地固め")
    }

    private val targetSkillSet = targetSkills.toSet()

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        val byRate = selection
            .map { it to calcDeBuffRate(state, it) }
            .filter { it.second > 0.0 }
            .sortedByDescending { it.second }
        return when {
            byRate.isNotEmpty() -> byRate.first().first
            state.status.motivation < 2 -> selectOuting(selection)
            state.status.hp >= 95 -> selectTraining(selection, StatusType.SPEED)
            state.status.hp >= 60 -> selectTraining(selection, StatusType.WISDOM)
            else -> selectSleep(selection)
        }
    }

    private fun calcDeBuffRate(state: SimulationState, action: Action): Double {
        if (action !is Training || action.failureRate >= 30) return 0.0
        val currentSkills = state.status.skillHint
        val hintRates = action.member.filter { it.hint }.map { member ->
            val skills = member.card.skills.filter { !currentSkills.containsKey(it) }
            skills.count { targetSkillSet.contains(it) }.toDouble() / (skills.size + 1)
        }
        val hintRate = if (hintRates.isEmpty()) 0.0 else hintRates.average()
        val aoharuSkills = action.member
            .filter { it.scenarioState is AoharuMemberState && !it.scenarioState.aoharuBurn && it.scenarioState.aoharuIcon }
            .flatMap { it.card.skills }
            .filter { !currentSkills.containsKey(it) }
        val aoharuRate = if (aoharuSkills.isEmpty()) 0.0 else aoharuSkills.count { targetSkillSet.contains(it) }
            .toDouble() / aoharuSkills.size * 0.15
        return (hintRate + aoharuRate) * (100.0 - action.failureRate) / 100.0
    }
}