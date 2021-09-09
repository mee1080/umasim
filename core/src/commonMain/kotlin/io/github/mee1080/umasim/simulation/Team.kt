package io.github.mee1080.umasim.simulation

import io.github.mee1080.umasim.data.*

class Team(
    cardList: List<SupportCard>
) {
    val memberList = cardList
        .mapNotNull { Store.Aoharu.getTeamMember(it.id) }
        .map { TeamMember(it, false) }
        .toMutableList()

    val memberCount get() = memberList.size

    fun addGuest(count: Int, charaStatus: Status?) {
        if (count <= 0) return
        val total = memberCount + count
        val rank = charaStatus?.let { statusRank(it) }
        Store.Aoharu.getShuffledGuest().forEach { guest ->
            if (!memberList.any { it.data.chara == guest.chara }) {
                addGuestInternal(guest, rank)
                if (memberCount >= total) return@forEach
            }
        }
    }

    fun addGuest(guest: TeamMemberData, charaStatus: Status? = null) {
        addGuestInternal(guest, charaStatus?.let { statusRank(it) })
    }

    private fun addGuestInternal(guest: TeamMemberData, rank: Map<StatusType, AoharuTeamStatusRank>?) {
        val member = TeamMember(guest, true)
        if (rank != null) {
            member.addStatus(calcStatusBonus(rank))
        }
        memberList.add(member)
    }

    private fun calcStatusBonus(rank: Map<StatusType, AoharuTeamStatusRank>) = Status(
        rank[StatusType.SPEED]!!.getRandomStatus(),
        rank[StatusType.STAMINA]!!.getRandomStatus(),
        rank[StatusType.POWER]!!.getRandomStatus(),
        rank[StatusType.GUTS]!!.getRandomStatus(),
        rank[StatusType.WISDOM]!!.getRandomStatus(),
    )

    fun totalStatus(charaStatus: Status) = memberList
        .fold(charaStatus) { acc, teamMember -> acc + teamMember.status }

    fun averageStatus(charaStatus: Status): ExpectedStatus {
        val total = totalStatus(charaStatus)
        return ExpectedStatus(
            total.speed.toDouble() / memberCount,
            total.stamina.toDouble() / memberCount,
            total.power.toDouble() / memberCount,
            total.guts.toDouble() / memberCount,
            total.wisdom.toDouble() / memberCount,
        )
    }

    fun statusRank(charaStatus: Status): Map<StatusType, AoharuTeamStatusRank> {
        val average = averageStatus(charaStatus)
        return trainingType.associateWith { type ->
            Store.Aoharu.teamStatusRank.values.first {
                average.get(type) >= it.threshold
            }
        }
    }

    fun addRaceBonus(value: Int) {
        memberList.forEach { it.addStatus(Status(value, value, value, value, value)) }
    }

    fun onTurnChange() {
        memberList.forEach { it.onTurnChange() }
    }
}