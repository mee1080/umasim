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

import io.github.mee1080.umasim.ai.FactorBasedActionSelector
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.simulation.Evaluator
import io.github.mee1080.umasim.simulation.Runner
import io.github.mee1080.umasim.simulation.Simulator
import io.github.mee1080.umasim.simulation.Summary
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt


fun generateOptions(
    base: FactorBasedActionSelector.Option = FactorBasedActionSelector.Option(),
    step: Double = 0.1,
    speed: ClosedRange<Double> = 1.0..1.0,
    stamina: ClosedRange<Double> = 1.0..1.0,
    power: ClosedRange<Double> = 1.0..1.0,
    guts: ClosedRange<Double> = 0.0..0.0,
    wisdom: ClosedRange<Double> = 0.0..0.0,
    skillPt: ClosedRange<Double> = 0.4..0.4,
    hp: ClosedRange<Double> = 0.5..0.5,
    motivation: ClosedRange<Double> = 15.0..15.0,
): Array<FactorBasedActionSelector.Option> {
    val list = mutableListOf<FactorBasedActionSelector.Option>()
    var option = base
    val conv = { v: Double -> (v * 100).roundToInt() / 100.0 }
    (speed step step).map { sp ->
        option = option.copy(speedFactor = conv(sp))
        (stamina step step).map { st ->
            option = option.copy(staminaFactor = conv(st))
            (power step step).map { pw ->
                option = option.copy(powerFactor = conv(pw))
                (guts step step).map { gt ->
                    option = option.copy(gutsFactor = conv(gt))
                    (wisdom step step).map { ws ->
                        option = option.copy(wisdomFactor = conv(ws))
                        (skillPt step step).map { sk ->
                            option = option.copy(skillPtFactor = conv(sk))
                            (hp step step).map { hp ->
                                option = option.copy(hpFactor = conv(hp))
                                (motivation step step).map { mt ->
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

infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    var value = start - step
    return generateSequence {
        value += step
        if (value > endInclusive + step / 1000.0) null else value
    }.asIterable()
}

fun optimizeAI(
    chara: Chara,
    support: List<SupportCard>,
    turn: Int = 55,
    testCount: Int = 100,
    vararg options: FactorBasedActionSelector.Option
) {
    println(chara)
    support.forEach { println(it.name) }
    runBlocking {
        options.mapIndexed { index, option ->
            launch(context) {
                val summary = mutableListOf<Summary>()
                repeat(testCount) {
                    val simulator = Simulator(chara, support, Store.getTrainingList(scenario)).apply {
                        status = status.copy(motivation = 2)
                    }
                    summary.add(
                        Runner.simulate(
                            turn,
                            simulator,
                            FactorBasedActionSelector(option)
                        )
                    )
                }
                val optionStr =
                    "${option.speedFactor},${option.staminaFactor},${option.powerFactor},${option.gutsFactor},${option.wisdomFactor},${option.skillPtFactor},${option.hpFactor},${option.motivationFactor}"
                println("$index,\"$optionStr\",,${Evaluator(summary).toSummaryString()}")
            }
        }.forEach {
            it.join()
        }
    }
}