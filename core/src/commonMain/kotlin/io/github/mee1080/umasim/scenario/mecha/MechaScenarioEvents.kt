/*
 * Copyright 2024 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.scenario.mecha

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.randomSelect
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.CommonScenarioEvents
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.addGuest
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.applyIf
import io.github.mee1080.utility.mapIf

class MechaScenarioEvents : CommonScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        val mechaStatus = MechaStatus(MechaLinkEffect(state.support.map { it.charaName } + state.chara.charaName))
        return super.beforeSimulation(state).copy(scenarioStatus = mechaStatus)
    }

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.beforeAction(state, selector)
        val mechaStatus = base.mechaStatus ?: return base
        return when (base.turn) {

            // 1T: 研究メンバー参加
            1 -> base.addGuest(11, Scenario.MECHA)

            // スーパーオーバードライブ
            73, 74, 75, 76, 77, 78 -> base.applyIf(mechaStatus.ugeHistory.all { it == 2 }) {
                applyMechaOverdrive().updateMechaStatus { copy(overdriveGauge = 6) }
            }

            else -> base
        }
    }

    override fun beforePredict(state: SimulationState): SimulationState {
        var result = super.beforePredict(state)
        val mechaStatus = result.mechaStatus ?: return result

        // ギア配置
        val newGearExists = if (mechaStatus.overdrive && mechaStatus.odGearAll) {
            trainingType.associateWith { true }
        } else {
            val gearCount =
                randomSelect(mechaGearRate[state.turn / 24][mechaStatus.linkEffects.mechaGearFrequencyCount])
            val gearStatus = trainingType.asList().shuffled().take(gearCount)
            val friendStatus = state.member.filter { it.isFriendTraining(it.position) }.map { it.position }.toSet()
            trainingType.associateWith { friendStatus.contains(it) || gearStatus.contains(it) }
        }
        result = result.updateMechaStatus { copy(gearExists = newGearExists) }

        if (!mechaStatus.overdrive) return result

        // ODヒント全員
        if (mechaStatus.odHintAll) {
            val newMember = state.member.mapIf({ !it.guest && !it.outingType }) {
                it.copy(supportState = it.supportState?.copy(hintIcon = true))
            }
            result = result.copy(member = newMember)
        }

        // OD複数配置
        if (mechaStatus.odAddSupport) {
            val targetMember = state.member.filter { !it.guest && !it.outingType }.map { it.index }.shuffled().take(2)
            val supportPosition = trainingType.associateWith { 0 }.toMutableMap()
            state.member.forEach {
                it.positions.forEach { status ->
                    supportPosition[status] = supportPosition[status]!! + 1
                }
            }
            val targetPosition = supportPosition.filter { it.value < 5 }.keys.toMutableSet()
            val newMember = state.member.mapIf({ targetMember.contains(it.index) }) {
                val candidates = targetPosition - it.position
                it.applyIf(candidates.isNotEmpty()) {
                    val newPosition = candidates.random()
                    if (supportPosition[newPosition] == 4) {
                        targetPosition -= newPosition
                    }
                    it.copy(additionalPosition = setOf(newPosition))
                }
            }
            result = result.copy(member = newMember)
        }

        return result
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {

            // 2T: メカEN追加、チューニング（本来は3T開始時だが、実装の都合上、2T終了時に実行）
            2 -> base.setMechaEnergy(5)
                .tuning(selector)

            // 7T: 末脚Lv3
            7 -> base.addStatus(Status(skillHint = mapOf("末脚" to 3)))

            // J12後: UGE1回目
            24 -> base.upgradeExam(600, 10, 25, 11, true)
                .tuning(selector)

            // C6後: UGE2回目
            36 -> base.upgradeExam(1000, 15, 35, 17, false)
                .tuning(selector)

            // C12後: UGE3回目
            48 -> base.upgradeExam(1400, 20, 45, 23, true)
                .tuning(selector)

            // S6後: UGE4回目
            60 -> base.upgradeExam(1900, 25, 55, 29, false)
                .tuning(selector)

            // S12後: UGE5回目
            72 -> base.upgradeExam(2400, 30, 65, 35, true, hint = true)
                .tuning(selector)

            // F1T後: 選択スキル（シミュレーションに影響がないのでライツ博士固定）
            73 -> base.addStatus(Status(skillHint = mapOf("キラーチューン" to 1)))

            else -> base
        }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        val base = super.afterSimulation(state)
        val mechaStatus = base.mechaStatus ?: return base
        // 正確な分岐条件は未調査、全てS/全てA以上/Bあり、で実装
        return if (mechaStatus.ugeHistory.all { it == 2 }) {
            base.addStatus(
                Status(
                    45, 45, 45, 45, 45, 175,
                    skillHint = mapOf("もう少しだけ、いい景色" to 3),
                )
            )
        } else if (mechaStatus.ugeHistory.all { it >= 1 }) {
            base.addStatus(
                Status(
                    40, 40, 40, 40, 40, 12,
                    skillHint = mapOf("もう少しだけ、いい景色" to 1),
                )
            )
        } else {
            base.addStatus(
                Status(
                    30, 30, 30, 30, 30, 100,
                    skillHint = mapOf("夢の再生方法" to 1),
                )
            )
        }
    }

    private fun SimulationState.setMechaEnergy(value: Int) = updateMechaStatus {
        copy(maxMechaEnergy = value + linkEffects.initialMechaEnergyCount)
    }

    private suspend fun SimulationState.tuning(selector: ActionSelector): SimulationState {
        val maxMechaEnergy = mechaStatus?.maxMechaEnergy ?: return this
        var newState = updateMechaStatus { resetTuning() }
        repeat(maxMechaEnergy) {
            val mechaStatus = newState.mechaStatus!!
            val candidates = buildList {
                val chipLevels = mechaStatus.chipLevels
                if (chipLevels[MechaChipType.HEAD]!![0] < 5) add(MechaTuning(MechaChipType.HEAD, 0))
                if (chipLevels[MechaChipType.HEAD]!![1] < 5) add(MechaTuning(MechaChipType.HEAD, 1))
                if (newState.turn >= 36 && chipLevels[MechaChipType.HEAD]!![2] < 5) {
                    add(MechaTuning(MechaChipType.HEAD, 2))
                }
                if (chipLevels[MechaChipType.BODY]!![0] < 5) add(MechaTuning(MechaChipType.BODY, 0))
                if (chipLevels[MechaChipType.BODY]!![1] < 5) add(MechaTuning(MechaChipType.BODY, 1))
                if (newState.turn >= 60 && chipLevels[MechaChipType.BODY]!![2] < 5) {
                    add(MechaTuning(MechaChipType.BODY, 2))
                }
                if (chipLevels[MechaChipType.LEG]!![0] < 5) add(MechaTuning(MechaChipType.LEG, 0))
                if (chipLevels[MechaChipType.LEG]!![1] < 5) add(MechaTuning(MechaChipType.LEG, 1))
                if (newState.turn >= 60 && chipLevels[MechaChipType.LEG]!![2] < 5) {
                    add(MechaTuning(MechaChipType.LEG, 2))
                }
            }
            val action = selector.select(newState, candidates)
            newState = newState.applyAction(action, action.randomSelectResult())
        }
        return newState
    }

    private fun SimulationState.upgradeExam(
        target: Int, status: Int, skillPt: Int, maxMechaEnergy: Int, trainingLevelUp: Boolean, hint: Boolean = false,
    ): SimulationState {
        val totalLearningLevel = mechaStatus?.totalLearningLevel ?: return this
        return if (totalLearningLevel >= target) {
            addStatus(Status(status, status, status, status, status, skillPt))
                .setMechaEnergy(maxMechaEnergy)
                .updateMechaStatus { copy(ugeHistory = ugeHistory + 2) }
                .applyIf(trainingLevelUp) { allTrainingLevelUp() }
                .applyIf(hint) { addStatus(Status(skillHint = mapOf("全身全霊" to 3, "夢の再生方法" to 3))) }
        } else if (totalLearningLevel >= target / 10 * 7) {
            addStatus(Status(status, status, status, status, status, skillPt))
                .setMechaEnergy(maxMechaEnergy - 1)
                .updateMechaStatus { copy(ugeHistory = ugeHistory + 1) }
                .applyIf(trainingLevelUp) { allTrainingLevelUp() }
                .applyIf(hint) { addStatus(Status(skillHint = mapOf("全身全霊" to 1, "夢の再生方法" to 1))) }
        } else {
            val value = status - 5
            addStatus(Status(value, value, value, value, value, skillPt - 10))
                .setMechaEnergy(maxMechaEnergy - 2)
                .updateMechaStatus { copy(ugeHistory = ugeHistory + 0) }
        }
    }
}
