/*
 * Copyright 2021 mee1080
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
package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

@Suppress("unused")
class ClimaxFactorBasedActionSelector(val option: Option = Option()) : ActionSelector {

    companion object {
        private const val DEBUG = false

        val speed3Wisdom2Friend1 = Option().copy(
            speedFactor = 1.0,
            staminaFactor = 1.5,
            powerFactor = 1.8,
            gutsFactor = 1.3,
            wisdomFactor = 1.5,
            hpFactor = 0.75,
            motivationFactor = 25.0,
            relationFactor = { type, rank, _ ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        1 -> 9.0
                        2 -> 10.9
                        else -> 14.7
                    }
                    else -> when (rank) {
                        1 -> 11.6
                        else -> 12.5
                    }
                }
            }
        )

        val guts4Wisdom2 = Option().copy(
            speedFactor = 1.7,
            staminaFactor = 1.7,
            powerFactor = 1.7,
            gutsFactor = 0.3,
            wisdomFactor = 1.0,
            hpFactor = 0.6,
            motivationFactor = 25.0,
            relationFactor = { type, rank, _ ->
                when (type) {
                    StatusType.GUTS -> when (rank) {
                        1 -> 2.0
                        2 -> 2.6
                        3 -> 7.6
                        else -> 7.8
                    }
                    else -> when (rank) {
                        1 -> 9.4
                        else -> 8.7
                    }
                }
            }
        )

        val guts4Wisdom2Guts = Option().copy(
            speedFactor = 1.3,
            staminaFactor = 1.2,
            powerFactor = 1.3,
            gutsFactor = 0.7,
            wisdomFactor = 1.0,
            hpFactor = 0.6,
            motivationFactor = 25.0,
            relationFactor = { type, rank, _ ->
                when (type) {
                    StatusType.GUTS -> when (rank) {
                        1 -> 2.0
                        2 -> 2.6
                        3 -> 7.6
                        else -> 7.8
                    }
                    else -> when (rank) {
                        1 -> 9.4
                        else -> 8.7
                    }
                }
            }
        )

        val speed2guts2Wisdom2 = Option().copy(
            speedFactor = 1.36,
            staminaFactor = 1.83,
            powerFactor = 1.59,
            gutsFactor = 0.64,
            wisdomFactor = 1.46,
            hpFactor = 0.69,
            motivationFactor = 25.0,
            relationFactor = { type, rank, _ ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        1 -> 6.78
                        else -> 14.92
                    }
                    StatusType.GUTS -> when (rank) {
                        1 -> 9.93
                        else -> 4.18
                    }
                    else -> when (rank) {
                        1 -> 13.98
                        else -> 10.06
                    }
                }
            }
        )
    }

    @Serializable
    data class Option(
        val speedFactor: Double = 1.0,
        val staminaFactor: Double = 1.0,
        val powerFactor: Double = 1.0,
        val gutsFactor: Double = 0.4,
        val wisdomFactor: Double = 0.6,
        val skillPtFactor: Double = 0.4,
        val hpFactor: Double = 0.5,
        val motivationFactor: Double = 15.0,
        val relationFactor: (type: StatusType, rank: Int, count: Int) -> Double = { _: StatusType, rank: Int, count: Int ->
            when (count) {
                1 -> 3.0
                2 -> if (rank == 0) 5.5 else 7.0
                else -> when (rank) {
                    0 -> 7.0
                    1 -> 9.0
                    else -> 12.0
                }
            }
        },
        val expectedStatusFactor: Double = 0.0,
        val race: Map<Int, String> = mapOf(
            16 to "?????????????????????????????????",
            17 to "?????????????????????????????????",
            19 to "??????????????????????????????????????????",
            22 to "????????????????????????????????????????????????",
            23 to "???????????????????????????????????????",
            24 to "??????????????????????????????",
            27 to "???????????????",
            30 to "??????????????????????????????",
            31 to "?????????",
            33 to "NHK??????????????????",
            34 to "????????????????????????????????????",
            36 to "????????????",
            41 to "????????????",
            43 to "??????????????????????????????",
            44 to "?????????",
            46 to "????????????????????????????????????",
            48 to "????????????",
            50 to "????????????JCC",
            53 to "??????????????????????????????",
            54 to "?????????",
            56 to "??????????????????",
            57 to "???????????????????????????",
            59 to "????????????",
            65 to "????????????",
            67 to "??????????????????????????????",
            68 to "??????????????????",
            70 to "?????????????????????",
            72 to "????????????",
        )
    ) : ActionSelectorGenerator {
        override fun generateSelector() = ClimaxFactorBasedActionSelector(this)
    }

    private var lastItemCheckedTurn = -1

    private var whistleCount = 5

    private var amuletCount = 5

    override fun init(state: SimulationState) {
        lastItemCheckedTurn = -1
        whistleCount = 5
        amuletCount = 5
    }

    override fun select(state: SimulationState, selection: List<Action>): Action {
        val race = option.race[state.turn]
        return if (race == null) {
            selection
                .filterNot { it is Race }
                .maxByOrNull { calcScore(state, it) } ?: selection.first()
        } else {
            selection.first { it is Race && it.raceName == race }
        }
    }

    override fun selectWithItem(state: SimulationState, selection: List<Action>): SelectedAction {
        if (state.turn >= 13 && lastItemCheckedTurn != state.turn) {
            val itemList = (when (state.turn) {
                13 -> listOf("????????????BBQ?????????")
                25, 49 -> listOf("??????????????????")
                45, 58, 69 -> listOf("????????????????????????") + selectVitalOrAmulet(state)
                47, 60, 71 -> selectVital(state)
                37, 39, 61, 63,
                38, 40, 62, 64 -> selectCampItem(state, selection)
                74, 76, 78 -> listOf("????????????????????????")
                else -> emptyList()
            } + listOfNotNull(
                if (state.shopCoin > 150 && state.status.motivation < 2) {
                    "??????????????????????????????"
                } else null,
            ))
            if (!itemList.contains("???????????????????????????")) {
                lastItemCheckedTurn = state.turn
            }
            if (itemList.isNotEmpty()) {
                amuletCount -= itemList.count { it == "????????????????????????" }
                whistleCount -= itemList.count { it == "???????????????????????????" }
                val list = itemList.map { Store.Climax.getShopItem(it) }
                return SelectedAction(
                    buyItem = list,
                    useItem = list,
                )
            }
        }
        if (state.possessionItem.isNotEmpty()) {
            return SelectedAction(useItem = state.possessionItem)
        }
        return SelectedAction(action = select(state, selection))
    }

    private fun selectVitalOrAmulet(state: SimulationState): List<String> {
        if (amuletCount <= 0) return selectVital(state)
        return listOfNotNull(
            when {
                state.status.hp <= 30 -> "????????????????????????"
                state.status.hp <= 50 -> "????????????20"
                else -> null
            }
        )
    }

    private fun selectVital(state: SimulationState): List<String> {
        return listOfNotNull(
            when {
                state.status.hp <= 20 -> "????????????65"
                state.status.hp <= 40 -> "????????????40"
                state.status.hp <= 50 -> "????????????20"
                else -> null
            }
        )
    }

    private fun selectCampItem(state: SimulationState, selection: List<Action>): List<String> {
        val topTraining = selection.filterIsInstance<Training>().filter { it.type != StatusType.WISDOM }.map {
            it to calcScore(it.baseStatus)
        }.maxByOrNull { it.second } ?: return emptyList()
        if (whistleCount > 0) {
            val expected = (trainingType.maxOfOrNull { calcExpectedScore(state, it) } ?: 0.0)
            if (topTraining.second - expected < 0.0) return listOf("???????????????????????????")
        }

        val topTrainingType = topTraining.first.type
        val megaphone = if (state.enableItem.megaphone == null) "?????????????????????????????????" else null
        val weight = if (topTrainingType == StatusType.WISDOM) null else "${topTrainingType.displayName}????????????????????????"
        val vital = selectVitalOrAmulet(state)
        return listOfNotNull(megaphone, weight) + vital
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (DEBUG) println("${state.turn}: $action")
        val total = action.resultCandidate.sumOf { it.second }.toDouble()
        val expected = if (action !is Training || option.expectedStatusFactor <= 0.0) 0.0 else {
            option.expectedStatusFactor * calcExpectedScore(state, action.type)
        }
        val score = action.resultCandidate.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            (calcScore(calcExpectedHintStatus(action) + it.first) - expected) * it.second / total
        } + calcRelationScore(state, action)
        if (DEBUG) println("total $score")
        return score
    }

    private fun calcExpectedHintStatus(action: Action): ExpectedStatus {
        if (action !is Training) return ExpectedStatus()
        val target = action.member.filter { it.hint }.map { it.card.hintStatus }
        if (target.isEmpty()) return ExpectedStatus()
        val rate = 1.0 / target.size
        return target.fold(ExpectedStatus()) { acc, status -> acc.add(rate, status) }
    }

    private fun calcScore(status: StatusValues): Double {
        val score = status.speed.toDouble() * option.speedFactor +
                status.stamina.toDouble() * option.staminaFactor +
                status.power.toDouble() * option.powerFactor +
                status.guts.toDouble() * option.gutsFactor +
                status.wisdom.toDouble() * option.wisdomFactor +
                status.skillPt.toDouble() * option.skillPtFactor +
                status.hp.toDouble() * option.hpFactor +
                status.motivation.toDouble() * option.motivationFactor
        if (DEBUG) println("  $score $status")
        return score
    }

    private fun calcExpectedScore(state: SimulationState, type: StatusType): Double {
        val expectedStatus = Calculator.calcExpectedTrainingStatus(
            Calculator.CalcInfo(
                state.chara,
                state.getTraining(type).current,
                state.status.motivation,
                state.member,
                state.scenario,
                state.supportTypeCount,
                state.status.fanCount,
                state.status,
                state.totalRelation,
            ),
        ).first
        return expectedStatus.speed * option.speedFactor +
                expectedStatus.stamina * option.staminaFactor +
                expectedStatus.power * option.powerFactor +
                expectedStatus.guts * option.gutsFactor +
                expectedStatus.wisdom * option.wisdomFactor +
                expectedStatus.skillPt * option.skillPtFactor +
                expectedStatus.hp * option.hpFactor +
                expectedStatus.motivation * option.motivationFactor
    }

    private fun calcRelationScore(state: SimulationState, action: Action): Double {
        if (action !is Training) return 0.0
        val supportRank = mutableMapOf<StatusType, MutableList<Pair<MemberState, Int>>>()
        state.support.forEach {
            supportRank.getOrPut(it.card.type) { mutableListOf() }.add(it to it.relation)
        }
        supportRank.values.forEach { list -> list.sortByDescending { it.second } }
        val score = action.support.sumOf { support ->
            if (support.relation >= support.card.requiredRelation) return@sumOf 0.0
            val list = supportRank[support.card.type]!!
            val rank = list.indexOfFirst { it.first == support }
            option.relationFactor(support.card.type, rank, list.size)
        }
        if (DEBUG) println("  relation $score")
        return score
    }

    override fun toString(): String {
        return "ClimaxFactorBasedActionSelector $option"
    }
}