package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*

class GrandLiveScenarioEvents : CommonScenarioEvents() {

    override fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {
            // 導入
            4 -> base.copy(
                liveStatus = LiveStatus(),
                status = state.status.copy(performance = Performance())
            ).addMember("[トレセン学園]スマートファルコン").purchaseLesson(specialSongs[0])

            // ジュニア9月：スマートファルコン編成時加入
            18 -> base.addMember(1)

            // グランドライブ楽曲決定
            71 -> base.addLesson(
                if (base.liveStatus!!.learnedSongs.size >= 18) specialSongs[2] else specialSongs[1]
            )

            // ライブ
            24 -> base.applyLive(selector)
                .addMember("[トレセン学園]サイレンススズカ")
                .addMember("[トレセン学園]アグネスタキオン")
                .addMember(6)

            36 -> base.applyLive(selector)
                .addMember("[トレセン学園]ミホノブルボン")
                .addMember(8)

            48 -> base.applyLive(selector).addMember(9)

            60 -> base.applyLive(selector).addMember(11)

            72 -> base.applyLive(selector)

            78 -> base.purchaseBeforeLive(selector)

            else -> base
        }
    }

    private fun SimulationState.addMember(name: String): SimulationState {
        val current = member.map { it.charaName }
        if (current.contains(name)) return this
        return copy(member = member + createGuest(member.size, Store.getSupportByName(name, 0)))
    }

    private fun SimulationState.addMember(count: Int): SimulationState {
        val guestCount = member.count { it.guest }
        if (guestCount >= count) return this
        val newMember = member.toMutableList()
        Store.GrandLive.getShuffledGuest().subList(0, count - guestCount).forEach {
            newMember += createGuest(newMember.size, it)
        }
        return copy(member = newMember)
    }

    private fun createGuest(index: Int, card: SupportCard): MemberState {
        return MemberState(
            index,
            card,
            StatusType.NONE,
            null,
            GrandLiveMemberState,
        )
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        val newState = super.afterSimulation(state)
        // 理事長絆最高
        return newState.updateStatus { it + Status(15, 15, 15, 15, 15, 50) }
    }
}
