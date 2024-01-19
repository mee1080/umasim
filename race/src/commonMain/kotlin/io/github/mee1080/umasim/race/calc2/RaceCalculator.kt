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
package io.github.mee1080.umasim.race.calc2

import io.github.mee1080.umasim.race.data.SkillEffect
import io.github.mee1080.umasim.race.data.frameLength
import io.github.mee1080.umasim.race.data.maxSpeed
import io.github.mee1080.umasim.race.data.spConsumptionCoef
import kotlin.math.*
import kotlin.random.Random

class RaceCalculator(private val setting: RaceSetting) {

    fun simulate(): Pair<RaceSimulationResult, RaceState> {
        val state = initializeState()
        val result = state.progressRace()
        return result to state
    }

    private fun initializeState(): RaceState {
        val state = RaceState(setting, RaceSimulationState())
        state.invokeSkills()
        val simulation = state.simulation
        if (!state.setting.fixRandom) {
            simulation.startDelay = Random.nextDouble() * 0.1
        }

        state.triggerStartSkills()

        // initTemptation
        if (Random.nextDouble() * 100.0 < state.setting.temptationRate) {
            simulation.temptationSection = 1 + Random.nextInt(8)
        }
        simulation.isStartDash = true
        simulation.delayTime = simulation.startDelay
        simulation.sp = state.setting.spMax
        simulation.sectionTargetSpeedRandoms = state.initSectionTargetSpeedRandoms()
        return state
    }

}

private fun RaceState.invokeSkills() {
    setting.hasSkills.forEach { skill ->
        skill.invokes.forEach { invoke ->
            val invokeRate = if (setting.skillActivateAdjustment > 0 || skill.activateLot == 0) {
                100.0
            } else {
                maxOf(100.0 - 9000.0 / setting.umaStatus.wisdom, 20.0)
            }
            if (Random.nextDouble() * 100 < invokeRate) {
                // TODO init
                simulation.invokedSkills += InvokedSkill(skill, invoke, checkCondition(invoke, setting))
            }
        }
    }
}

private fun RaceState.triggerStartSkills() {
    val skills = mutableListOf<InvokedSkill>()
    simulation.invokedSkills.removeAll { skill ->
        when (skill.displayType) {
            "passive" -> {
                if (skill.checkAll(this) && (skill.triggerRate == null || Random.nextDouble() < skill.triggerRate)) {
                    skill.trigger(this)
                    simulation.skillTriggerCount[0]++
                    simulation.passiveTriggered++
                    skills += skill
                }
                true
            }

            "gate" -> {
                simulation.startDelay *= skill.startDelay ?: 1.0
                skill.trigger(this)
                simulation.skillTriggerCount[0]++
                skills += skill
                true
            }

            else -> false
        }
    }
    simulation.frames += RaceFrame(
        speed = 0.0,
        sp = setting.spMax,
        startPosition = 0.0,
        skills = skills,
    )
}

private fun RaceState.initSectionTargetSpeedRandoms(): List<Double> {
    return (0..24).map {
        val max = (setting.modifiedWisdom / 5500.0) *
                log10(setting.modifiedWisdom * 0.1) * 0.01
        if (setting.fixRandom) {
            max - 0.00325
        } else {
            max + Random.nextDouble() * -0.0065
        }
    }
}

private fun RaceState.progressRace(): RaceSimulationResult {
    while (simulation.position < setting.courseLength) {
        if (simulation.frameElapsed > 5000) {
            break
        }
        simulation.startPosition = simulation.position
        val startSp = simulation.sp
        val startPhase = currentPhase
        var frame = RaceFrame(
            speed = simulation.currentSpeed,
            sp = simulation.sp,
            startPosition = simulation.startPosition,
        )

        // 下り坂モードに入るか・終わるかどうかの判定
        if (isInSlopeDown() && !setting.fixRandom) {
            // 1秒置きなので、このフレームは整数秒を含むかどうかのチェック
            if (floor(simulation.frameElapsed * frameLength).toInt() != floor(
                    (simulation.frameElapsed + 1) * frameLength
                ).toInt()
            ) {
                if (simulation.downSlopeModeStart == null) {
                    if (Random.nextDouble() < setting.modifiedWisdom * 0.0004) {
                        simulation.downSlopeModeStart = simulation.frameElapsed
                    }
                } else {
                    if (Random.nextDouble() < 0.2) {
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
                    if (Random.nextDouble() < 0.55) {
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
            movement = simulation.position - simulation.startPosition,
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
        val skillTriggered = checkSkillTrigger()
        val spurtParameters = simulation.spurtParameters
        val spurting =
            spurtParameters != null && simulation.position + spurtParameters.distance >= setting.courseLength
        frame = frame.copy(
            skills = skillTriggered,
            spurting = spurting,
        )

        // Remove overtime skills
        simulation.operatingSkills.removeAll { operatingSkill ->
            val duration = operatingSkill.duration ?: 0.0
            (simulation.frameElapsed - operatingSkill.startFrame) * frameLength > duration * setting.timeCoef
        }

        simulation.frames += frame
    }

    return goal()
}

private fun RaceState.move(elapsedTime: Double) {
    if (simulation.delayTime > 0.0) {
        simulation.delayTime -= elapsedTime
    }
    if (simulation.delayTime <= 0.0) {
        var timeAfterDelay = elapsedTime
        if (simulation.delayTime < 0.0) {
            timeAfterDelay = abs(simulation.delayTime)
            simulation.delayTime = 0.0
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
            consume *= 0.4
        }
        if (simulation.isInTemptation) {
            simulation.temptationWaste += consume * 0.6
            consume *= 1.6
        }
        simulation.sp -= consume

        this.updateStartDash()
    }
}

private fun RaceState.updateSelfSpeed(elapsedTime: Double) {
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
        it.data.speed ?: 0.0
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

fun RaceState.calcSpurtParameter(): SpurtParameters {
    val maxDistance = setting.trackDetail.distance - simulation.position
    val spurtDistance = calcSpurtDistance(setting.maxSpurtSpeed)
    val totalConsume = calcRequiredSp(setting.maxSpurtSpeed)
    if (spurtDistance >= maxDistance) {
        if (simulation.position <= (setting.courseLength * 2.0) / 3 + 5) {
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
            distance = 0.0,
            speed = setting.v3,
            spDiff = simulation.spurtParameters?.spDiff ?: (simulation.sp - totalConsume),
        )
    }
    val candidates = ((setting.v3 * 10).toInt()..(setting.maxSpurtSpeed * 10 - 1).toInt()).map {
        val v = it * 0.1
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
        if (Random.nextDouble() * 100.0 < 15.0 + 0.05 * setting.modifiedWisdom) return candidate
    }
    return candidates.last()
}

fun RaceState.calcSpurtDistance(v: Double): Double {
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

fun RaceState.calcRequiredSp(v: Double): Double {
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

private fun RaceState.consumePerSecond(baseSpeed: Double, v: Double, phase: Int): Double {
    var ret =
        (20.0 *
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
    val raceTimeDelta = raceTime - setting.trackDetail.finishTimeMax / 1.18
    return RaceSimulationResult(
        raceTime = raceTime,
        raceTimeDelta = raceTimeDelta,
        maxSpurt = simulation.maxSpurt,
        spDiff = simulation.spurtParameters?.spDiff ?: 0.0,
    )
}
