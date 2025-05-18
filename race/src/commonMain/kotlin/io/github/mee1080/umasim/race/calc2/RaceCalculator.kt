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
import kotlin.random.nextInt

class RaceCalculator(
    private val system: SystemSetting,
) {

    fun simulate(setting: RaceSetting): Pair<RaceSimulationResult, RaceState> {
        val state = setting.initializeState()
        val result = state.progressRace()
        return result to state
    }

    private fun RaceSetting.initializeState(): RaceState {
        val invokedSkills = invokeSkills()
        val gateCount = track.gateCount
        val gateNumber = when (umaStatus.gateNumber) {
            0 -> Random.nextInt(1..gateCount)
            -1 -> Random.nextInt(1..((3..6).maxBy { gateNumberToPostNumber[it][gateCount] <= 3 }))
            -2 -> Random.nextInt(((6..12).maxBy { gateNumberToPostNumber[it][gateCount] >= 6 })..8)
            else -> umaStatus.gateNumber
        }
        val initialLane = gateNumber * horseLane + track.initialLaneAdjuster
        val simulationState = RaceSimulationState(
            currentLane = initialLane,
            targetLane = initialLane,
            invokedSkills = invokedSkills,
            postNumber = gateNumberToPostNumber[gateNumber][gateCount],
        )
        val settingWithPassive = applyPassive(system, simulationState)
        simulationState.passiveTriggered = settingWithPassive.passiveBonus.skills.size

        val virtualLeader = if (positionKeepMode == PositionKeepMode.VIRTUAL) {
            copy(umaStatus = virtualLeader, positionKeepMode = PositionKeepMode.SPEED_UP).initializeState()
        } else null
        val state = RaceState(settingWithPassive, simulationState, system, virtualLeader)
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

        approximateConditions.forEach { entry ->
            simulation.specialState[entry.key] = entry.value.valueOnStart
        }

        simulation.forceInSpeed = Random.nextDouble(0.1) * forceInFixed[umaStatus.style]!!

        return state
    }

}

private fun RaceSetting.invokeSkills(): List<InvokedSkill> {
    val invokeRate = if (skillActivateAdjustment != SkillActivateAdjustment.NONE) 100.0 else skillActivateRate
    return buildList {
        umaStatus.hasSkills.map {
            if (it.rarity == "unique") {
                it.applyLevel(umaStatus.uniqueLevel)
            } else it
        }.forEach { skill ->
            val calculatedAreas = mutableMapOf<String, List<RandomEntry>>()
            if (skill.activateLot == 0 || Random.nextDouble() * 100 < invokeRate) {
                skill.invokes.forEach { invoke ->
                    add(
                        InvokedSkill(
                            skill,
                            invoke,
                            checkCondition(skill, invoke.preConditions, this@invokeSkills, calculatedAreas),
                            checkCondition(skill, invoke.conditions, this@invokeSkills, calculatedAreas),
                        )
                    )
                }
            }
        }
    }
}

private fun RaceSetting.applyPassive(system: SystemSetting, simulation: RaceSimulationState): RaceSettingWithPassive {
    var passiveBonus = PassiveBonus()
    val stateForCheck = RaceState(RaceSettingWithPassive(this, passiveBonus), simulation, system, null)
    simulation.invokedSkills.forEach { skill ->
        if (skill.invoke.isPassive && skill.check(stateForCheck)) {
            passiveBonus = passiveBonus.add(stateForCheck, skill)
        }
    }
    return RaceSettingWithPassive(this, passiveBonus)
}

private fun RaceState.triggerStartSkills() {
    val skills = mutableListOf<TriggeredSkill>()
    setting.passiveBonus.skills.forEach { skill ->
        if (!skill.invoke.isStart) {
            skills += triggerSkill(skill)
        }
    }
    simulation.invokedSkills.forEach { skill ->
        if (skill.invoke.isStart) {
            simulation.startDelay *= skill.invoke.startMultiply(this)
            simulation.startDelay += skill.invoke.startAdd(this)
            skills += triggerSkill(skill)
        }
    }
    simulation.frames += RaceFrame(
        speed = 0.0,
        sp = setting.spMax,
        startPosition = 0.0,
        currentLane = simulation.currentLane,
        triggeredSkills = skills,
        paceMakerFrame = paceMaker?.simulation?.frames?.lastOrNull(),
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
        if (updateFrame()) break
    }
    return goal()
}

private fun RaceState.updateFrame(): Boolean {
    if (simulation.frameElapsed > 5000) {
        return true
    }
    paceMaker?.updateFrame()
    simulation.startPosition = simulation.position
    val startSp = simulation.sp
    var frame = RaceFrame(
        speed = simulation.currentSpeed,
        sp = simulation.sp,
        startPosition = simulation.startPosition,
        currentLane = simulation.currentLane,
        temptation = simulation.isInTemptation,
        positionKeepState = simulation.positionKeepState,
        downSlopeMode = simulation.isInDownSlopeMode,
        leadCompetition = inLeadCompetition,
        competeFight = simulation.competeFight,
        conservePower = isInConservePower,
        positionCompetition = simulation.positionCompetition,
        staminaKeep = simulation.staminaKeep,
        secureLead = simulation.secureLead,
        staminaLimitBreak = simulation.staminaLimitBreak,
        paceMakerFrame = paceMaker?.simulation?.frames?.lastOrNull(),
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

    // ポジションキープ
    if (currentSection <= 10) {
        applyPositionKeep()
    } else if (currentSection == 11) {
        simulation.positionKeepState = PositionKeepState.NONE
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
            simulation.competeFightEnd = simulation.frameElapsed
        }
    } else {
        // 最終直線でHP15%以上で発動可能、1秒ごとに一定確率で発動するよう近似
        if (changeSecond && simulation.frameElapsed >= framePerSecond * 2 - 1 && isInFinalStraight() && simulation.sp >= 0.15 * setting.spMax && Random.nextDouble() < system.competeFightRate) {
            simulation.competeFight = true
            simulation.competeFightStart = simulation.frameElapsed
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
    if (currentPhase == 1 && getPhase(simulation.position) == 2) {
        // ラストスパート計算
        simulation.spurtParameters = calcSpurtParameter()

        // 脚色十分
        applyConservePower()
    }

    if (simulation.position >= setting.courseLength) {
        return true
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
        (simulation.frameElapsed - operatingSkill.startFrame) * secondPerFrame > duration
    }
    simulation.operatingSkills.removeAll(endedSkills)

    // レーン移動
    applyMoveLane()

    frame = frame.copy(
        triggeredSkills = skillTriggered,
        endedSkills = endedSkills,
        operatingSkills = simulation.operatingSkills.toList(),
        spurting = spurting,
    )
    simulation.frames += frame
    return false
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
        val moveLength = simulation.currentSpeed * timeAfterDelay
        val cornerLoss = currentCorner?.let {
            moveLength / it.length / 4 * 2 * PI * simulation.currentLane
        } ?: 0.0

        // 移動距離及び耐力消耗を算出
        simulation.position += moveLength - cornerLoss
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
        it.currentSpeed
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
    val competeFightFrame = (simulation.competeFightEnd ?: simulation.frameElapsed) -
            (simulation.competeFightStart ?: simulation.frameElapsed)
    return RaceSimulationResult(
        raceTime = raceTime,
        raceTimeDelta = raceTimeDelta,
        maxSpurt = simulation.maxSpurt,
        spDiff = simulation.spurtParameters?.spDiff ?: 0.0,
        positionCompetitionCount = simulation.positionCompetitionCount,
        staminaKeepDistance = simulation.staminaKeepDistance,
        competeFightFinished = simulation.competeFightEnd == null && simulation.competeFightStart != null,
        competeFightTime = competeFightFrame * secondPerFrame,
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

fun RaceState.applyMoveLane() {
    if (setting.trackDetail.corners.isEmpty()) return

    val currentLane = simulation.currentLane

    // 最終コーナー/最終直線での目標レーン計算
    if (simulation.extraMoveLane < 0.0 && isAfterFinalCornerOrInFinalStraight) {
        simulation.extraMoveLane =
            min(currentLane / 0.1, setting.trackDetail.maxLaneDistance) * 0.5 + Random.nextDouble(0.1)
    }

    // 固定モード（危険回避）
    val fixLane = simulation.operatingSkills.any { it.fixLane }

    // 追い抜きモード
    val overtake = (simulation.specialState["overtake"] ?: 0) > 0

    // 横ブロック
    val sideBlocked = (simulation.specialState["blocked_side"] ?: 0) > 0

    // 目標レーン計算
    simulation.targetLane = when {
        fixLane -> 9.5 * horseLane

        // 追い抜きモードの場合、内側から1人分外側扱いとする
        overtake -> maxOf(simulation.targetLane, horseLane, simulation.extraMoveLane)

        simulation.sp <= 0.0 -> currentLane
        simulation.positionKeepState == PositionKeepState.PACE_DOWN -> 0.18
        simulation.extraMoveLane > currentLane -> simulation.extraMoveLane
        currentPhase <= 1 && !sideBlocked -> max(0.0, currentLane - 0.05)
        else -> currentLane
    }

    if ((sideBlocked && simulation.targetLane < currentLane) || abs(simulation.targetLane - currentLane) < 0.00001) {
        simulation.laneChangeSpeed = 0.0
    } else {
        val targetSpeed = if (simulation.position < setting.trackDetail.moveLanePoint) {
            setting.baseLaneChangeTargetSpeed * (1 + currentLane / setting.trackDetail.maxLaneDistance * 0.05)
        } else setting.baseLaneChangeTargetSpeed
        // 終盤の順位係数は無視

        simulation.laneChangeSpeed = min(simulation.laneChangeSpeed + laneChangeAccelerationPerFrame, targetSpeed)

        val actualSpeed = min(simulation.laneChangeSpeed + simulation.operatingSkills.sumOf { it.laneChangeSpeed }, 0.6)
        if (simulation.targetLane > currentLane) {
            simulation.currentLane = min(simulation.targetLane, currentLane + actualSpeed)
        } else {
            simulation.currentLane =
                max(simulation.targetLane, currentLane - actualSpeed * (1.0 + currentLane))
        }
    }
}

fun RaceState.applyPositionKeep() {
    when (setting.positionKeepMode) {
        PositionKeepMode.NONE -> return

        PositionKeepMode.APPROXIMATE -> {
            if (simulation.positionKeepState == PositionKeepState.NONE) {
                if (simulation.frameElapsed < simulation.positionKeepNextFrame) return
                if (paceDownModeSetting.getOrElse(currentSection) { false }) {
                    simulation.positionKeepState = PositionKeepState.PACE_DOWN
                    simulation.positionKeepExitPosition =
                        simulation.position + floor(setting.sectionLength) * (if (setting.oonige) 3 else 1)
                } else {
                    simulation.positionKeepNextFrame += framePerSecond * 2
                }
            } else {
                if (simulation.position >= simulation.positionKeepExitPosition) {
                    simulation.positionKeepState = PositionKeepState.NONE
                    simulation.positionKeepNextFrame += framePerSecond * 3
                }
            }
        }

        PositionKeepMode.VIRTUAL -> {
            val paceMaker = paceMaker ?: return
            val behind = paceMaker.simulation.startPosition - simulation.startPosition
            val myStyle = setting.basicRunningStyle
            // 自身が先頭の場合、逃げ先行は常に、差し追込は中盤以降なら、自身がペースメーカーになる
            val paceMakerIsSelf = behind <= 0 && (myStyle <= Style.SEN || currentPhase >= 1)
            val paceMakerStyle = paceMaker.setting.basicRunningStyle
            when (simulation.positionKeepState) {
                PositionKeepState.NONE -> {
                    if (simulation.frameElapsed < simulation.positionKeepNextFrame) return
                    if (behind > 0 && paceMakerStyle > myStyle) {
                        simulation.positionKeepState = PositionKeepState.PACE_UP_EX
                    } else if (myStyle == Style.NIGE) {
                        if (behind <= 0) {
                            val threshold = when {
                                setting.oonige -> -17.5
                                paceMakerStyle != Style.NIGE -> -12.5
                                else -> -4.5
                            }
                            if (behind > threshold && Random.nextDouble() < setting.positionKeepSpeedUpOvertakeRate) {
                                simulation.positionKeepState = PositionKeepState.SPEED_UP
                            }
                        } else {
                            if (Random.nextDouble() < setting.positionKeepSpeedUpOvertakeRate) {
                                simulation.positionKeepState = PositionKeepState.OVERTAKE
                            }
                        }
                    } else {
                        if (behind > setting.positionKeepMaxDistance) {
                            if (Random.nextDouble() < setting.positionKeepPaceUpRate) {
                                simulation.positionKeepState = PositionKeepState.PACE_UP
                                simulation.positionKeepExitDistance = Random.nextDouble(
                                    setting.positionKeepMinDistance, setting.positionKeepMaxDistance,
                                )
                            }
                        } else if (!paceMakerIsSelf && behind < setting.positionKeepMinDistance) {
                            if (simulation.operatingSkills.all { it.totalSpeed <= 0 }) {
                                simulation.positionKeepState = PositionKeepState.PACE_DOWN
                                val max = if (currentPhase == 1) {
                                    setting.positionKeepMinDistance + 0.5 * (setting.positionKeepMaxDistance - setting.positionKeepMinDistance)
                                } else setting.positionKeepMaxDistance
                                simulation.positionKeepExitDistance = Random.nextDouble(
                                    setting.positionKeepMinDistance, max,
                                )
                            }
                        }
                    }
                    if (simulation.positionKeepState == PositionKeepState.NONE) {
                        simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                    } else {
                        simulation.positionKeepExitPosition =
                            simulation.position + floor(setting.sectionLength) * (if (setting.oonige) 3 else 1)
                    }
                }

                PositionKeepState.SPEED_UP -> {
                    if (simulation.position >= simulation.positionKeepExitPosition) {
                        simulation.positionKeepState = PositionKeepState.NONE
                        simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                    } else {
                        val threshold = when {
                            setting.oonige -> -17.5
                            paceMakerStyle != Style.NIGE -> -12.5
                            else -> -4.5
                        }
                        if (behind < threshold) {
                            simulation.positionKeepState = PositionKeepState.NONE
                            simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                        }
                    }
                }

                PositionKeepState.OVERTAKE -> {
                    if (simulation.position >= simulation.positionKeepExitPosition) {
                        simulation.positionKeepState = PositionKeepState.NONE
                        simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                    } else {
                        val threshold = if (setting.oonige) -27.5 else -10.0
                        if (behind < threshold) {
                            simulation.positionKeepState = PositionKeepState.NONE
                            simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                        }
                    }
                }

                PositionKeepState.PACE_UP -> {
                    if (simulation.position >= simulation.positionKeepExitPosition) {
                        simulation.positionKeepState = PositionKeepState.NONE
                        simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                    } else {
                        if (behind < simulation.positionKeepExitDistance) {
                            simulation.positionKeepState = PositionKeepState.NONE
                            simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                        }
                    }
                }

                PositionKeepState.PACE_DOWN -> {
                    if (simulation.position >= simulation.positionKeepExitPosition) {
                        simulation.positionKeepState = PositionKeepState.NONE
                        simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                    } else {
                        if (
                            paceMakerIsSelf ||
                            behind > simulation.positionKeepExitDistance ||
                            simulation.operatingSkills.any { it.totalSpeed > 0 }
                        ) {
                            simulation.positionKeepState = PositionKeepState.NONE
                            simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                        }
                    }
                }

                PositionKeepState.PACE_UP_EX -> {
                    if (simulation.position >= simulation.positionKeepExitPosition) {
                        simulation.positionKeepState = PositionKeepState.NONE
                        simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                    } else {
                        if (behind <= 0) {
                            simulation.positionKeepState = PositionKeepState.NONE
                            simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                        }
                    }
                }
            }
        }

        PositionKeepMode.SPEED_UP -> {
            if (simulation.positionKeepState == PositionKeepState.NONE) {
                if (simulation.frameElapsed < simulation.positionKeepNextFrame) return
                if (Random.nextInt(100) < setting.positionKeepRate && Random.nextDouble() < setting.positionKeepSpeedUpOvertakeRate) {
                    simulation.positionKeepState = PositionKeepState.SPEED_UP
                    simulation.positionKeepExitPosition =
                        simulation.position + floor(setting.sectionLength) * (if (setting.oonige) 3 else 1)
                } else {
                    simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                }
            } else {
                if (simulation.position >= simulation.positionKeepExitPosition) {
                    simulation.positionKeepState = PositionKeepState.NONE
                    simulation.positionKeepNextFrame = simulation.frameElapsed + framePerSecond * 3
                }
            }
        }
    }
}