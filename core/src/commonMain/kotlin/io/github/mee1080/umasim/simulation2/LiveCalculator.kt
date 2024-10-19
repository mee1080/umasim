package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import kotlin.random.Random

object LiveCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        return calcLiveStatus(info, base, friendTraining)
    }

    private fun calcLiveStatus(
        info: Calculator.CalcInfo,
        base: Status,
        friendTraining: Boolean,
    ): Status {
        return if (info.liveStatus != null) {
            val performanceValue = calcPerformanceValue(info)
            val firstPerformanceType = selectFirstPerformanceType(info)
            val trainingUp = Status(
                speed = calcLiveStatusSingle(info, StatusType.SPEED),
                stamina = calcLiveStatusSingle(info, StatusType.STAMINA),
                power = calcLiveStatusSingle(info, StatusType.POWER),
                guts = calcLiveStatusSingle(info, StatusType.GUTS),
                wisdom = calcLiveStatusSingle(info, StatusType.WISDOM),
                skillPt = calcLiveStatusSingle(info, StatusType.SKILL),
                performance = firstPerformanceType.asPerformance(performanceValue),
            )
            val friendUp = if (friendTraining) {
                val calc = base + trainingUp
                val secondPerformanceType = selectSecondPerformanceType(info, firstPerformanceType)
                Status(
                    speed = calcTrainingUp(calc.speed, info.liveStatus.friendTrainingUp),
                    stamina = calcTrainingUp(calc.stamina, info.liveStatus.friendTrainingUp),
                    power = calcTrainingUp(calc.power, info.liveStatus.friendTrainingUp),
                    guts = calcTrainingUp(calc.guts, info.liveStatus.friendTrainingUp),
                    wisdom = calcTrainingUp(calc.wisdom, info.liveStatus.friendTrainingUp),
                    skillPt = calcTrainingUp(calc.skillPt, info.liveStatus.friendTrainingUp),
                    performance = secondPerformanceType.asPerformance(performanceValue),
                )
            } else Status()
            trainingUp + friendUp
        } else Status()
    }

    private fun calcLiveStatusSingle(
        info: Calculator.CalcInfo,
        target: StatusType,
    ) = if (upInTraining(info.training.type, target)) info.liveStatus?.trainingUp(target) ?: 0 else 0

    private fun calcTrainingUp(value: Int, up: Int) = ((value * up) + 50) / 100

    fun calcPerformanceValue(
        info: Calculator.CalcInfo,
    ): Int {
        val base = if (info.training.type == StatusType.WISDOM) 5 else 9
        val link = 2 * info.member.count { !it.guest && info.scenario.scenarioLink.contains(it.charaName) }
        return (base + info.training.displayLevel) * when (info.member.size) {
            1 -> 11
            2 -> 13
            3 -> 15
            4 -> 17
            5 -> 20
            else -> 10
        } / 10 + link
    }

    private fun selectFirstPerformanceType(
        info: Calculator.CalcInfo,
    ): PerformanceType {
        return randomSelect(firstPerformanceRate[info.training.type]!!)
    }

    private fun selectSecondPerformanceType(
        info: Calculator.CalcInfo,
        firstType: PerformanceType,
    ): PerformanceType {
        val minimumType = info.currentStatus.performance?.minimumType ?: PerformanceType.entries
        return if (!minimumType.contains(firstType) && Random.nextInt(100) >= 85) {
            minimumType.random()
        } else {
            return randomSelect(firstPerformanceRate[info.training.type]!!.filterNot { it.first == firstType })
        }
    }
}