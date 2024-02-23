/*
 * Copyright 2022 mee1080
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
package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.util.applyIf
import kotlin.math.min
import kotlin.math.pow

class ExpectedCalculator(
    private val info: ExpectedCalcInfo,
    private val targetTypes: List<StatusType> = trainingType.toList(),
    private val evaluate: (status: Status, needRelationCount: Int) -> Int = { status, needRelationCount ->
        status.totalPlusSkillPtPerformanceX2 + status.hp + needRelationCount * 1000
    }
) {

    class ExpectedCalcInfo(
        chara: Chara,
        private val training: Map<StatusType, TrainingBase>,
        motivation: Int,
        val member: List<MemberState>,
        scenario: Scenario,
        fanCount: Int,
        currentStatus: Status,
        totalRelation: Int,
        speedSkillCount: Int,
        healSkillCount: Int,
        accelSkillCount: Int,
        totalTrainingLevel: Int,
        val liveStatus: TrainingLiveStatus?,
        gmStatus: GmStatus?,
        lArcStatus: LArcStatus?,
        uafStatus: UafStatus?,
    ) {

        internal val baseCalcInfo = Calculator.CalcInfo(
            chara = chara,
            training = training[StatusType.SPEED]!!,
            motivation = motivation,
            member = emptyList(),
            scenario = scenario,
            supportTypeCount = member.distinctBy { it.card.type }.size,
            fanCount = fanCount,
            currentStatus = currentStatus,
            totalRelation = totalRelation,
            speedSkillCount = speedSkillCount,
            healSkillCount = healSkillCount,
            accelSkillCount = accelSkillCount,
            totalTrainingLevel = totalTrainingLevel,
            liveStatus = liveStatus,
            gmStatus = gmStatus,
            lArcStatus = lArcStatus,
            uafStatus = uafStatus,
        )

        fun toCalcInfo(type: StatusType, factors: List<Wrapper>, count: Int): Calculator.CalcInfo {
            return baseCalcInfo.copy(
                training = training[type]!!,
                member = factors.map { it.factor },
            ).setTeamMember(count - factors.size)
        }
    }

    inner class Wrapper(val index: Int, val factor: MemberState) {
        override fun equals(other: Any?): Boolean {
            return (other as? Wrapper)?.index == index
        }

        override fun hashCode(): Int {
            return index.hashCode()
        }

        private val cardType = factor.card.type

        private val positionSelection =
            Calculator.calcCardPositionSelection(info.baseCalcInfo, factor, (info.liveStatus?.specialityRateUp ?: 0))

        private val specialityRate = calcRate(cardType, *positionSelection)

        private val restRate = calcRate(StatusType.NONE, *positionSelection)

        private val notSpecialityRate = (1.0 - specialityRate - restRate) / 4.0

        fun rate(type: StatusType) = when (type) {
            StatusType.NONE -> restRate
            cardType -> specialityRate
            else -> notSpecialityRate
        }

        val needRelation = factor.relation < 80

        override fun toString(): String {
            return factor.charaName
        }
    }

    private val factors = info.member.filter { !it.guest }.mapIndexed { index, factor -> Wrapper(index, factor) }

    private val otherCount = info.member.count { it.guest }

    private val cache = mutableMapOf<Triple<StatusType, List<Wrapper>, Int>, Status>()

    private val otherPositionRate = 0.18

    private fun calc(type: StatusType, factors: List<Wrapper>, count: Int): Status {
        return cache.getOrPut(Triple(type, factors, count)) {
            val info = info.toCalcInfo(type, factors, count)
            Calculator.calcTrainingSuccessStatus(info)
        }
    }

    private val factorial = Array(otherCount + 1) { n -> if (n == 0) 1.0 else (1..n).fold(1.0) { acc, k -> acc * k } }

    private val calculatedOtherRate = Array(targetTypes.size) { stage ->
        val rate = otherPositionRate / (1.0 - stage * otherPositionRate)
        Array(otherCount + 1) { total ->
            Array(min(6, total + 1)) { count ->
                rate.pow(count) * (1.0 - rate).pow(total - count) * factorial[total] / factorial[count] / factorial[total - count]
            }
        }
    }

    private val calculatedOtherRates = Array(targetTypes.size) { stage ->
        Array(otherCount + 1) { otherCount ->
            Array(min(6, otherCount + 1)) { otherPatternCount ->
                var restRate = 1.0
                Array(otherPatternCount + 1) { count ->
                    if (count == otherPatternCount) restRate else {
                        val otherRate = calculatedOtherRate[stage][otherCount][count]
                        restRate -= otherRate
                        otherRate
                    }
                }
            }
        }
    }

    private val calculatedPositions = Array(2.0.pow(factors.size).toInt()) { positionValue ->
        Array(factors.size) { positionValue and (1 shl it) > 0 }
    }

    private fun calcPosition(factors: List<Wrapper>, positionValue: Int): Pair<List<Wrapper>, List<Wrapper>> {
        val positioned = mutableListOf<Wrapper>()
        val rest = mutableListOf<Wrapper>()
        val positions = calculatedPositions[positionValue]
        factors.forEachIndexed { index, factor ->
            if (positions[index]) {
                positioned += factor
            } else {
                rest += factor
            }
        }
        return positioned to rest
    }

    private val calculatedNotJoinRate = factors.associate { factor ->
        factor.index to (trainingType.toList() - targetTypes.toSet() + StatusType.NONE).sumOf { type ->
            factor.rate(type)
        }
    }

    private fun calcRecursive(
        out: MutableList<Triple<List<Pair<StatusType, List<Wrapper>>>, Pair<Int, Status>, Double>>?,
        results: MutableMap<StatusType, Triple<List<Wrapper>, Status, Int>>?,
        types: List<StatusType>,
        targetFactors: List<Wrapper>,
        targetOtherCount: Int,
        stage: Int,
        currentMax: Int,
        currentMaxStatus: Status,
        currentRate: Double,
        typeRate: MutableMap<StatusType, Double>?,
        currentMaxType: List<StatusType>?,
    ): Pair<ExpectedStatus, Double> {
        var expected = ExpectedStatus().applyIf(info.liveStatus != null) { enablePerformance() }
        var totalRate = 0.0
        val type = types[stage]
        val maxPositionValue = 2.0.pow(targetFactors.size).toInt() - 1
        for (positionValue in maxPositionValue downTo 0) {
            val (positioned, rest) = calcPosition(targetFactors, positionValue)
            if (positioned.size > 5) continue
            val factorRate = positioned.fold(currentRate) { acc, factor ->
                acc * factor.rate(type)
            }
            val restPositionCount = 5 - positioned.size
            val otherPatternCount = min(targetOtherCount, restPositionCount)
            val otherRates = calculatedOtherRates[stage][targetOtherCount][otherPatternCount]
            var skip = false
            for (other in otherPatternCount downTo 0) {
                val otherRate = otherRates[other]
                val rate = factorRate * otherRate

                var max = currentMax
                var maxStatus = currentMaxStatus
                var maxType = currentMaxType
                if (!skip) {
                    val status = calc(type, positioned, positioned.size + other)
                    val needRelationCount = positioned.count { it.needRelation }
                    val result = evaluate(status, needRelationCount)
                    results?.put(type, Triple(positioned, status, result))
                    if (result > currentMax) {
                        max = result
                        maxStatus = status
                        if (typeRate != null) {
                            maxType = listOf(type)
                        }
                    } else {
                        if (currentMaxType != null && result == currentMax) {
                            maxType = currentMaxType + type
                        }
                        skip = true
                    }
                }

                if (stage == types.lastIndex) {
                    val finalRate = rest.fold(rate) { acc, factor ->
                        acc * calculatedNotJoinRate[factor.index]!!
                    }
                    if (out != null && results != null) {
                        val maxTargets = results
                            .filterValues { it.third == max }
                            .map { it.key to it.value.first }
                            .sortedBy { it.first.ordinal }
                        out += Triple(maxTargets, max to maxStatus, finalRate)
                    }
                    if (typeRate != null && maxType != null) {
                        val singleTypeRate = finalRate / maxType.size
                        maxType.forEach {
                            typeRate[it] = typeRate[it]!! + singleTypeRate
                        }
                    }
                    expected = expected.add(finalRate, maxStatus)
                    totalRate += finalRate
                } else {
                    val ret = calcRecursive(
                        out, results, types, rest, targetOtherCount - other,
                        stage + 1, max, maxStatus, rate, typeRate, maxType,
                    )
                    expected += ret.first
                    totalRate += ret.second
                }
            }
        }
        return expected to totalRate
    }

    fun calc(typeRate: MutableMap<StatusType, Double>? = null): ExpectedStatus {
        val result =
            if (otherCount > -1) null else mutableListOf<Triple<List<Pair<StatusType, List<Wrapper>>>, Pair<Int, Status>, Double>>()
        val results =
            if (otherCount > -1) null else trainingType.associateWith { Triple(emptyList<Wrapper>(), Status(), 0) }
                .toMutableMap()
        val maxByType = targetTypes.associateWith { type ->
            calc(type, factors, 6)
        }
        val types = maxByType.toList().sortedByDescending {
            evaluate(it.second, factors.count { factor -> factor.needRelation })
        }.map { it.first }

        val ret = calcRecursive(
            result, results, types, factors, otherCount,
            0, 0, Status(), 1.0, typeRate, null,
        )
//        result
//            ?.sortedBy { it.second.first * 100 + it.first.size * 10 + (it.first.firstOrNull()?.first?.ordinal ?: 6) }
//            ?.forEach { println(it) }
//        factors.forEach { factor ->
//            println((trainingType + StatusType.NONE).joinToString(",") { "${it.displayName}=${factor.rate(it)}" })
//        }

        return ret.first / ret.second
    }

}