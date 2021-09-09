package io.github.mee1080.umasim.simulation

import io.github.mee1080.umasim.data.*
import kotlin.random.Random

class TeamMember(
    val data: TeamMemberData,
    val guest: Boolean,
) {
    var status = data.initialStatus

    var aoharuCount = 0

    var aoharu = false

    val aoharuBurn get() = aoharu && aoharuCount == 4

    fun onTurnChange() {
        aoharu = Random.nextDouble() < 0.4
    }

    fun onTraining(training: AoharuTrainingTeam, chara: Chara) {
        if (aoharu) {
            status += if (aoharuBurn) {
                Store.Aoharu.getBurnTeam(training.type).getStatus(chara)
            } else {
                training.getRandomStatus(chara)
            }
            aoharuCount++
        }
    }

    fun addStatus(status: Status) {
        this.status += status
    }
}