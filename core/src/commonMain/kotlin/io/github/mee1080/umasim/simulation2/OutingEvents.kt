package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status

fun SimulationState.applyOutingEvent(support: MemberState): SimulationState {
    val step = support.supportState?.outingStep ?: return this
    return when (support.charaName) {
        "都留岐涼花" -> {
            when (step) {
                1 -> applyFriendEvent(support, Status(hp = 20, maxHp = 4, motivation = 1), 5, 2)

                2 -> applyFriendEvent(support, Status(speed = 15, hp = 35, motivation = 1), 5, 3)
                    .uafAthleticsLevelUp()

                3 -> applyFriendEvent(support, Status(speed = 10, wisdom = 10, hp = 30, motivation = 1), 5, 4)
                    .uafAthleticsLevelUp()

                4 -> applyFriendEvent(support, Status(hp = 50, motivation = 1), 7, 5)
                    .uafAthleticsLevelUp()

                5 -> applyFriendEvent(support, Status(speed = 25, hp = 30, motivation = 1), 5, 6)
                    .uafAthleticsLevelUp()

                6 -> applyFriendEvent(
                    support, Status(speed = 15, hp = 35, motivation = 1, skillHint = mapOf("機先の勝負" to 1)),
                    5, 7,
                ).uafAthleticsLevelUp()

                else -> this
            }
        }

        else -> this
    }
}

private fun SimulationState.uafAthleticsLevelUp(): SimulationState {
    val uafStatus = uafStatus ?: return this
    return copy(
        uafStatus = uafStatus.copy(
            athleticsLevel = uafStatus.athleticsLevel.mapValues { it.value + 1 }
        ).applyHeatUpFrom(uafStatus)
    )
}

fun SimulationState.applyOutingNewYearEvent(): SimulationState {
    return support.filter { (it.supportState?.outingStep ?: 0) >= 2 }.fold(this) { state, support ->
        when (support.charaName) {
            "都留岐涼花" -> state.applyFriendEvent(
                support, Status(speed = 10, hp = 10, maxHp = 4, motivation = 1, skillHint = mapOf("末脚" to 3)),
                7, 0,
            )

            else -> this
        }
    }
}

fun SimulationState.applyOutingFinalEvent(): SimulationState {
    return support.filter { (it.supportState?.outingStep ?: 0) >= 2 }.fold(this) { state, support ->
        when (support.charaName) {
            "都留岐涼花" -> state.applyFriendEvent(
                support, Status(speed = 30, wisdom = 30, skillPt = 45),
                7, 0,
            )

            else -> this
        }
    }
}