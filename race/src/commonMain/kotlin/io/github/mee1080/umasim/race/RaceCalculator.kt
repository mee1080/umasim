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

import io.github.mee1080.umasim.race.data.*
import kotlin.math.*
import kotlin.random.Random

class RaceCalculator(private val setting: RaceSetting) {

    fun simulate(): Pair<RaceSimulationResult, List<RaceFrame>> {
        return initializeState().progressRace()
    }

    private fun initializeState(): RaceState {
        val state = RaceState(setting, RaceSimulationState())
        state.invokeSkills()
        val simulation = state.simulation
        if (!state.setting.fixRandom) {
            simulation.startDelay = Random.nextFloat() * 0.1f
        }
        // TODO triggerStartSkills

        // initTemptation
        if (Random.nextFloat() * 100f < state.setting.temptationRate) {
            simulation.temptationSection = 1 + Random.nextInt(8)
        }
        simulation.isStartDash = true
        simulation.delayTime = simulation.startDelay
        simulation.sp = state.setting.spMax
        simulation.sectionTargetSpeedRandoms = state.initSectionTargetSpeedRandoms()
        return state
    }

    private fun RaceState.invokeSkills() {
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
        simulation.invokedSkills = invokedSkills
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

    private fun RaceState.progressRace(): Pair<RaceSimulationResult, List<RaceFrame>> {
        val frames = mutableListOf<RaceFrame>()
        while (simulation.position < setting.courseLength) {
            if (simulation.frameElapsed > 5000) {
                break
            }
            val startPosition = simulation.position
            val startSp = simulation.sp
            val startPhase = currentPhase
            var frame = RaceFrame(
                speed = simulation.currentSpeed,
                sp = simulation.sp,
                startPosition = startPosition,
            )

            // 下り坂モードに入るか・終わるかどうかの判定
            if (isInSlopeDown() && !setting.fixRandom) {
                // 1秒置きなので、このフレームは整数秒を含むかどうかのチェック
                if (floor(simulation.frameElapsed * frameLength).toInt() != floor(
                        (simulation.frameElapsed + 1) * frameLength
                    ).toInt()
                ) {
                    if (simulation.downSlopeModeStart == null) {
                        if (Random.nextFloat() < setting.modifiedWisdom * 0.0004) {
                            simulation.downSlopeModeStart = simulation.frameElapsed
                        }
                    } else {
                        if (Random.nextFloat() < 0.2f) {
                            simulation.downSlopeModeStart = null
                        }
                    }
                }
            } else {
                simulation.downSlopeModeStart = null
            }

            // 掛かり処理
            if (simulation.isInTemptation) {
                // 掛かり終了判定
                val temptationDuration =
                    (simulation.frameElapsed - simulation.temptationModeStart!!) * frameLength
                val prevTemptationDuration =
                    (simulation.frameElapsed - 1 - simulation.temptationModeStart!!) * frameLength
                repeat(3) {
                    val j = it * 3 + 3
                    if (prevTemptationDuration < j && temptationDuration >= j) {
                        if (Random.nextFloat() < 0.55f) {
                            simulation.temptationModeEnd = simulation.frameElapsed
                        }
                    }
                }
                if (temptationDuration >= 12) {
                    simulation.temptationModeEnd = simulation.frameElapsed
                }
            }
            // 掛かり開始
            if (simulation.temptationSection > 0 && currentSection == simulation.temptationSection) {
                simulation.temptationModeStart = simulation.frameElapsed
                simulation.temptationSection = -1
            }

            move(frameLength)
            frame = frame.copy(
                movement = simulation.position - startPosition,
                consume = simulation.sp - startSp,
                targetSpeed = targetSpeed,
                acceleration = acceleration
            )
            simulation.frameElapsed++

            // 終盤入り・ラストスパート計算
            if (startPhase == 1 && currentPhase == 2) {
                simulation.spurtParameters = calcSpurtParameter()
            }

            if (simulation.position >= setting.courseLength) {
                break
            }
            // Calculate target speed of next frame and do heal/fatigue
            val skillTriggered = checkSkillTrigger(startPosition)
            val spurtParameters = simulation.spurtParameters
            val spurting =
                spurtParameters != null && simulation.position + spurtParameters.distance >= setting.courseLength
            frame = frame.copy(
                skills = skillTriggered,
                spurting = spurting,
            )

            // Remove overtime skills
            simulation.operatingSkills = simulation.operatingSkills.filterNot { operatingSkill ->
                val duration = operatingSkill.duration ?: 0f
                (simulation.frameElapsed - operatingSkill.startFrame) * frameLength > duration * setting.timeCoef
            }

            frames += frame
        }

        return goal() to frames
    }

    private fun RaceState.move(elapsedTime: Float) {
        if (simulation.delayTime > 0f) {
            simulation.delayTime -= elapsedTime
        }
        if (simulation.delayTime <= 0f) {
            var timeAfterDelay = elapsedTime
            if (simulation.delayTime < 0f) {
                timeAfterDelay = abs(simulation.delayTime)
                simulation.delayTime = 0f
            }

            updateSelfSpeed(elapsedTime /* NOT timeAfterDelay!! */)
            val actualSpeed = simulation.currentSpeed

            // 移動距離及び耐力消耗を算出
            simulation.position += actualSpeed * timeAfterDelay
            val baseSpeed = if (simulation.isStartDash) simulation.currentSpeed else setting.baseSpeed
            var consume = consumePerSecond(
                baseSpeed,
                simulation.currentSpeed,
                currentPhase
            ) * elapsedTime
            if (simulation.downSlopeModeStart != null) {
                consume *= 0.4f
            }
            if (simulation.isInTemptation) {
                simulation.temptationWaste += consume * 0.6f
                consume *= 1.6f
            }
            simulation.sp -= consume

            this.updateStartDash()
        }
    }

    private fun RaceState.updateSelfSpeed(elapsedTime: Float) {
        var newSpeed = if (simulation.currentSpeed < targetSpeed) {
            min(simulation.currentSpeed + elapsedTime * acceleration, targetSpeed)
        } else {
            max(simulation.currentSpeed + elapsedTime * deceleration, targetSpeed)
        }
        if (simulation.isStartDash && newSpeed > setting.v0) {
            newSpeed = setting.v0
        }
        newSpeed = max(min(newSpeed, maxSpeed), vMin)
        newSpeed -= simulation.speedDebuff
        val speedModification = simulation.operatingSkills.sumOf {
            // 減速スキルの現在速度低下分
            it.data.speed ?: 0f
        }
        simulation.speedDebuff = speedModification
        newSpeed += speedModification
        simulation.currentSpeed = newSpeed
    }

    private fun RaceState.updateStartDash() {
        if (simulation.isStartDash && simulation.currentSpeed >= setting.v0) {
            simulation.isStartDash = false
        }
    }

    private fun RaceState.calcSpurtParameter(): SpurtParameters {
        val maxDistance = setting.trackDetail.distance - simulation.position
        val spurtDistance = calcSpurtDistance(setting.maxSpurtSpeed)
        val totalConsume = calcRequiredSp(setting.maxSpurtSpeed)
        if (spurtDistance >= maxDistance) {
            if (simulation.position <= (setting.courseLength * 2.0f) / 3 + 5) {
                if (simulation.spurtParameters == null) {
                    simulation.maxSpurt = true
                }
            }
            return SpurtParameters(
                distance = maxDistance,
                speed = setting.maxSpurtSpeed,
                spDiff = simulation.spurtParameters?.spDiff ?: (simulation.sp - totalConsume),
            )
        }
        // SPが足りない場合の処理
        val totalConsumeV3 = calcRequiredSp(setting.v3)
        val excessSp = simulation.sp - totalConsumeV3
        if (excessSp < 0) {
            return SpurtParameters(
                distance = 0f,
                speed = setting.v3,
                spDiff = simulation.spurtParameters?.spDiff ?: (simulation.sp - totalConsume),
            )
        }
        val candidates = ((setting.v3 * 10).toInt()..(setting.maxSpurtSpeed * 10 - 1).toInt()).map {
            val v = it * 0.1f
            val distanceV = min(maxDistance, calcSpurtDistance(v))
            SpurtParameters(
                distance = distanceV,
                speed = v,
                spDiff = simulation.spurtParameters?.spDiff ?: (simulation.sp - totalConsume),
                time = distanceV / v + (maxDistance - distanceV) / setting.v3,
            )
        }.sortedBy { it.time }
        for (candidate in candidates) {
            if (setting.fixRandom) return candidate
            if (Random.nextFloat() * 100f < 15f + 0.05f * setting.modifiedWisdom) return candidate
        }
        return candidates.last()
    }

    fun RaceState.calcSpurtDistance(v: Float): Float {
        return (
                (simulation.sp -
                        ((setting.courseLength - simulation.position - 60) *
                                20 *
                                spConsumptionCoef[setting.trackDetail.surface]!![
                                    setting.track.surfaceCondition
                                ]!! *
                                setting.spurtSpCoef *
                                (setting.v3 - setting.baseSpeed + 12).pow(2)) /
                        144 /
                        setting.v3) /
                        (20 *
                                spConsumptionCoef[setting.trackDetail.surface]!![
                                    setting.track.surfaceCondition
                                ]!! *
                                setting.spurtSpCoef *
                                ((v - setting.baseSpeed + 12).pow(2) / 144 / v -
                                        (setting.v3 - setting.baseSpeed + 12).pow(2) / 144 / setting.v3)) +
                        60
                )
    }

    fun RaceState.calcRequiredSp(v: Float): Float {
        return (
                ((setting.courseLength - simulation.position - 60) *
                        20 *
                        spConsumptionCoef[setting.trackDetail.surface]!![
                            setting.track.surfaceCondition
                        ]!! *
                        setting.spurtSpCoef *
                        (setting.v3 - setting.baseSpeed + 12).pow(2)) /
                        144 /
                        setting.v3 +
                        (setting.courseLength - simulation.position - 60) *
                        (20 *
                                spConsumptionCoef[setting.trackDetail.surface]!![
                                    setting.track.surfaceCondition
                                ]!! *
                                setting.spurtSpCoef *
                                ((v - setting.baseSpeed + 12).pow(2) / 144 / v -
                                        (setting.v3 - setting.baseSpeed + 12).pow(2) / 144 / setting.v3))
                )
    }

    private fun RaceState.consumePerSecond(baseSpeed: Float, v: Float, phase: Int): Float {
        var ret =
            (20.0f *
                    spConsumptionCoef[setting.trackDetail.surface]!![
                        setting.track.surfaceCondition
                    ]!! *
                    (v - baseSpeed + 12).pow(2)) /
                    144
        if (phase >= 2) {
            ret *= setting.spurtSpCoef
        }
        return ret
    }

    private fun RaceState.goal(): RaceSimulationResult {
        val excessTime = (simulation.position - setting.courseLength) / simulation.currentSpeed
        val raceTime = simulation.frameElapsed * frameLength - excessTime
        val raceTimeDelta = raceTime - setting.trackDetail.finishTimeMax / 1.18f
        return RaceSimulationResult(
            raceTime = raceTime,
            raceTimeDelta = raceTimeDelta,
            maxSpurt = simulation.maxSpurt,
            spDiff = simulation.spurtParameters?.spDiff ?: 0f,
        )
    }

    private fun RaceState.checkSkillTrigger(startPosition: Float): List<Skill> {
        // TODO
        return emptyList()
    }

}