package io.github.mee1080.umasim.scenario.uaf

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.calcRate
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.MemberState

data class ColorFactor(
    val type: Int,
    val link: Boolean,
    val friend: Boolean,
    private val mainRate: Double,
    private val subRate: Double,
    private val restRate: Double = 1 - mainRate - subRate * 4,
) {

    val rate = DoubleArray(6) {
        when (it) {
            5 -> restRate
            type -> mainRate
            else -> subRate
        }
    }
}

object UafAthleticsLevelCalculator {

    fun calc(
        info: Calculator.CalcInfo,
        bonus: Boolean,
    ): DoubleArray {
        val factors = info.member.map { member ->
            val positions = Calculator.calcCardPositionSelection(info, member, 0, 0)
            val restRate = calcRate(StatusType.NONE, *positions)
            val link = info.scenario.scenarioLink.contains(member.charaName)
            val typeInt = member.card.type.ordinal
            if (typeInt >= 5) {
                val joinRate = (1.0 - restRate) / 5.0
                ColorFactor(5, link, false, joinRate, joinRate, restRate)
            } else {
                val mainRate = calcRate(member.card.type, *positions)
                val subRate = (1.0 - mainRate - restRate) / 4.0
                ColorFactor(typeInt, link, member.friendTrainingEnabled, mainRate, subRate, restRate)
            }
        } + if (info.member.any { it.charaName == "都留岐涼花" }) emptyList() else listOf(
            ColorFactor(5, link = false, friend = false, 1.0 / 6.0, 1.0 / 6.0),
        )
        return calc(factors, bonus)
    }

    fun calc(
        factors: List<ColorFactor>,
        bonus: Boolean,
    ): DoubleArray {
        val positionPatterns = createPositionPatterns(factors, if (bonus) 3 else 0)
        return calcExpected(colorPatterns, positionPatterns)
    }

    fun calcLevelUp(
        members: List<MemberState>,
        bonus: Boolean,
    ): Map<StatusType, Int> {
        val factors = mutableListOf<ColorFactor>()
        val positions = IntArray(members.size) { 0 }
        members.forEachIndexed { index, member ->
            val link = member.isScenarioLink
            val typeInt = member.card.type.ordinal
            factors.add(ColorFactor(typeInt, link, member.friendTrainingEnabled, 0.0, 0.0, 0.0))
            positions[index] = member.position.ordinal
        }
        val list = calcLevelUp(factors, positions, if (bonus) 3 else 0)!!
        return list.mapIndexed { index, i -> StatusType.entries[index] to i }.associate { it }
    }

    private class CalcEntry(
        val data: List<Int>,
        val rate: Double,
    )

    private val colorPatterns by lazy {
        buildList {
            for (a in 0..1) {
                var has1 = a == 1
                for (b in 0..if (has1) 2 else 1) {
                    has1 = has1 || b == 1
                    for (c in 0..if (has1) 2 else 1) {
                        has1 = has1 || c == 1
                        for (d in 0..if (has1) 2 else 1) {
                            add(CalcEntry(listOf(0, a, b, c, d), (if (has1 || d == 1) 2 else 1) / 81.0))
                        }
                    }
                }
            }
        }
    }

    private fun createPositionPatterns(factors: List<ColorFactor>, bonusValue: Int): List<CalcEntry> {
        val list = buildList {
            positionPatternsRecursive(factors, bonusValue, 0, IntArray(factors.size) { 0 }, 1.0)
        }
        val total = list.sumOf { it.rate }
        return list.map { CalcEntry(it.data, it.rate / total) }
    }

    private fun MutableList<CalcEntry>.positionPatternsRecursive(
        factors: List<ColorFactor>,
        bonusValue: Int,
        step: Int,
        currentPositions: IntArray,
        currentRate: Double,
    ) {
        val factor = factors.getOrNull(step)
        if (factor == null) {
            calcLevelUp(factors, currentPositions, bonusValue)?.let {
                add(CalcEntry(it, currentRate))
            }
        } else {
            for (i in 0..5) {
                currentPositions[step] = i
                positionPatternsRecursive(factors, bonusValue, step + 1, currentPositions, currentRate * factor.rate[i])
            }
        }
    }

    private fun calcLevelUp(
        factors: List<ColorFactor>,
        positions: IntArray,
        bonusValue: Int,
    ): List<Int>? {
        val count = IntArray(5) { 0 }
        val link = IntArray(5) { 0 }
        val friend = IntArray(5) { 1 }
        positions.forEachIndexed { index, type ->
            if (type < 5) {
                val factor = factors[index]
                count[type]++
                if (factor.link) link[type]++
                if (factor.friend && type == factor.type) friend[type] = 2
            }
        }
        if (count.any { it > 5 }) return null
        return List(5) { type ->
            (3 + (if (count[type] == 0) 0 else (count[type] / 2 + 1))) * friend[type] + link[type] + bonusValue
        }
    }

    private fun calcExpected(
        colorPattern: List<CalcEntry>,
        levelUpPattern: List<CalcEntry>,
    ): DoubleArray {
        val result = DoubleArray(67) { 0.0 }
        colorPattern.forEach { color ->
            levelUpPattern.forEach { levelUp ->
                val value = calc(color.data, levelUp.data).max()
                result[value] += levelUp.rate * color.rate
            }
        }
        return result
    }

    private fun calc(
        color: List<Int>,
        levelUp: List<Int>,
    ): IntArray {
        val values = IntArray(3) { 0 }
        levelUp.forEachIndexed { type, value ->
            values[color[type]] += value
        }
        return values
    }
}