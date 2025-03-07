package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.randomSelect
import io.github.mee1080.umasim.scenario.cook.CookMaterial
import io.github.mee1080.umasim.scenario.cook.updateCookStatus
import io.github.mee1080.umasim.scenario.legend.LegendMember
import io.github.mee1080.umasim.scenario.legend.addBuffGauge
import io.github.mee1080.umasim.scenario.legend.updateLegendStatus
import io.github.mee1080.utility.applyIf
import kotlin.random.Random

suspend fun SimulationState.applyAfterTrainingEvent(target: MemberState, selector: ActionSelector): SimulationState {
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
                        addRelation(5, target)
                            .addRelation(3, relationTarget)
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

        "スピードシンボリ" -> {
            if (isFirst) {
                applyFriendEvent(target, Status(6, 6, 6, 6, 6), 10, 1)
                    .updateLegendStatus { addBuffGauge(3) }
            } else if ((target.supportState?.passionTurn ?: 0) > 0 || Random.nextDouble() < 0.4) {
                val selection = listOf(
                    FriendAction(
                        LegendMember.Blue.displayName, FriendActionResult(
                            status, target, Status(speed = 6), 5,
                            scenarioActionParam = LegendActionParam(LegendMember.Blue, 1),
                        )
                    ),
                    FriendAction(
                        LegendMember.Green.displayName, FriendActionResult(
                            status, target, Status(skillPt = 3, hp = 3), 5,
                            scenarioActionParam = LegendActionParam(LegendMember.Green, 1),
                        )
                    ),
                    FriendAction(
                        LegendMember.Red.displayName, FriendActionResult(
                            status, target, Status(wisdom = 3), 5, 1,
                            scenarioActionParam = LegendActionParam(LegendMember.Red, 1),
                        )
                    ),
                )
                val selected = selector.select(this, selection)
                applyAction(selected, selected.randomSelectResult()).applyIf(target.supportState?.passion == false) {
                    startPassion(target)
                }
            } else this
        }

        // TODO 他の友人のイベント
        else -> this
    }
}

suspend fun SimulationState.applyOutingEvent(support: MemberState, selector: ActionSelector): SimulationState {
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

        "スピードシンボリ" -> {
            when (step) {
                1 -> applyFriendEvent(support, Status(hp = 35, skillPt = 10, skillHint = mapOf("中盤巧者" to 3)), 5, 2)
                    .updateLegendStatus { addBuffGauge(3) }
                    .startPassion(support)

                // スピードシンボリ（TODO 本来は順番選択可能）
                2 -> applyFriendEvent(
                    support,
                    Status(
                        stamina = 15, power = 10, guts = 10,
                        skillPt = 20, hp = 45, maxHp = 4, motivation = 1,
                    ),
                    5, 3
                ).updateLegendStatus { addBuffGauge(3) }

                // セントライト
                3 -> applyFriendEvent(
                    support,
                    Status(
                        speed = 10, stamina = 10, power = 10, guts = 10, wisdom = 10,
                        skillPt = 25, hp = 35, motivation = 1,
                    ),
                    5, 4,
                ).updateLegendStatus { addBuffGauge(3) }

                // ハイセイコー
                4 -> applyFriendEvent(
                    support,
                    Status(
                        speed = 15, wisdom = 10,
                        skillPt = 30, hp = 45, motivation = 1,
                    ),
                    5, 5,
                ).updateLegendStatus { addBuffGauge(3) }

                5 -> {
                    val selection = listOf(
                        FriendAction(
                            LegendMember.Blue.displayName, FriendActionResult(
                                status, support,
                                Status(
                                    speed = 5, stamina = 5, power = 5, guts = 5, wisdom = 5,
                                    hp = 45, motivation = 1, skillHint = mapOf("尻尾上がり" to 2),
                                ),
                                5,
                                scenarioActionParam = LegendActionParam(LegendMember.Blue, 8),
                                outingStep = 6,
                            )
                        ),
                        FriendAction(
                            LegendMember.Green.displayName, FriendActionResult(
                                status, support,
                                Status(
                                    stamina = 15, power = 10,
                                    hp = 45, motivation = 1, skillHint = mapOf("闘争心" to 2),
                                ),
                                5,
                                scenarioActionParam = LegendActionParam(LegendMember.Green, 8),
                                outingStep = 6,
                            )
                        ),
                        FriendAction(
                            LegendMember.Red.displayName, FriendActionResult(
                                status, support,
                                Status(
                                    speed = 10, guts = 15,
                                    hp = 45, motivation = 1, skillHint = mapOf("地固め" to 2),
                                ),
                                5,
                                scenarioActionParam = LegendActionParam(LegendMember.Red, 8),
                                outingStep = 6,
                            )
                        ),
                    )
                    val selected = selector.select(this, selection)
                    applyAction(
                        selected,
                        selected.randomSelectResult()
                    )
                }

                6 -> applyFriendEvent(
                    support,
                    Status(
                        speed = 8, stamina = 8, power = 8, guts = 8, wisdom = 8,
                        skillPt = 30, hp = 50, motivation = 1, skillHint = mapOf("英俊豪傑" to 3),
                    ),
                    5, 7,
                ).updateLegendStatus { addBuffGauge(3) }.startPassion(support)

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
        scenarioStatus = uafStatus.copy(
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

            "スピードシンボリ" -> state.applyFriendEvent(
                support,
                Status(
                    speed = 4, stamina = 4, power = 4, guts = 4, wisdom = 4,
                    skillPt = 5, maxHp = 4, motivation = 1, skillHint = mapOf("比類なき" to 3),
                ),
                5, 0,
            ).updateLegendStatus { addBuffGauge(2) }

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

            "スピードシンボリ" -> state.applyFriendEvent(
                support,
                Status(speed = 14, stamina = 14, power = 14, guts = 14, wisdom = 14, skillPt = 14),
                0, 0,
            )

            else -> this
        }
    }
}