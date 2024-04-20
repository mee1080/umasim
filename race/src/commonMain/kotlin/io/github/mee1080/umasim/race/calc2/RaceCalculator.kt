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

import io.github.mee1080.umasim.race.data.*
import io.github.mee1080.umasim.race.data2.approximateConditions
import kotlin.math.*
import kotlin.random.Random

class RaceCalculator(
    private val setting: RaceSetting,
    private val system: SystemSetting = SystemSetting(),
) {

    fun simulate(): Pair<RaceSimulationResult, RaceState> {
        val state = initializeState()
        val result = state.progressRace()
        return result to state
    }

    private fun initializeState(): RaceState {
        val state = RaceState(setting, RaceSimulationState(), system)
        state.invokeSkills()
        val simulation = state.simulation
        if (!state.setting.fixRandom) {
            simulation.startDelay = Random.nextDouble() * 0.1
        }

        state.triggerStartSkills()

        // initTemptation
        // FIXME 自制心
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
    val invokeRate = if (setting.skillActivateAdjustment != SkillActivateAdjustment.NONE) {
        100.0
    } else {
        maxOf(100.0 - 9000.0 / setting.umaStatus.wisdom, 20.0)
    }
    setting.hasSkills.map {
        if (it.rarity == "unique") {
            it.applyLevel(setting.uniqueLevel)
        } else it
    }.forEach { skill ->
        val calculatedAreas = mutableMapOf<String, List<RandomEntry>>()
        if (skill.activateLot == 0 || Random.nextDouble() * 100 < invokeRate) {
            skill.invokes.forEach { invoke ->
                simulation.invokedSkills += InvokedSkill(
                    skill,
                    invoke,
                    checkCondition(skill, invoke.preConditions, setting, calculatedAreas),
                    checkCondition(skill, invoke.conditions, setting, calculatedAreas),
                )
            }
        }
    }
}

private fun RaceState.triggerStartSkills() {
    val skills = mutableListOf<InvokedSkill>()
    simulation.invokedSkills.removeAll { skill ->
        var remove = false
        if (skill.invoke.isPassive) {
            if (skill.check(this)) {
                triggerSkill(skill)
                simulation.skillTriggerCount.increment(this)
                simulation.passiveTriggered++
                skills += skill
            }
            remove = true
        }
        if (skill.invoke.isStart) {
            if (skill.invoke.startMultiply > 0.0) {
                simulation.startDelay *= skill.invoke.startMultiply
            }
            simulation.startDelay += skill.invoke.startAdd
            triggerSkill(skill)
            simulation.skillTriggerCount.increment(this)
            skills += skill
            remove = true
        }
        remove
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
            temptation = simulation.isInTemptation,
            paceDownMode = paceDownMode,
            downSlopeMode = simulation.isInDownSlopeMode,
            leadCompetition = inLeadCompetition,
            competeFight = simulation.competeFight,
            conservePower = isInConservePower,
            positionCompetition = simulation.positionCompetition,
            staminaKeep = simulation.staminaKeep,
            secureLead = simulation.secureLead,
            staminaLimitBreak = simulation.staminaLimitBreak,
        )
        // 1秒おき判定
        val changeSecond = simulation.frameElapsed % framePerSecond == framePerSecond - 1
        val currentSection = currentSection

        // 下り坂モードに入るか・終わるかどうかの判定
        if (isInSlopeDown() && !setting.fixRandom) {
            // 1秒置きなので、このフレームは整数秒を含むかどうかのチェック
            if (changeSecond) {
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
                (simulation.frameElapsed - simulation.temptationModeStart!!) * secondPerFrame
            val prevTemptationDuration =
                (simulation.frameElapsed - 1 - simulation.temptationModeStart!!) * secondPerFrame
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

        // 位置取り争い
        if (simulation.leadCompetitionStart == null && setting.basicRunningStyle == Style.NIGE && simulation.position >= system.leadCompetitionPosition) {
            simulation.leadCompetitionStart = simulation.frameElapsed
        }

        // 追い比べ
        if (simulation.competeFight) {
            // HP5%以下で終了
            if (simulation.sp <= 0.05 * setting.spMax) {
                simulation.competeFight = false
            }
        } else {
            // 最終直線でHP15%以上で発動可能、1秒ごとに一定確率で発動するよう近似
            if (changeSecond && isInFinalStraight() && simulation.sp >= 0.15 * setting.spMax && Random.nextDouble() < system.competeFightRate) {
                simulation.competeFight = true
            }
        }

        if (currentSection in 11..15) {
            // 位置取り調整/持久力温存
            if (simulation.frameElapsed >= simulation.positionCompetitionNextFrame) {
                if (simulation.positionCompetition) {
                    // 位置取り調整終了後は1秒のクールタイム
                    simulation.positionCompetition = false
                    simulation.positionCompetitionNextFrame = simulation.frameElapsed + framePerSecond
                } else if (!simulation.staminaKeep) {
                    applyPositionCompetition()
                }
            }

            // リード確保
            if (simulation.frameElapsed >= simulation.secureLeadNextFrame) {
                if (simulation.secureLead) {
                    // リード確保終了後は1秒のクールタイム
                    simulation.secureLead = false
                    simulation.secureLeadNextFrame = simulation.frameElapsed + framePerSecond
                } else if (setting.runningStyle != Style.OI) {
                    if (Random.nextDouble() < system.secureLeadRate) {
                        // リード確保発生時は2秒後に終了
                        simulation.secureLead = true
                        simulation.secureLeadNextFrame = simulation.frameElapsed + framePerSecond * 2
                        simulation.sp -= setting.secureLeadStamina
                    } else {
                        // 非発生時は2秒後に再判定
                        simulation.secureLeadNextFrame = simulation.frameElapsed + framePerSecond * 2
                    }
                }
            }
        } else if (currentSection == 16) {
            simulation.positionCompetition = false
            if (simulation.staminaKeep) {
                simulation.staminaKeepDistance += simulation.position - simulation.staminaKeepStart
                simulation.staminaKeep = false
            }
            simulation.secureLead = false
        }

        // スタミナ勝負
        if (setting.courseLength > 2100 && !simulation.staminaLimitBreak) {
            if (simulation.currentSpeed >= setting.maxSpurtSpeed) {
                simulation.staminaLimitBreak = true
            }
        }

        move(secondPerFrame)
        frame = frame.copy(
            movement = simulation.position - simulation.startPosition,
            consume = simulation.sp - startSp,
            targetSpeed = targetSpeed,
            acceleration = acceleration
        )
        simulation.frameElapsed++

        // 終盤入り
        if (startPhase == 1 && currentPhase == 2) {
            // ラストスパート計算
            simulation.spurtParameters = calcSpurtParameter()

            // 脚色十分
            applyConservePower()
        }

        if (simulation.position >= setting.courseLength) {
            break
        }

        // スキル条件近似
        if (changeSecond) {
            approximateConditions.forEach { entry ->
                simulation.specialState[entry.key] = entry.value.update(this, simulation.specialState[entry.key] ?: 0)
            }
        }

        // Calculate target speed of next frame and do heal/fatigue
        val skillTriggered = checkSkillTrigger()
        val spurtParameters = simulation.spurtParameters
        val spurting =
            spurtParameters != null && simulation.position + spurtParameters.distance >= setting.courseLength

        // Remove overtime skills
        val endedSkills = simulation.operatingSkills.filter { operatingSkill ->
            val duration = operatingSkill.duration
            (simulation.frameElapsed - operatingSkill.startFrame) * secondPerFrame > duration * setting.timeCoef
        }
        simulation.operatingSkills.removeAll(endedSkills)

        frame = frame.copy(
            skills = skillTriggered,
            endedSkills = endedSkills,
            spurting = spurting,
        )
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
        simulation.sp -= calcConsumePerSecond() * elapsedTime

        this.updateStartDash()
    }
}

private fun RaceState.updateSelfSpeed(elapsedTime: Double) {
    val targetSpeed = targetSpeed
    var newSpeed = if (simulation.currentSpeed < targetSpeed) {
        min(simulation.currentSpeed + elapsedTime * acceleration, targetSpeed)
    } else {
        max(simulation.currentSpeed + elapsedTime * deceleration, targetSpeed)
    }
    if (simulation.isStartDash && newSpeed > setting.v0) {
        newSpeed = setting.v0
    }
    newSpeed = max(min(newSpeed, maxSpeed), vMin)

    // TODO ブロック
    // newSpeed = 前のウマ娘の0.988倍(0m)～1.0倍(2m)

    newSpeed -= simulation.speedDebuff
    val speedModification = simulation.operatingSkills.sumOf {
        // 減速スキルの現在速度低下分
        it.data.invoke.currentSpeed
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

private fun RaceState.goal(): RaceSimulationResult {
    val excessTime = (simulation.position - setting.courseLength) / simulation.currentSpeed
    val raceTime = simulation.frameElapsed * secondPerFrame - excessTime
    val raceTimeDelta = raceTime - setting.trackDetail.finishTimeMax / 1.18
    return RaceSimulationResult(
        raceTime = raceTime,
        raceTimeDelta = raceTimeDelta,
        maxSpurt = simulation.maxSpurt,
        spDiff = simulation.spurtParameters?.spDiff ?: 0.0,
        positionCompetitionCount = simulation.positionCompetitionCount,
        staminaKeepDistance = simulation.staminaKeepDistance,
    )
}

private fun RaceState.applyConservePower() {
    val base = setting.conservePowerAccelerationBase
    if (base == 0.0) return
    val activityCoef = when {
        simulation.hasTemptation -> 0.8
        simulation.hasLeadCompetition -> 0.98
        else -> 1.0
    }
    simulation.conservePowerAcceleration = base * activityCoef
    simulation.conservePowerStart = simulation.frameElapsed
}

fun RaceState.applyPositionCompetition() {
    val requiredSp = calcRequiredSpInPhase2()
    if (simulation.sp < requiredSp * Random.nextDouble(1.035, 1.04) && Random.nextDouble() < system.staminaKeepRate) {
        // 持久力温存
        simulation.staminaKeep = true
        simulation.staminaKeepStart = simulation.position
        simulation.positionCompetitionNextFrame = Int.MAX_VALUE
    } else {
        if (simulation.staminaKeep) {
            simulation.staminaKeepDistance += simulation.position - simulation.staminaKeepStart
            simulation.staminaKeep = false
        }
        if (Random.nextDouble() < system.positionCompetitionRate) {
            // 2秒間位置取り調整
            simulation.positionCompetition = true
            simulation.positionCompetitionNextFrame = simulation.frameElapsed + framePerSecond * 2
            simulation.sp -= setting.positionCompetitionStamina
            simulation.positionCompetitionCount++
        } else {
            // 2秒後に再判定
            simulation.positionCompetition = false
            simulation.positionCompetitionNextFrame = simulation.frameElapsed + framePerSecond * 2
        }
    }
}