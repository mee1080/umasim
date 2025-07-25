package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.mujinto.MujintoCalculator
import io.github.mee1080.umasim.scenario.mujinto.MujintoStatus
import io.github.mee1080.umasim.scenario.mujinto.mujintoFacility
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.MemberState
import io.github.mee1080.umasim.simulation2.createTeamMemberState
import io.github.mee1080.utility.replaced

val mujintoFacilityType = (trainingType + StatusType.FRIEND).toList()

val mujintoPositionSelection = mujintoFacilityType + StatusType.NONE

data class MujintoState(
    val supportPosition: List<StatusType> = List(6) { StatusType.NONE },
    val guestCount: Map<StatusType, Int> = mujintoFacilityType.associateWith { 0 },
    val facilityLevel: Map<StatusType, Int> = mujintoFacilityType.associateWith { 0 },
    val facilityJukuren: Map<StatusType, Boolean> = trainingType.associateWith { false },
    val baseTrainingResult: Status = Status(),
    val rawTrainingResult: ExpectedStatus = ExpectedStatus(),
    val additionalTrainingResult: Status = Status(),
) {
    fun calcIslandTrainingStatus(
        supportList: List<MemberState>,
        baseCalcInfo: Calculator.CalcInfo,
    ): Triple<Status, ExpectedStatus, Status> {
        var info = baseCalcInfo.copy(
            member = emptyList(),
        )
        mujintoFacilityType.forEach { type ->
            if ((facilityLevel[type] ?: 0) == 0) return@forEach
            info = info.copy(
                member = info.member + supportList.filterIndexed { index, _ -> supportPosition[index] == type }.map {
                    it.copy(position = type)
                } + createTeamMemberState(guestCount[type] ?: 0, Scenario.MUJINTO).map {
                    it.copy(position = type)
                },
            )
        }
        val result = MujintoCalculator.calcIslandTrainingStatusSeparated(info)
        return Triple(result.first.first, result.first.second, result.second)
    }

    fun toMujintoStatus() = MujintoStatus(
        facilities = buildMap {
            facilityLevel.forEach { (type, level) ->
                if (level == 0) return@forEach
                val jukuren = (facilityJukuren[type] == true) && level >= 3 && type != StatusType.FRIEND
                put(type, mujintoFacility(type, level, jukuren))
            }
        },
    )

    fun calcAndUpdateTrainingResult(
        supportList: List<MemberState>,
        baseCalcInfo: Calculator.CalcInfo,
    ): MujintoState {
        val (base, expected, additional) = calcIslandTrainingStatus(supportList, baseCalcInfo)
        return copy(
            baseTrainingResult = base,
            rawTrainingResult = expected,
            additionalTrainingResult = additional,
        )
    }

    fun updatePosition(index: Int, position: StatusType) = copy(
        supportPosition = supportPosition.replaced(index, position)
    )

    fun updateGuestCount(type: StatusType, count: Int) = copy(
        guestCount = guestCount.replaced(type, count),
    )

    fun updateFacilityLevel(type: StatusType, level: Int) = copy(
        facilityLevel = facilityLevel.replaced(type, level),
    )

    fun updateFacilityJukuren(type: StatusType, jukuren: Boolean) = copy(
        facilityJukuren = facilityJukuren.replaced(type, jukuren),
    )
}
