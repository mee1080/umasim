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
package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.optimize.Optimizer
import io.github.mee1080.umasim.simulation2.Evaluator
import io.github.mee1080.umasim.simulation2.Simulator
import kotlin.math.roundToInt


fun generateOptions2(
    base: FactorBasedActionSelector2.Option = FactorBasedActionSelector2.Option(),
    speed: DoubleArray = doubleArrayOf(0.6, 0.8, 1.0, 1.2),
    stamina: DoubleArray = doubleArrayOf(0.6, 0.8, 1.0, 1.2),
    power: DoubleArray = doubleArrayOf(0.6, 0.8, 1.0, 1.2),
    guts: DoubleArray = doubleArrayOf(0.2, 0.4),
    wisdom: DoubleArray = doubleArrayOf(0.6, 0.8, 1.0, 1.2),
    skillPt: DoubleArray = doubleArrayOf(0.2, 0.4),
    hp: DoubleArray = doubleArrayOf(0.4, 0.6, 0.8),
    motivation: DoubleArray = doubleArrayOf(25.0),
): Array<FactorBasedActionSelector2.Option> {
    val list = mutableListOf<FactorBasedActionSelector2.Option>()
    var option = base
    val conv = { v: Double -> (v * 100).roundToInt() / 100.0 }
    speed.map { sp ->
        option = option.copy(speedFactor = conv(sp))
        stamina.map { st ->
            option = option.copy(staminaFactor = conv(st))
            power.map { pw ->
                option = option.copy(powerFactor = conv(pw))
                guts.map { gt ->
                    option = option.copy(gutsFactor = conv(gt))
                    wisdom.map { ws ->
                        option = option.copy(wisdomFactor = conv(ws))
                        skillPt.map { sk ->
                            option = option.copy(skillPtFactor = conv(sk))
                            hp.map { hp ->
                                option = option.copy(hpFactor = conv(hp))
                                motivation.map { mt ->
                                    option = option.copy(motivationFactor = conv(mt))
                                    list.add(option)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return list.toTypedArray()
}

private val defaultEvaluateSetting = mapOf(
    StatusType.SPEED to (1.0 to 1100),
    StatusType.STAMINA to (1.0 to 1100),
    StatusType.POWER to (1.0 to 1100),
    StatusType.GUTS to (1.0 to 600),
    StatusType.WISDOM to (0.8 to 1000),
    StatusType.SKILL to (0.4 to Int.MAX_VALUE),
)

fun optimizeAI(
    scenario: Scenario,
    chara: Chara,
    support: List<SupportCard>,
    evaluateSetting: Map<StatusType, Pair<Double, Int>> = defaultEvaluateSetting,
) {
    println("optimize")
    println(chara)
    support.forEach { println(it.name) }
    val option = Simulator.Option(checkGoalRace = true)
    val selectors = generateOptions2().map { it.generateSelector() }
    val result = Optimizer(scenario, chara, support, option, 78, selectors) {
        it.upperSum(0.2, evaluateSetting)
    }.optimize()
    result.forEachIndexed { index, target ->
        println("$index,\"${target.first.option}\",,${Evaluator(target.second).toSummaryString()}")
    }
}