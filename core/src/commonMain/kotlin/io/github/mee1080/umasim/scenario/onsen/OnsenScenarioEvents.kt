package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.simulation2.*

data class SelectGensenResult(
    val type: GensenType
) : ActionResult, ScenarioActionResult

data class SelectGensen(
    val type: GensenType
) : Action, ScenarioAction {
    override val result: SelectGensenResult = SelectGensenResult(type)
}

class OnsenScenarioEvents : BaseScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        val onsenStatus = OnsenStatus(state.support.map { it.card })
        return super.beforeSimulation(state).copy(scenarioStatus = onsenStatus)
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {
            // 源泉選択
            2 -> base.selectGensen(selector)

            // 湯浴み会
            24, 48, 72 -> base.yuyamiKai()

            // シナリオキャラクタイベント
            61 -> base.scenarioCharacterEvents(selector)

            else -> base
        }
    }

    private suspend fun SimulationState.selectGensen(selector: ActionSelector): SimulationState {
        val onsenStatus = onsenStatus ?: return this
        val availableGensen = GensenType.values().toList()

        val candidates = availableGensen.map { SelectGensen(it) }
        val selected = selector.select(this, candidates) as SelectGensen

        return updateOnsenStatus { copy(activeGensen = selected.type) }
    }

    private fun SimulationState.yuyamiKai(): SimulationState {
        val onsenStatus = onsenStatus ?: return this
        val year = when (turn) {
            24 -> 1
            48 -> 2
            72 -> 3
            else -> 0
        }
        val rewards = yuyamiKaiRewards[year] ?: return this
        val reward = rewards.getOrElse(onsenStatus.excavatedGensenCount) { rewards.last() }

        return addAllStatus(status = reward.status, skillPt = reward.skillPt)
    }

    private suspend fun SimulationState.scenarioCharacterEvents(selector: ActionSelector): SimulationState {
        var newState = this
        if (support.any { it.card.chara == "トウカイテイオー" }) {
            newState = newState.addStatus(Status(speed = 15, skillHint = mapOf("アンストッパブル" to 1)))
        }
        if (support.any { it.card.chara == "ミホノブルボン" }) {
            newState = newState.addStatus(Status(stamina = 15, skillHint = mapOf("踏ませぬ影" to 1)))
        }
        if (support.any { it.card.chara == "トランセンド" }) {
            newState = newState.addStatus(Status(power = 15, skillHint = mapOf("コンセントレーション" to 1)))
        }
        if (support.any { it.card.chara == "ホッコータルマエ" }) {
            newState = newState.addStatus(Status(guts = 15, skillHint = mapOf("見事な砂蹴り" to 1)))
        }
        if (support.any { it.card.chara == "ワンダーアキュート" }) {
            newState = newState.addStatus(Status(wisdom = 15, skillHint = mapOf("猛追" to 1)))
        }
        if (support.any { it.card.chara == "保科健子" }) {
             newState = newState.updateOnsenStatus { copy(bathTickets = (bathTickets + 1).coerceAtMost(3)) }
                .addStatus(Status(skillHint = mapOf("全身全霊" to 1)))
        }
        return newState
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        val excavatedCount = state.onsenStatus?.excavatedGensenCount ?: 0
        val finalStatus = Status(all = 20 + excavatedCount * 5)
        val finalSkillPt = 100 + excavatedCount * 20
        return state.addAllStatus(status = finalStatus, skillPt = finalSkillPt, skillHint = mapOf("ユノハナブルーム" to 1))
    }
}
