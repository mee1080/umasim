package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import kotlin.math.max
import kotlin.math.min

object AoharuScenarioEvents {

    fun asScenarioEvents() = ScenarioEvents(
        onTurnEnd = { onTurnEnd(it) }
    )

    private fun onTurnEnd(state: SimulationState): SimulationState {
        return when (state.turn) {
            3 -> state
                .addLinkMember()
                .updateTrainingLevel()
            18 -> state
                .addMember(9 - state.teamMember.size)
                .updateTrainingLevel()
            24 -> state
                .updateTrainingLevel()
                .applyRace("E")
                .addMember((3..min(4, 13 - state.teamMember.size)).random())
            36 -> state
                .updateTrainingLevel()
                .applyRace("D")
                .addMember((max(2, 15 - state.teamMember.size)..min(4, 16 - state.teamMember.size)).random())
            48 -> state
                .updateTrainingLevel()
                .applyRace("B")
                .addMember(19 - state.teamMember.size)
            60 -> state
                .updateTrainingLevel()
                .applyRace("A")
            72 -> state
                .updateTrainingLevel()
                .applyRace("S")
            else -> state
                .updateTrainingLevel()
        }
    }

    private fun SimulationState.addLinkMember(): SimulationState {
        val current = member.map { it.charaName }
        val newMember = member.toMutableList()
        Store.getScenarioLink(scenario).filter { !current.contains(it) }.forEach {
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
        // TODO 育成キャラ能力上昇
        // 勝ち前提
        return copy(
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
