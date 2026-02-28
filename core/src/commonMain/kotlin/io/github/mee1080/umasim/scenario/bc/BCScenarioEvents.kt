package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.bc.BCCalculator.addRandomDreamGauge
import io.github.mee1080.umasim.simulation2.*

class BCScenarioEvents(private val route: BCRoute = BCRoute.Classic) : BaseScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return super.beforeSimulation(state).copy(
            goalRace = listOf(state.goalRace.first(), *route.goalRace.toTypedArray()),
        ).joinTeamMembers()
    }

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.beforeAction(state, selector)
        return when (base.turn) {

            3 -> base
                .selectTeamParameter(selector, 3, dpInitial)

            25 -> base
                .addAllStatus(status = 6, skillPt = 40)

            41 -> base
                .addAllStatus(status = 10, skillPt = 60)

            49 -> base
                .addAllStatus(status = 10, skillPt = 40)

            59 -> base
                .addAllStatus(status = 25, skillPt = 60)

            37, 38, 39, 40, 61, 62, 63, 64 -> base
                .updateBCStatus { addRandomDreamGauge() }

            else -> base
        }
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {

            12 -> base
                .addAllStatus(status = 3, skillPt = 15, hp = 15, motivation = 1)
                .addAllSupportHint()
                .selectTeamParameter(selector, 3, dpMeeting)

            16 -> base
                // 本来はチームランクE達成時に発生だけど結果に影響がないので適当なターンに発生扱い
                .addStatus(Status(speed = 10, skillPt = 10))

            24 -> base
                .addAllStatus(status = 5, skillPt = 25, hp = 15, motivation = 1)
                .addAllSupportHint()
                .allTrainingLevelUp()
                .selectTeamParameter(selector, 5, dpMeeting)

            36 -> base
                .addAllStatus(status = 10, skillPt = 30, hp = 15, motivation = 1)
                .addAllSupportHint()
                .selectTeamParameter(selector, 5, dpMeeting)

            48 -> base
                .addAllStatus(status = 15, skillPt = 60, hp = 15, motivation = 1, skillHint = mapOf("全身全霊" to 1))
                .addAllSupportHint()
                .allTrainingLevelUp()
                .selectTeamParameter(selector, 8, dpMeeting)

            60 -> base
                .addAllStatus(status = 30, skillPt = 110, hp = 15, motivation = 1)
                .addAllSupportHint()
                .selectTeamParameter(selector, 8, dpMeeting, 4)

            else -> base
        }
    }

    private fun SimulationState.joinTeamMembers(): SimulationState {
        val supportMemberNames = support
            .filter { !it.outingType && Scenario.BC.scenarioLink.contains(it.charaName) }
            .shuffled().take(3).map { it.charaName }
        val guestMembernames = if (supportMemberNames.size == 3) emptyList() else Scenario.BC.scenarioLink
            .filter { it != "カジノドライヴ" && !supportMemberNames.contains(it) }
            .shuffled().take(3 - supportMemberNames.size)
        val casinoRarity = support.firstOrNull { it.charaName == "カジノドライヴ" }?.card?.rarity ?: 0
        val bcStatus = BCStatus(
            teamMember = (supportMemberNames + guestMembernames).map { BCTeamMember(it) },
            casinoRarity = casinoRarity,
        )
        return copy(scenarioStatus = bcStatus)
    }

    private suspend fun SimulationState.selectTeamParameter(
        selector: ActionSelector,
        maxLevel: Int,
        dreamsPointData: List<Int>,
        dreamsTrainingCount: Int = 2,
    ): SimulationState {
        var state = updateBCStatus {
            copy(
                dreamsPoint = dreamsPoint + dreamsPointData[casinoRarity],
                dreamsTrainingCount = dreamsTrainingCount,
            )
        }
        while ((state.bcStatus?.dreamsPoint ?: 0) >= 5) {
            val bcStatus = state.bcStatus ?: return state
            val actions = BCTeamParameter.entries
                .filter { bcStatus.teamParameter[it]!! < maxLevel }
                .map { BCTeamParameterUp(it) }
            if (actions.isEmpty()) break
            val action = selector.select(state, actions)
            state = BCCalculator.applyScenarioAction(state, (action as BCTeamParameterUp).result)
        }
        return state
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        return super.afterSimulation(state)
            .addAllStatus(status = 40, skillPt = 100, skillHint = mapOf(route.skill to 2, "情熱と挑戦の先の栄光" to 1))
    }

    override fun beforePredict(state: SimulationState): SimulationState {
        return state.updateBCStatus {
            // シナリオリンクサポカが配置されている場合、チームメンバーをその位置に移動
            copy(teamMember = teamMember.map { member ->
                val supportPosition = state.member.firstOrNull { it.charaName == member.charaName }?.position
                if (supportPosition != null && supportPosition != StatusType.NONE) {
                    member.copy(position = supportPosition)
                } else member
            })
        }
    }
}
