package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.addGuest
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.applyIf
import kotlin.math.max
import kotlin.math.min

class MujintoScenarioEvents : BaseScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        val mujintoStatus = MujintoStatus(state.support.map { it.card })
        return super.beforeSimulation(state).copy(scenarioStatus = mujintoStatus)
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {

            // 評価会/建設計画
            2, 12, 24, 36, 48, 60 -> base.selectPlan(base.turn / 12, selector)

            // クラシック夏合宿後の施設獲得
            40 -> base.updateMujintoStatus { upgradeFacilityAfterCamp() }

            // シニア後半の島トレ券獲得
            64 -> base.updateMujintoStatus { copy(islandTrainingTicket = islandTrainingTicket + 2) }

            // シナリオ金スキル（タッカー固定）
            65 -> base.addStatus(Status(skillHint = mapOf("アガッてきた！" to 1)))

            else -> base
        }
    }

    private suspend fun SimulationState.selectPlan(phase: Int, selector: ActionSelector): SimulationState {
        val mujintoStatus = mujintoStatus ?: return this
        val maxSpace = mujintoFacilitySpace[mujintoStatus.linkMode][phase]
        val facilityPlan = if (phase == 5) emptyList() else {
            val plan = mutableListOf<MujintoFacility>()
            while (plan.sumOf { it.space } < maxSpace) {
                val restSpace = maxSpace - plan.sumOf { it.space }
                val candidates = buildList {
                    trainingType.forEach { type ->
                        if (plan.any { it.type == type }) return@forEach
                        val current = mujintoStatus.facilities[type]
                        val nextLevel = (current?.level ?: 0) + 1
                        when (nextLevel) {
                            1, 2 -> {
                                add(mujintoFacility(type, nextLevel, false))
                            }

                            3 -> {
                                add(mujintoFacility(type, nextLevel, false))
                                add(mujintoFacility(type, nextLevel, true))
                            }

                            4, 5 -> {
                                add(mujintoFacility(type, nextLevel, current!!.jukuren))
                            }
                        }
                    }
                    if (plan.all { it.type != StatusType.FRIEND }) {
                        val nextLevel = (mujintoStatus.facilities[StatusType.FRIEND]?.level ?: 0) + 1
                        if (nextLevel <= 3) {
                            add(mujintoFacility(StatusType.FRIEND, 1, false))
                        }
                    }
                }.filter { it.space <= restSpace }.map { MujintoAddPlan(MujintoAddPlanResult(it)) }
                if (candidates.isEmpty()) break
                val selected = selector.select(this, candidates) as MujintoAddPlan
                plan.add(selected.result.facility)
            }
            plan
        }
        val statusUp = mujintoEvaluationStatusUp[phase]
        val skillPt = mujintoEvaluationSkillPt[phase]
        val hpDivider = mujintoEvaluationHpDivider[phase]
        val hpMax = mujintoEvaluationHpMax[phase]
        val hp = min(hpMax, max(0, mujintoStatus.pioneerPoint - mujintoStatus.requiredPoint2) / hpDivider)
        val skillHint = if (phase == 5) mapOf("全身全霊" to 1) else emptyMap()
        return updateMujintoStatus { updatePhase(phase, facilityPlan) }
            .addAllStatus(status = statusUp, skillPt = skillPt, hp = hp, skillHint = skillHint)
            .addRandomSupportHint()
            .applyIf({ phase == 0 }) { addGuest(10, Scenario.MUJINTO) }
            .applyIf({ phase == 2 }) {
                allTrainingLevelUp().addGuest(13, Scenario.MUJINTO)
            }
            .applyIf({ phase == 4 }) {
                allTrainingLevelUp().addGuest(16, Scenario.MUJINTO)
            }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        return state.addAllStatus(status = 55, skillPt = 540, skillHint = mapOf("本能の懸け橋" to 1))
    }
}
