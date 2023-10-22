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

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Founder
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.simulation2.*

@Suppress("unused")
class GmActionSelector(val option: Option = Option()) : ActionSelector {

    companion object {
        private const val DEBUG = false

        /**
         * 叡智Lv（R,B,Y） -> 色補正値（R,B,Y）
         */
//        private val targetFounderSetting: Map<Triple<Int, Int, Int>, DoubleArray> = buildMap {
//            for (r in 0..5) {
//                for (b in 0..5) {
//                    for (y in 0..5) {
//                        put(Triple(r, b, y), targetSetting(r, b, y))
//                    }
//                }
//            }
//        }

        fun targetFounderSetting(r: Int, b: Int, y: Int): DoubleArray {
            // 初手青（他色になった場合も青獲得まで青狙い）
            if (b == 0) return doubleArrayOf(-0.5, 1.0, -0.5)
            // 初手青→2手目黄（赤は年末狙い）
            if (y == 0) return doubleArrayOf(-0.5, -0.5, 1.0)
            // まず3種1にする
            if (r == 0) return doubleArrayOf(1.0, -0.5, -0.5)

            return doubleArrayOf(0.0, 0.0, 0.0)
        }

        val speed3Power1Wisdom2SR = Option(
            speedFactor = 1.7,
            staminaFactor = 1.9,
            powerFactor = 1.8,
            gutsFactor = 0.5,
            wisdomFactor = 1.3,
            skillPtFactor = 0.8,
            hpFactor = 1.4,
            motivationFactor = 15.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        0 -> 9.4
                        1 -> 19.0
                        else -> 18.9
                    }

                    StatusType.POWER -> 17.3

                    else -> when (rank) {
                        0 -> 14.1
                        else -> 10.3
                    }
                }
            },
        )

        val speed2Power1Guts1Wisdom2 = Option(
            speedFactor = 1.4,
            staminaFactor = 1.48,
            powerFactor = 1.3,
            gutsFactor = 0.6,
            wisdomFactor = 0.6,
            skillPtFactor = 0.54,
            hpFactor = 1.24,
            motivationFactor = 15.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        0 -> 15.5
                        1 -> 18.4
                        else -> 16.8
                    }

                    StatusType.POWER -> 17.5

                    else -> when (rank) {
                        0 -> 15.6
                        else -> 10.1
                    }
                }
            },
        )

        val speed2Power1Guts1Wisdom2V2 = Option(
            speedFactor = 1.6,
            staminaFactor = 1.55,
            powerFactor = 1.3,
            gutsFactor = 1.2,
            wisdomFactor = 0.75,
            skillPtFactor = 0.65,
            hpFactor = 1.35,
            motivationFactor = 15.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        0 -> 14.0
                        else -> 11.5
                    }

                    StatusType.POWER -> 19.0

                    StatusType.GUTS -> 19.5

                    else -> when (rank) {
                        0 -> 12.0
                        else -> 10.5
                    }
                }
            },
            knowledgeSpeedFactor = 8.5,
            knowledgeStaminaFactor = -1.0,
            knowledgePowerFactor = -2.5,
            knowledgeGutsFactor = -4.0,
            knowledgeWisdomFactor = 1.0,
            knowledgeSkillPtFactor = -5.0,
            knowledgeFounderFactor = 4.0,
            knowledgeCountBase = 10.5,
            knowledgeCountFactor = 1.0,
        )

        val speed2Power1Guts1Wisdom2SP = Option(
            speedFactor = 1.15,
            staminaFactor = 1.05,
            powerFactor = 1.0,
            gutsFactor = 1.35,
            wisdomFactor = 0.65,
            skillPtFactor = 1.0,
            hpFactor = 1.2,
            motivationFactor = 15.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        0 -> 18.5
                        else -> 6.5
                    }

                    StatusType.POWER -> 8.5

                    StatusType.GUTS -> 0.5

                    else -> when (rank) {
                        0 -> 8.5
                        else -> 13.0
                    }
                }
            },
            knowledgeSpeedFactor = -0.5,
            knowledgeStaminaFactor = -5.0,
            knowledgePowerFactor = -0.5,
            knowledgeGutsFactor = -9.0,
            knowledgeWisdomFactor = -3.0,
            knowledgeSkillPtFactor = -1.5,
            knowledgeFounderFactor = 19.5,
            knowledgeCountBase = 19.0,
            knowledgeCountFactor = 0.0,
        )

        val speed2Power1Guts1Wisdom1Group1 = Option(
            speedFactor = 1.6,
            staminaFactor = 1.4,
            powerFactor = 1.1,
            gutsFactor = 1.2,
            wisdomFactor = 1.15,
            skillPtFactor = 0.75,
            hpFactor = 1.4,
            motivationFactor = 15.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        0 -> 14.0
                        else -> 11.0
                    }

                    StatusType.POWER -> 16.0

                    StatusType.GUTS -> 11.0

                    StatusType.WISDOM -> 18.5

                    else -> 11.5
                }
            },
            knowledgeSpeedFactor = 8.0,
            knowledgeStaminaFactor = -5.0,
            knowledgePowerFactor = 6.5,
            knowledgeGutsFactor = -3.0,
            knowledgeWisdomFactor = 0.5,
            knowledgeSkillPtFactor = 1.0,
            knowledgeFounderFactor = 10.5,
            knowledgeCountBase = 10.5,
            knowledgeCountFactor = 0.0,
            passionChallengeFactor = 11.0,
        )

        val speed2Stamina1Power1Wisdom1Group1Long = Option(
            speedFactor = 1.8,
            staminaFactor = 1.8,
            powerFactor = 1.0,
            gutsFactor = 1.0,
            wisdomFactor = 1.25,
            skillPtFactor = 0.95,
            hpFactor = 1.5,
            motivationFactor = 15.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        0 -> 15.5
                        else -> 4.5
                    }

                    StatusType.STAMINA -> 13.5

                    StatusType.POWER -> 10.5

                    StatusType.WISDOM -> 17.0

                    else -> 14.5
                }
            },
            knowledgeSpeedFactor = 8.0,
            knowledgeStaminaFactor = -7.5,
            knowledgePowerFactor = 0.0,
            knowledgeGutsFactor = -8.0,
            knowledgeWisdomFactor = -3.0,
            knowledgeSkillPtFactor = 1.0,
            knowledgeFounderFactor = 13.5,
            knowledgeCountBase = 17.0,
            knowledgeCountFactor = 1.0,
            passionChallengeFactor = 16.0,
        )

        val speed2Guts2Wisdom1Group1Middle = Option(
            speedFactor = 1.15,
            staminaFactor = 1.2,
            powerFactor = 1.05,
            gutsFactor = 1.15,
            wisdomFactor = 1.0,
            skillPtFactor = 0.95,
            hpFactor = 1.45,
            motivationFactor = 15.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> when (rank) {
                        0 -> 19.0
                        else -> 17.0
                    }

                    StatusType.GUTS -> when (rank) {
                        0 -> 9.5
                        else -> 19.5
                    }

                    StatusType.WISDOM -> 16.5

                    else -> 18.0
                }
            },
            knowledgeSpeedFactor = -4.0,
            knowledgeStaminaFactor = -7.0,
            knowledgePowerFactor = -4.0,
            knowledgeGutsFactor = -2.5,
            knowledgeWisdomFactor = -1.5,
            knowledgeSkillPtFactor = -0.5,
            knowledgeFounderFactor = 2.5,
            knowledgeCountBase = 19.5,
            knowledgeCountFactor = 2.0,
            passionChallengeFactor = 9.0,
        )

        val speed1Guts2Wisdom2Group1Short = Option(
            speedFactor = 0.8,
            staminaFactor = 0.3,
            powerFactor = 1.15,
            gutsFactor = 1.05,
            wisdomFactor = 0.6,
            skillPtFactor = 0.95,
            hpFactor = 1.1,
            motivationFactor = 15.0,
            relationFactor = { type: StatusType, rank: Int, _: Int ->
                when (type) {
                    StatusType.SPEED -> 12.0

                    StatusType.GUTS -> when (rank) {
                        0 -> 2.0
                        else -> 4.0
                    }

                    StatusType.WISDOM -> when (rank) {
                        0 -> 13.5
                        else -> 5.5
                    }

                    else -> 12.0
                }
            },
            knowledgeSpeedFactor = 4.5,
            knowledgeStaminaFactor = -4.0,
            knowledgePowerFactor = 4.0,
            knowledgeGutsFactor = -1.5,
            knowledgeWisdomFactor = -0.5,
            knowledgeSkillPtFactor = 3.0,
            knowledgeFounderFactor = 9.0,
            knowledgeCountBase = 12.0,
            knowledgeCountFactor = 1.0,
            passionChallengeFactor = 6.5,
        )
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
        val knowledgeSpeedFactor: Double = 0.0,
        val knowledgeStaminaFactor: Double = 0.0,
        val knowledgePowerFactor: Double = 0.0,
        val knowledgeGutsFactor: Double = 0.0,
        val knowledgeWisdomFactor: Double = 0.0,
        val knowledgeSkillPtFactor: Double = 0.0,
        val knowledgeFounderFactor: Double = 10.0,
        val knowledgeCountBase: Double = 10.0,
        val knowledgeCountFactor: Double = 2.0,
        val passionChallengeFactor: Double = 0.0,
    ) : ActionSelectorGenerator {
        override fun generateSelector() = GmActionSelector(this)
    }

    override suspend fun select(state: SimulationState, selection: List<Action>): Action {
        val forceUse = true
        when (val wisdom = state.gmStatus?.waitingWisdom) {
            Founder.Red -> {
                if (state.turn !in 20..23 && (forceUse || state.status.hp < 60 || state.status.motivation < 2)) {
                    return GmActivateWisdom(GmActivateWisdomResult(wisdom))
                }
            }

            Founder.Blue -> {
                if (forceUse || selection.any { it is Training && it.failureRate < 10 && it.member.size >= 3 }) {
                    return GmActivateWisdom(GmActivateWisdomResult(wisdom))
                }
            }

            Founder.Yellow -> {
                if (forceUse || selection.any {
                        it is Training && it.failureRate < 10 && it.member.count { member ->
                            !member.isFriendTraining(it.type)
                        } >= 2
                    }) {
                    return GmActivateWisdom(GmActivateWisdomResult(wisdom))
                }
            }

            null -> {}
        }
        return selection.maxByOrNull { calcScore(state, it) } ?: selection.first()
    }

    private fun calcScore(state: SimulationState, action: Action): Double {
        if (DEBUG) println("${state.turn}: $action")
        val total = action.candidates.sumOf { it.second }.toDouble()
        val score = action.candidates.sumOf {
            if (DEBUG) println("  ${it.second.toDouble() / total * 100}%")
            val result = it.first as? StatusActionResult ?: return@sumOf 0.0
            val statusScore = calcScore(calcExpectedHintStatus(action) + result.status)
            val relationScore = if (result.success) calcRelationScore(state, action) else 0.0
            val knowledgeScore = calcKnowledgeScore(state, result.scenarioActionParam as? GmActionParam)
            val passionChallengeScore = if (result.success) calcPassionChallengeScore(state, action) else 0.0
            (statusScore + relationScore + knowledgeScore + passionChallengeScore) * it.second / total
        }

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

    private fun calcScore(status: ExpectedStatus): Double {
        val score = status.speed * option.speedFactor +
                status.stamina * option.staminaFactor +
                status.power * option.powerFactor +
                status.guts * option.gutsFactor +
                status.wisdom * option.wisdomFactor +
                status.skillPt * option.skillPtFactor +
                status.hp * option.hpFactor +
                status.motivation * option.motivationFactor
        if (DEBUG) println("  $score $status")
        return score
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

    private fun calcKnowledgeScore(state: SimulationState, param: GmActionParam?): Double {
        if (param == null) return 0.0
        val gmStatus = state.gmStatus ?: return 0.0
        if (gmStatus.knowledgeFragmentCount == 8) return 0.0
        val typeFactor = when (param.knowledgeType) {
            StatusType.SPEED -> option.knowledgeSpeedFactor
            StatusType.STAMINA -> option.knowledgeStaminaFactor
            StatusType.POWER -> option.knowledgePowerFactor
            StatusType.GUTS -> option.knowledgeGutsFactor
            StatusType.WISDOM -> option.knowledgeWisdomFactor
            else -> option.knowledgeSkillPtFactor
        }
        val founderEffect = when (gmStatus.knowledgeFragmentCount) {
            0 -> 1.0
            3 -> if (param.knowledgeCount == 2) 1.0 else 0.0
            4 -> 1.0
            else -> 0.0
        }
        val knowledgeEventRate =
            if (gmStatus.knowledgeFragmentCount + param.knowledgeCount >= 8) 0.0 else param.knowledgeEventRate
        val founderFactor = targetFounderSetting(
            gmStatus.wisdomLevel[Founder.Red]!!,
            gmStatus.wisdomLevel[Founder.Blue]!!,
            gmStatus.wisdomLevel[Founder.Yellow]!!,
        )[param.knowledgeFounder.ordinal] * founderEffect * option.knowledgeFounderFactor
        val score = if (param.knowledgeCount == 1) typeFactor + founderFactor else {
            option.knowledgeCountBase + (typeFactor + founderFactor) * option.knowledgeCountFactor
        } + knowledgeEventRate * option.knowledgeCountBase
        if (DEBUG) println("  knowledge $score $param")
        return score
    }

    private fun calcPassionChallengeScore(state: SimulationState, action: Action): Double {
        return when {
            action !is Training -> 0.0
            action.support.any { it.supportState != null && it.supportState.outingEnabled && !it.supportState.passion } -> option.passionChallengeFactor
            else -> 0.0
        }

    }

    override fun toString(): String {
        return "GmActionSelector $option"
    }
}