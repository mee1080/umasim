package io.github.mee1080.umasim.scenario.aoharu

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.TeamMemberData
import io.github.mee1080.umasim.scenario.CommonScenarioEvents
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.max
import kotlin.math.min

class AoharuScenarioEvents : CommonScenarioEvents() {

    override fun onTurnEnd(state: SimulationState): SimulationState {
        val newState = super.onTurnEnd(state)
        return when (newState.turn) {
            3 -> newState
                .addLinkMember()
                .updateTrainingLevel()

            18 -> newState
                .addStatus(Status(motivation = 1))
                .addMember(9 - newState.teamMember.size)
                .updateTrainingLevel()

            24 -> newState
                .updateTrainingLevel()
                .applyRace("E")
                .addMember((3..min(4, 13 - newState.teamMember.size)).random())

            36 -> newState
                .updateTrainingLevel()
                .applyRace("D")
                .addMember((max(2, 15 - newState.teamMember.size)..min(4, 16 - newState.teamMember.size)).random())

            48 -> newState
                .updateTrainingLevel()
                .applyRace("B")
                .addMember(19 - newState.teamMember.size)

            60 -> newState
                .updateTrainingLevel()
                .applyRace("A")

            70 -> newState
                // TODO アオハルヒント、爆発10で+20
                .addStatus(Status(skillPt = 15))
                .updateTrainingLevel()

            72 -> newState
                .updateTrainingLevel()
                .applyRace("S")

            78 -> newState
                .addStatus(newState.raceStatus(0, 0, 50))

            else -> newState
                .updateTrainingLevel()
        }
    }

    private fun SimulationState.addLinkMember(): SimulationState {
        val current = member.map { it.charaName }
        val newMember = member.toMutableList()
        scenario.scenarioLink.filter { !current.contains(it) }.forEach {
            newMember.add(createGuest(newMember.size, Store.Aoharu.getGuest(it)!!))
        }
        return copy(member = newMember)
    }

    private fun SimulationState.addMember(count: Int): SimulationState {
        if (count <= 0) return this
        val current = member.map { it.charaName }
        val newMember = member.toMutableList()
        val rank = teamStatusRank
        // TODO 適性による優先順位をつける
        Store.Aoharu.getShuffledGuest().filter { !current.contains(it.chara) }.subList(0, count).forEach {
            newMember.add(createGuest(newMember.size, it, rank))
        }
        return copy(member = newMember)
    }

    private fun SimulationState.applyRace(opponentRank: String): SimulationState {
        // 勝ち前提
        return addStatus(
            when (opponentRank) {
                "S" -> raceStatus(5, 7, 50)
                "A" -> raceStatus(5, 5, 25)
                "B" -> raceStatus(5, 4, 20)
                "C" -> raceStatus(5, 4, 15)
                "D" -> raceStatus(5, 3, 15)
                "E" -> raceStatus(5, 3, 10)
                "F" -> raceStatus(5, 2, 10)
                else -> raceStatus(5, 1, 10)
            }
        ).copy(
            member = teamMember.map {
                it.addStatus(Status(50, 50, 50, 50, 50))
            }
        )
    }

    private fun MemberState.addStatus(status: Status): MemberState {
        val scenarioState = scenarioState as AoharuMemberState
        return copy(
            scenarioState = scenarioState.copy(
                status = (scenarioState.status + status).adjustRange(scenarioState.maxStatus)
            )
        )
    }

    private fun createGuest(
        index: Int,
        member: TeamMemberData,
        rank: Map<StatusType, AoharuTeamStatusRank>? = null
    ): MemberState {
        val card = Store.getSupport(member.supportCardId, 0)
        return MemberState(
            index = index,
            card = card,
            position = StatusType.NONE,
            supportState = null,
            scenarioState = AoharuMemberState(
                member = member,
                status = member.initialStatus + (rank?.let { calcStatusBonus(it) } ?: Status()),
                maxStatus = member.maxStatus,
                aoharuTrainingCount = 0,
                aoharuIcon = false,
            )
        )
    }

    private fun calcStatusBonus(rank: Map<StatusType, AoharuTeamStatusRank>) = Status(
        rank[StatusType.SPEED]!!.getRandomStatus(),
        rank[StatusType.STAMINA]!!.getRandomStatus(),
        rank[StatusType.POWER]!!.getRandomStatus(),
        rank[StatusType.GUTS]!!.getRandomStatus(),
        rank[StatusType.WISDOM]!!.getRandomStatus(),
    )

    private fun SimulationState.updateTrainingLevel(): SimulationState {
        val statusRank = teamStatusRank
        return copy(training = training.map {
            it.copy(level = statusRank[it.type]!!.trainingLevel)
        })
    }
}
