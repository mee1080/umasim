/*
 * Copyright 2025 mee1080
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
package io.github.mee1080.umasim.scenario.legend

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.scenario.CommonScenarioEvents
import io.github.mee1080.umasim.scenario.allTrainingLevelUp
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.applyIf
import io.github.mee1080.utility.applyIfNotNull

class LegendScenarioEvents(
    private val forceBuffList: List<LegendBuff>? = null,
) : CommonScenarioEvents() {

    // TODO: 心得の効果一部
    // TODO: 緑導きトレ失敗無効
    // TODO: 青導きやる気低下無効
    // TODO: 赤導き親友ゲージ、強制配置

    override fun beforeSimulation(state: SimulationState): SimulationState {
        val legendStatus = LegendStatus()
        return super.beforeSimulation(state).copy(scenarioStatus = legendStatus)
    }

    override fun beforeAction(state: SimulationState): SimulationState {
        val base = super.beforeAction(state)
        val status = base.status
        return base.updateLegendStatus {
            copy(
                buffList = buffList.map {
                    val condition = it.buff.condition ?: return@map it
                    val newEnabled = condition.activateBeforeAction(status)
                            || (it.enabled && !condition.deactivateBeforeAction(status))
                    it.copy(enabled = newEnabled)
                }
            )
        }
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {

            // 心得獲得
            6 -> base.getBuff(selector)
            12 -> base.getBuff(selector)
            18 -> base.getBuff(selector)

            // J12後: Dream Fest Stella
            24 -> base.getBuff(selector)
                .dreamFest(25, 30, 125, true)

            // C1以降レース後: イベント（単純化のためC1前固定）
            25 -> base.addStatus(Status(speed = 20, skillPt = 20))

            // 心得獲得
            30 -> base.getBuff(selector)

            // C6後: 心得/導き獲得、全トレーニングLv+1
            36 -> base.getBuff(selector).getMastery().allTrainingLevelUp()

            // 心得獲得
            42 -> base.getBuff(selector)

            // C10後: イベント
            44 -> base.addAllStatus(status = 5, skillPt = 50)

            // C12後: Dream Fest Pride
            48 -> base.getBuff(selector)
                .dreamFest(35, 45, 210, true, skill = "時代を変える者")

            // S2後: イベント
            52 -> base.addAllStatus(status = 10, skillPt = 100)

            // 心得獲得
            54 -> base.getBuff(selector)
            60 -> base.getBuff(selector)
            66 -> base.getBuff(selector)

            // S10後: 金スキル（単純化のため機先の勝負固定）
            68 -> base.addStatus(Status(skillHint = mapOf("機先の勝負" to 1)))

            // S12後: イベント、Dream Fest Legend、終了イベント（全勝扱い）
            72 -> base.addAllStatus(status = 25, skillPt = 125)
                .dreamFest(0, 55, 300, false, skill = "時中の妙")
                .addAllStatus(status = 45, skillPt = 245, skillHint = mapOf("新たな伝説を築く者" to 1))

            else -> base
        }
    }

    private suspend fun SimulationState.getBuff(selector: ActionSelector): SimulationState {
        val legendStatus = legendStatus ?: return this
        val forceBuff = forceBuffList?.getOrNull(turn / 6)
        val selection = if (forceBuff != null) listOf(LegendSelectBuff(forceBuff)) else {
            val currentBuffCount = legendStatus.buffList.size
            val currentBuffSet = legendStatus.buffList.map { it.buff }.toSet()
            val maxRank = if (currentBuffCount >= 4) 3 else (if (currentBuffCount >= 2) 2 else 1)
            buildList {
                LegendMember.entries.forEach { legend ->
                    val gauge = legendStatus.buffGauge[legend]!!
                    if (gauge < 2) return@forEach
                    val restBuff = (legendBuffData[legend]!!.filter { it.rank <= maxRank } - currentBuffSet).shuffled()
                    add(restBuff[0])
                    if (gauge >= 4) add(restBuff[1])
                    if (gauge >= 8) add(restBuff.first { it.rank == maxRank && it != restBuff[0] && it != restBuff[1] })
                }
            }.map {
                LegendSelectBuff(it)
            }
        }
        val action = selector.select(this, selection)
        return applyAction(action, action.randomSelectResult())
    }

    private fun SimulationState.getMastery(): SimulationState {
        val legendStatus = legendStatus ?: return this
        val countMap = legendStatus.buffList.groupBy { it.buff.member }.mapValues { it.value.size }
        val maxCount = countMap.maxOf { it.value }
        val targets = countMap.filterValues { it == maxCount }.keys
        return setLegendMastery(targets.random())
    }

    private fun SimulationState.dreamFest(
        hp: Int, status: Int, skillPt: Int, trainingLevelUp: Boolean, skill: String? = null,
    ): SimulationState {
        return addStatus(raceStatus(5, status, skillPt) + Status(hp = hp))
            .applyIf(trainingLevelUp) { allTrainingLevelUp() }
            .applyIfNotNull(skill) { addStatus(Status(skillHint = mapOf(it to 1))) }
    }
}
