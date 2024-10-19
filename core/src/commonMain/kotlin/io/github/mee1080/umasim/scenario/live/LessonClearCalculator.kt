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
package io.github.mee1080.umasim.scenario.live

class LessonClearCalculator(
    private val categoryRate: Array<List<Pair<TechniqueLessonCategory, Int>>>,
    private val selections: Map<TechniqueLessonCategory, TechniqueLessonSet>,
    private val evaluate: (lesson: TechniqueLesson, restPerformance: Performance) -> Int,
) {

    private fun <T> calcRate(value: T, values: List<Pair<T, Int>>): Double {
        val target = values.firstOrNull { it.first == value } ?: return 0.0
        return target.second.toDouble() / values.sumOf { it.second }
    }

    private fun select(param: Performance, selections: List<TechniqueLesson>): Pair<TechniqueLesson, Performance>? {
        return selections.mapNotNull {
            val rest = param - it.cost
            if (rest.valid) it to rest else null
        }.maxByOrNull {
            evaluate(it.first, it.second)
        }
    }

    private fun calcSingle(
        param: Performance,
        detail: MutableList<Triple<List<TechniqueLesson>, Pair<TechniqueLesson, Performance>?, Double>>? = null,
    ): Map<Performance, Double> {
        val result = mutableMapOf<Performance, Double>()
        val categoryRate1 = categoryRate[0]
        for (category1 in TechniqueLessonCategory.entries) {
            val selections1 = selections[category1] ?: continue
            val rate1 = calcRate(category1, categoryRate1)
            if (rate1 == 0.0) continue
            val categoryRate2 = categoryRate[1].filter {
                !it.first.single || it.first != category1
            }
            for (selection1 in selections1.rateList) {
                for (category2 in TechniqueLessonCategory.entries) {
                    if (category2.single && category2 == category1) continue
                    val selections2 = selections[category2]?.filter { it !== selection1.first } ?: continue
                    val rate2 = calcRate(category2, categoryRate2)
                    if (rate2 == 0.0) continue
                    val categoryRate3 = categoryRate[2].filter {
                        !it.first.single || (it.first != category1 && it.first != category2)
                    }
                    for (selection2 in selections2.rateList) {
                        for (category3 in TechniqueLessonCategory.entries) {
                            if (category3.single && (category3 == category1 || category3 == category2)) continue
                            val selections3 =
                                selections[category3]?.filter { it !== selection1.first && it !== selection2.first }
                                    ?: continue
                            val rate3 = calcRate(category3, categoryRate3)
                            if (rate3 == 0.0) continue
                            for (selection3 in selections3.rateList) {
                                val selectionList = listOf(selection1.first, selection2.first, selection3.first)
                                val selected = select(param, selectionList)
                                if (selected != null) {
                                    result[selected.second] =
                                        result.getOrElse(selected.second) { 0.0 } + rate1 * selection1.second * rate2 * selection2.second * rate3 * selection3.second
                                }
                                detail?.add(
                                    Triple(
                                        selectionList,
                                        selected,
                                        rate1 * rate2 * selection2.second * rate3 * selection3.second
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        return result
    }

    fun calc(
        param: Performance,
        stepCount: Int,
        filter: Double,
    ): Array<Double> {
        val results = Array(stepCount) { 0.0 }
        var targets = mapOf(param to 1.0)
        repeat(stepCount) { step ->
            val stepResult = mutableMapOf<Performance, Double>()
            targets.forEach { target ->
                calcSingle(target.key).forEach { entry ->
                    val rate = entry.value * target.value
                    stepResult[entry.key] = stepResult.getOrElse(entry.key) { 0.0 } + rate
                    results[step] += rate
                }
            }
            targets = if (filter == 0.0) stepResult else {
                val filtered = stepResult.filterValues { it > filter }
                val filteredFactor = filtered.values.sum() / stepResult.values.sum()
                filtered.mapValues { it.value / filteredFactor }
            }
        }
        return results
    }
}