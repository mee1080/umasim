/*
 * Copyright 2023 mee1080
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
/*
 * This file was ported from uma-clock-emu by Romulus Urakagi Tsai(@urakagi)
 * https://github.com/urakagi/uma-clock-emu
 */
package io.github.mee1080.umasim.race

import io.github.mee1080.umasim.race.data.SkillEffect
import kotlin.math.log10
import kotlin.random.Random

class RaceCalculator(private val setting: RaceSetting) {

    fun simulate() {
        var state = initializeState()

        // TODO progressRace
    }

    private fun initializeState(): RaceState {
        val state = RaceState(setting, RaceSimulationState())
        var simulationState = state.invokeSkills()
        if (!state.setting.fixRandom) {
            simulationState = simulationState.copy(
                startDelay = Random.nextFloat() * 0.1f
            )
        }
        // TODO triggerStartSkills
        if (Random.nextFloat() * 100f < state.setting.temptationRate) {
            simulationState = simulationState.copy(
                temptationSection = 1 + Random.nextInt(8)
            )
        }
        simulationState = simulationState.copy(
            isStartDash = true,
            delayTime = simulationState.startDelay,
            sp = state.setting.spMax,
            sectionTargetSpeedRandoms = state.initSectionTargetSpeedRandoms(),
        )
        return state.update(simulationState)
    }

    private fun RaceState.invokeSkills(): RaceSimulationState {
        val invokedSkills = mutableListOf<SkillEffect>()
        setting.hasSkills.forEach { skill ->
            val invokeRate = if (setting.skillActivateAdjustment > 0) {
                100f
            } else when (skill.displayType) {
                "fatigue", "decel" -> 90f
                "passive", "unique" -> 100f
                else -> maxOf(100f - 9000f / setting.umaStatus.wisdom, 20f)
            }
            val invokes = skill.invokes ?: listOf(skill)
            for (invoke in invokes) {
                // TODO init, initSkillConditions
                if (Random.nextFloat() * 100 < invokeRate) {
                    invokedSkills += invoke
                }
            }
        }
        return simulation.copy(
            invokedSkills = invokedSkills,
        )
    }

    private fun RaceState.initSectionTargetSpeedRandoms(): List<Float> {
        return (0..24).map {
            val max = (setting.modifiedWisdom / 5500.0f) *
                    log10(setting.modifiedWisdom * 0.1f) * 0.01f
            if (setting.fixRandom) {
                max - 0.00325f
            } else {
                max + Random.nextFloat() * -0.0065f
            }
        }
    }
}