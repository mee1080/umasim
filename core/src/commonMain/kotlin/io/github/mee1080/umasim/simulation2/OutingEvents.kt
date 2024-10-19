package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.scenario.cook.CookMaterial
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.randomSelect
import io.github.mee1080.umasim.scenario.cook.updateCookStatus
import io.github.mee1080.utility.applyIf
import kotlin.random.Random

fun SimulationState.applyAfterTrainingEvent(target: MemberState): SimulationState {
    val isFirst = target.supportState?.outingStep == 0
    return when (target.charaName) {
        "都留岐涼花" -> {
            if (isFirst) {
                applyFriendEvent(target, Status(speed = 5, wisdom = 5, motivation = 1), 10, 1)
            } else if (Random.nextDouble() < 0.4) {
                if (Random.nextDouble() < 0.1) {
                    applyFriendEvent(target, Status(hp = 7, motivation = 1), 5)
                } else {
                    applyFriendEvent(target, Status(hp = 7), 5)
                }
            } else this
        }

        "秋川理事長" -> {
            if (isFirst) {
                applyFriendEvent(target, Status(speed = 14), 10, 1)
            } else if (Random.nextDouble() < 0.4) {
                when {
                    turn <= 24 -> {
                        val relationTarget = support.minBy { it.relation }
                        addRelation(if (charm) 7 else 5, target)
                            .addRelation(if (charm) 5 else 3, relationTarget)
                    }

                    turn <= 48 -> {
                        applyFriendEvent(target, Status(speed = 12), 5)
                    }

                    else -> {
                        applyFriendEvent(target, Status(guts = 15), 5)
                    }
                }.applyIf(Random.nextDouble() < 0.1) {
                    addStatus(Status(motivation = 1))
                }
            } else this
        }

        // TODO 他の友人のイベント
        else -> this
    }
}

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

        "秋川理事長" -> {
            when (step) {
                1 -> applyFriendEvent(support, Status(hp = 25, motivation = 1), 5, 2)
                    .copy(refreshTurn = refreshTurn + selectRefreshTurn())

                2 -> applyFriendEvent(support, Status(guts = 20, hp = 30, motivation = 1), 5, 3)
                    .updateCookStatus { addMaterials(CookMaterial.entries.associateWith { 40 }) }

                3 -> applyFriendEvent(support, Status(speed = 10, guts = 10, hp = 30, motivation = 1), 5, 4)
                    .copy(refreshTurn = refreshTurn + selectRefreshTurn())
                    .updateCookStatus { addMaterials(CookMaterial.entries.associateWith { 40 }) }

                4 -> applyFriendEvent(support, Status(hp = 43, motivation = 1), 5, 5)
                    .updateCookStatus { addMaterials(CookMaterial.entries.associateWith { 40 }) }

                5 -> applyFriendEvent(support, Status(guts = 25, hp = 30, motivation = 1), 5, 6)
                    .updateCookStatus { addMaterials(CookMaterial.entries.associateWith { 40 }) }

                6 -> applyFriendEvent(
                    support, Status(guts = 36, hp = 30, motivation = 1, skillHint = mapOf("ウママニア" to 1)),
                    5, 7,
                )
                    .copy(refreshTurn = refreshTurn + selectRefreshTurn())
                    .updateCookStatus { addMaterials(CookMaterial.entries.associateWith { 40 }) }

                else -> this
            }
        }

        else -> this
    }
}

private fun selectRefreshTurn(): Int {
    return randomSelect(
        3 to 45,
        4 to 30,
        5 to 15,
        6 to 10,
    )
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

            "秋川理事長" -> state.applyFriendEvent(
                support, Status(speed = 24, motivation = 1, skillHint = mapOf("中盤巧者" to 3)),
                5, 0,
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

            "秋川理事長" -> state.applyFriendEvent(
                support, Status(speed = 20, guts = 20, skillPt = 56),
                0, 0,
            )

            else -> this
        }
    }
}