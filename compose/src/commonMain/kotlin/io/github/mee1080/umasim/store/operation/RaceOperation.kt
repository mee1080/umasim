package io.github.mee1080.umasim.store.operation

import io.github.mee1080.umasim.race.averageOf
import io.github.mee1080.umasim.race.calc2.*
import io.github.mee1080.umasim.store.*
import io.github.mee1080.umasim.store.framework.AsyncOperation
import io.github.mee1080.umasim.store.framework.OnRunning

private val simulationTag = OnRunning.Tag()

private val simulationPolicy = OnRunning.Ignore(simulationTag)

private val simulationCancelPolicy = OnRunning.CancelAndRun(simulationTag)

fun runSimulation(overrideCount: Int? = null) = AsyncOperation<AppState>({ state ->
    val simulationCount = overrideCount ?: state.simulationCount
    if (simulationCount <= 0) return@AsyncOperation
    emit { it.copy(simulationSummary = null) }
    val calculator = RaceCalculator(state.setting)
    val results = mutableListOf<RaceSimulationResult>()
    val skillSummaries = state.setting.hasSkills.associate {
        it.name to mutableListOf<SimulationSkillInfo>()
    }
    var raceFrameList: List<RaceFrame>? = null
    repeat(simulationCount) { count ->
        if (count % 10 == 0) {
            emit { it.copy(simulationProgress = count + 1) }
        }
        val result = calculator.simulate()
        results += result.first
        createSkillMap(result.second).forEach {
            skillSummaries[it.key]?.add(it.value)
        }
        raceFrameList = result.second.simulation.frames
    }
    val graphData = toGraphData(state.setting, raceFrameList)
    val spurtResults = results.filter { it.maxSpurt }
    val notSpurtResult = results.filter { !it.maxSpurt }
    val summary = SimulationSummary(
        setting = state.setting,
        allSummary = toSummary(results),
        spurtSummary = toSummary(spurtResults),
        notSpurtSummary = toSummary(notSpurtResult),
        spurtRate = spurtResults.size.toDouble() / results.size,
        skillSummaries = skillSummaries.map { it.key to toSummary(it.value) },
    )
    emit {
        it.copy(
            simulationProgress = 0,
            simulationSummary = summary,
            graphData = graphData,
        )
    }
}, simulationPolicy)

private fun toSummary(result: List<RaceSimulationResult>): SimulationSummaryEntry {
    return if (result.isEmpty()) SimulationSummaryEntry() else SimulationSummaryEntry(
        count = result.size,
        averageTime = result.averageOf { it.raceTime },
        bestTime = result.minOf { it.raceTime },
        worstTime = result.maxOf { it.raceTime },
        averageSp = result.averageOf { it.spDiff },
        bestSp = result.maxOf { it.spDiff },
        worstSp = result.minOf { it.spDiff },
        positionCompetitionCount = result.averageOf { it.positionCompetitionCount.toDouble() },
        staminaKeepRate = result.count { it.staminaKeepDistance > 0.0 } / result.size.toDouble(),
        staminaKeepDistance = result.averageOf { it.staminaKeepDistance },
    )
}

private fun createSkillMap(state: RaceState): Map<String, SimulationSkillInfo> {
    val (phase1Start, phase2Start) = getPhaseChangeFrames(state)
    val skillMap = state.setting.hasSkills.associate {
        it.name to SimulationSkillInfo(phase1Start, phase2Start)
    }.toMutableMap()
    state.simulation.frames.forEachIndexed { index, frame ->
        frame.skills.forEach {
            val current = skillMap[it.skill.name] ?: return@forEach
            if (current.startFrame1 < 0) {
                skillMap[it.skill.name] = current.copy(startFrame1 = index)
            } else {
                skillMap[it.skill.name] = current.copy(startFrame2 = index)
            }
        }
        frame.endedSkills.forEach {
            val current = skillMap[it.data.skill.name] ?: return@forEach
            if (current.endFrame1 < 0) {
                skillMap[it.data.skill.name] = current.copy(endFrame1 = index)
            } else {
                skillMap[it.data.skill.name] = current.copy(endFrame2 = index)
            }
        }
    }
    return skillMap
}

private fun getPhaseChangeFrames(state: RaceState): Pair<Int, Int> {
    var phase1 = -1
    var phase2 = -1
    var nextPhaseStart = state.setting.phase1Start
    state.simulation.frames.forEachIndexed { index, frame ->
        if (phase1 < 0) {
            if (frame.startPosition >= nextPhaseStart) {
                phase1 = index - 1
                nextPhaseStart = state.setting.phase2Start
            }
        } else if (phase2 < 0) {
            if (frame.startPosition >= nextPhaseStart) {
                phase2 = index - 1
                return@forEachIndexed
            }
        }
    }
    return phase1 to phase2
}

private fun toSummary(list: List<SimulationSkillInfo>): SimulationSkillSummary {
    val triggeredList = list.filter { it.startFrame1 >= 0 }
    val secondTriggeredList = triggeredList.filter { it.startFrame2 >= 0 }
    val phase1ConnectedList = triggeredList.mapNotNull { it.phase1ConnectionFrame }
    val phase2ConnectedList = triggeredList.mapNotNull { it.phase2ConnectionFrame }
    return SimulationSkillSummary(
        count = triggeredList.size,
        triggerRate = if (list.isEmpty()) 0.0 else triggeredList.size.toDouble() / list.size,
        averageStartFrame1 = triggeredList.averageOf { it.startFrame1.toDouble() },
        doubleTriggerRate = if (list.isEmpty()) 0.0 else secondTriggeredList.size.toDouble() / list.size,
        averageStartFrame2 = secondTriggeredList.averageOf { it.startFrame2.toDouble() },
        phase0TriggeredRate = if (triggeredList.isEmpty()) 0.0 else {
            triggeredList.count { it.triggeredPhase == 0 }.toDouble() / triggeredList.size
        },
        phase1ConnectionRate = if (triggeredList.isEmpty()) 0.0 else phase1ConnectedList.size.toDouble() / triggeredList.size,
        averagePhase1ConnectionFrame = phase1ConnectedList.average(),
        phase1TriggeredRate = if (triggeredList.isEmpty()) 0.0 else {
            triggeredList.count { it.triggeredPhase == 1 }.toDouble() / triggeredList.size
        },
        phase2ConnectionRate = if (triggeredList.isEmpty()) 0.0 else phase2ConnectedList.size.toDouble() / triggeredList.size,
        averagePhase2ConnectionFrame = phase2ConnectedList.average(),
        phase2TriggeredRate = if (triggeredList.isEmpty()) 0.0 else {
            triggeredList.count { it.triggeredPhase == 2 }.toDouble() / triggeredList.size
        },
        averagePhase2DelayFrame = triggeredList.mapNotNull { it.phase2DelayFrame }.average(),
    )
}

private const val speedMin = 13f
private const val speedMax = 28f

private const val staminaMin = -200f

private const val areaHeight = 0.1f

private fun adjustRange(value: Double, min: Float, max: Float) = (value.toFloat() - min) / (max - min)

private fun toGraphData(setting: RaceSetting, frameList: List<RaceFrame>?): GraphData? {
    if (frameList.isNullOrEmpty()) return null
    val staminaMax = frameList[0].sp.toFloat()
    val trackDetail = setting.trackDetail
    return GraphData(
        frameList = frameList,
        speedData = frameList.mapIndexed { index, raceFrame ->
            index / 15f to adjustRange(raceFrame.speed, speedMin, speedMax)
        },
        staminaData = frameList.mapIndexed { index, raceFrame ->
            index / 15f to adjustRange(raceFrame.sp, staminaMin, staminaMax)
        },
        staminaZero = adjustRange(0.0, staminaMin, staminaMax),
        staminaOverData = frameList.mapIndexedNotNull { index, raceFrame ->
            if (raceFrame.sp >= 0) null else {
                index / 15f to adjustRange(raceFrame.sp, staminaMin, staminaMax)
            }
        },
        phase1Start = frameList.indexOfFirst { it.startPosition >= setting.phase1Start } / 15f,
        phase2Start = frameList.indexOfFirst { it.startPosition >= setting.phase2Start } / 15f,
        straightData = toGraphData(trackDetail.straights.map { it.start to it.end }, frameList),
        cornerData = toGraphData(trackDetail.corners.map { it.start to it.end }, frameList),
        upSlopeData = toGraphData(trackDetail.slopes.filter { it.slope > 0f }.map { it.start to it.end }, frameList),
        downSlopeData = toGraphData(trackDetail.slopes.filter { it.slope < 0f }.map { it.start to it.end }, frameList),
        skillData = buildList {
            var last = frameList[0]
            frameList.forEachIndexed { index, raceFrame ->
                raceFrame.skills.forEach {
                    add(index / 15f to it.skill.name)
                }
                add(index, raceFrame, last, "掛かり") { it.temptation }
//                add(index, raceFrame, last, "スパート開始") { it.spurting }
//                add(index, raceFrame, last, "ペースダウンモード") { it.paceDownMode }
//                add(index, raceFrame, last, "下り坂モード") { it.downSlopeMode }
//                add(index, raceFrame, last, "位置取り争い") { it.leadCompetition }
                add(index, raceFrame, last, "追い比べ") { it.competeFight }
//                add(index, raceFrame, last, "脚色十分") { it.conservePower }
//                add(index, raceFrame, last, "位置取り調整") { it.positionCompetition }
                add(index, raceFrame, last, "持久力温存") { it.staminaKeep }
//                add(index, raceFrame, last, "リード確保") { it.secureLead }
                add(index, raceFrame, last, "スタミナ勝負") { it.staminaLimitBreak }
                last = raceFrame
            }
        }.sortedBy { it.first }
    )
}

private fun MutableList<Pair<Float, String>>.add(
    frame: Int,
    raceFrame: RaceFrame,
    last: RaceFrame,
    label: String,
    check: (RaceFrame) -> Boolean,
) {
    if (check(raceFrame) && !check(last)) {
        add(frame / 15f to label)
    }
}

private fun toGraphData(
    areas: List<Pair<Double, Double>>,
    frameList: List<RaceFrame>,
): List<Pair<Float, Float>> {
    return buildList {
        var areaIndex = 0
        var inArea = false
        frameList.forEachIndexed { index, raceFrame ->
            val area = areas.getOrNull(areaIndex) ?: return@buildList
            if (inArea) {
                if (raceFrame.startPosition >= area.second) {
                    inArea = false
                    add(index / 15f to areaHeight)
                    add(index / 15f to 0f)
                    areaIndex++
                }
            } else {
                if (raceFrame.startPosition >= area.first) {
                    inArea = true
                    add(index / 15f to 0f)
                    add(index / 15f to areaHeight)
                }
            }
        }
        if (areas.getOrNull(areaIndex) != null) {
            add((frameList.size - 1) / 15f to areaHeight)
            add((frameList.size - 1) / 15f to 0f)
        }
    }
}

fun cancelSimulation() = AsyncOperation<AppState>({ state ->
    emit { it.copy(simulationProgress = 0, simulationSummary = null) }
}, simulationCancelPolicy)
