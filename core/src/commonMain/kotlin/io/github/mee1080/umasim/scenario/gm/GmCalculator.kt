package io.github.mee1080.umasim.scenario.gm

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.applyIf

object GmCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        return calcGmStatus(info, base)
    }

    private fun calcGmStatus(
        info: Calculator.CalcInfo,
        base: Status
    ): Status {
        val gmStatus = info.gmStatus ?: return Status()
        return Status(
            speed = calcGmStatusSingle(gmStatus, StatusType.SPEED, base.speed),
            stamina = calcGmStatusSingle(gmStatus, StatusType.STAMINA, base.stamina),
            power = calcGmStatusSingle(gmStatus, StatusType.POWER, base.power),
            guts = calcGmStatusSingle(gmStatus, StatusType.GUTS, base.guts),
            wisdom = calcGmStatusSingle(gmStatus, StatusType.WISDOM, base.wisdom),
            skillPt = calcGmStatusSingle(gmStatus, StatusType.SKILL, base.skillPt),
            hp = -(base.hp * gmStatus.wisdomHpCost / 100.0).toInt()
        ) + calcGmStatusHint(info.member, gmStatus)
    }

    private fun calcGmStatusSingle(
        gmStatus: GmStatus,
        target: StatusType,
        baseValue: Int,
    ): Int {
        if (baseValue == 0) return 0
        val bonus = gmStatus.getStatusBonus(target)
        val factor = ((baseValue + bonus) * gmStatus.wisdomTrainingFactor / 100.0).toInt()
        return bonus + factor
    }

    private fun calcGmStatusHint(
        member: List<MemberState>,
        gmStatus: GmStatus,
    ): Status {
        if (!gmStatus.hintFrequencyUp) return Status()
        val hintSupportList = member.filter { !it.outingType }
        val hintStatus = hintSupportList.map { hintSupport ->
            val base = hintSupport.card.hintStatus
            // ヒント全潰しは無視
            if (hintSupport.card.skills.isNotEmpty()) base else base + base
        }.fold(Status()) { acc, status ->
            acc + status
        }
        return hintStatus + Status(
            speed = if (hintStatus.speed > 0) gmStatus.getStatusBonus(StatusType.SPEED) else 0,
            stamina = if (hintStatus.stamina > 0) gmStatus.getStatusBonus(StatusType.STAMINA) else 0,
            power = if (hintStatus.power > 0) gmStatus.getStatusBonus(StatusType.POWER) else 0,
            guts = if (hintStatus.guts > 0) gmStatus.getStatusBonus(StatusType.GUTS) else 0,
            wisdom = if (hintStatus.wisdom > 0) gmStatus.getStatusBonus(StatusType.WISDOM) else 0,
            skillPt = hintSupportList.size * 20,
        )
    }

    override fun applyScenarioRaceBonus(state: SimulationState, base: Status): Status {
        var status = base
        if (state.gmStatus?.activeWisdom == Founder.Red) {
            status = status.copy(
                speed = (status.speed * (1 + 35 / 100.0)).toInt(),
                stamina = (status.stamina * (1 + 35 / 100.0)).toInt(),
                power = (status.power * (1 + 35 / 100.0)).toInt(),
                guts = (status.guts * (1 + 35 / 100.0)).toInt(),
                wisdom = (status.wisdom * (1 + 35 / 100.0)).toInt(),
            )
        }
        return status
    }

    override fun calcBaseRaceStatus(state: SimulationState, race: RaceEntry, goal: Boolean): Status? {
        return if (race.grade == RaceGrade.FINALS) {
            state.raceStatus(5, 20, 80)
        } else null
    }

    override fun predictScenarioActionParams(state: SimulationState, baseActions: List<Action>): List<Action> {
        val gmStatus = state.gmStatus ?: return baseActions
        return if (baseActions.size == 1) {
            baseActions.map {
                (it as Race).copy(
                    result = it.result.addScenarioActionParam(
                        GmActionParam(
                            Founder.entries.random(), trainingTypeOrSkill.random(), predictKnowledgeCount(state, 1.0),
                        )
                    ),
                )
            }
        } else {
            val trainingFounders = (Founder.entries + Founder.entries).shuffled()
            val sleepFounders = Founder.entries.shuffled()
            val raceKnowledgeType = trainingTypeOrSkill.random()
            baseActions.map {
                when (it) {
                    is Training -> {
                        val knowledgeTypeRate = gmTrainingKnowledgeType[it.type]!!
                        val doubleRate = when {
                            !it.friendTraining -> 0.0
                            it.type == StatusType.WISDOM -> 0.45
                            else -> 1.0
                        }
                        it.copy(
                            candidates = it.addScenarioActionParam(
                                GmActionParam(
                                    trainingFounders[it.type.ordinal],
                                    randomSelectDouble(knowledgeTypeRate),
                                    predictKnowledgeCount(state, doubleRate),
                                ).applyIf(it.support.any { support -> support.charaName == "ダーレーアラビアン" }) {
                                    val eventRate =
                                        if (it.support.first { support -> support.charaName == "ダーレーアラビアン" }.supportState?.passion == true) {
                                            1.0
                                        } else {
                                            0.4 * (100 + gmStatus.wisdomHintFrequency) / 100.0
                                        }
                                    copy(knowledgeEventRate = eventRate)
                                }
                            ),
                        )
                    }

                    is Sleep -> it.copy(
                        candidates = it.addScenarioActionParam(
                            GmActionParam(
                                sleepFounders[0], trainingTypeOrSkill.random(), predictKnowledgeCount(state, 0.2)
                            )
                        ),
                    )

                    is Outing -> it.copy(
                        candidates = it.addScenarioActionParam(
                            GmActionParam(
                                sleepFounders[1], trainingTypeOrSkill.random(), predictKnowledgeCount(state, 0.2)
                            )
                        ),
                    )

                    is Race -> it.copy(
                        result = it.result.addScenarioActionParam(
                            GmActionParam(
                                sleepFounders[2], raceKnowledgeType, predictKnowledgeCount(state, 0.2)
                            ),
                        )
                    )

                    else -> it
                }
            }
        }
    }

    private fun predictKnowledgeCount(state: SimulationState, doubleRate: Double): Int {
        return when (state.gmStatus?.knowledgeFragmentCount) {
            8, null -> 0
            7 -> 1
            else -> randomSelectPercent(doubleRate, 2, 1)
        }
    }

    private val gmTrainingKnowledgeType by lazy {
        trainingType.associateWith { training ->
            if (training == StatusType.GUTS) {
                trainingTypeOrSkill.map {
                    it to if (upInTraining(training, it)) 0.85 / 4 else 0.15 / 2
                }
            } else {
                trainingTypeOrSkill.map {
                    it to if (upInTraining(training, it)) 0.8 / 3 else 0.2 / 3
                }
            }
        }
    }

    override fun getHintFrequencyUp(
        state: SimulationState,
        position: StatusType
    ): Int {
        return state.gmStatus?.wisdomHintFrequency ?: 0
    }

    override fun isAllSupportHint(
        state: SimulationState,
        position: StatusType
    ): Boolean {
        return state.gmStatus?.hintFrequencyUp ?: false
    }
}