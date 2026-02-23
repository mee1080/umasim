package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.BaseScenarioEvents
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.bc.BCCalculator.addRandomDreamGauge
import io.github.mee1080.umasim.simulation2.ActionSelector
import io.github.mee1080.umasim.simulation2.BCTeamParameterUp
import io.github.mee1080.umasim.simulation2.SimulationState

class BCScenarioEvents(private val route: BCRoute = BCRoute.Classic) : BaseScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return super.beforeSimulation(state).copy(
            goalRace = listOf(state.goalRace.first(), *route.goalRace.toTypedArray()),
        )
    }

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.beforeAction(state, selector)
        return when (base.turn) {
            3 -> base
                .joinTeamMembers()
                .selectTeamParameter(selector, dpInitial)

            37, 38, 39, 40, 61, 62, 63, 64 -> base
                .updateBCStatus { addRandomDreamGauge() }

            else -> base
        }
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {

            12 -> base
                .selectTeamParameter(selector, dpMeeting)

            24 -> base
                .selectTeamParameter(selector, dpMeeting)

            36 -> base
                .selectTeamParameter(selector, dpMeeting)

            48 -> base
                .selectTeamParameter(selector, dpMeeting)

            60 -> base
                .selectTeamParameter(selector, dpMeeting, 4)

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
                .filter { bcStatus.teamParameter[it]!! < 8 }
                .map { BCTeamParameterUp(it) }
            if (actions.isEmpty()) break
            val action = selector.select(state, actions)
            state = BCCalculator.applyScenarioAction(state, (action as BCTeamParameterUp).result)
        }
        return state
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        // TODO: BCシナリオ固有の育成完了時イベント処理を実装する
        return super.afterSimulation(state)
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
