package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import kotlin.math.max
import kotlin.math.min

object LArcCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        return calcLArcStatus(info, base, friendTraining)
    }

    private fun calcLArcStatus(
        info: Calculator.CalcInfo,
        base: Status,
        friendTraining: Boolean,
    ): Status {
        val lArcStatus = info.lArcStatus ?: return Status()
        val trainingType = info.training.type
        val overseas = info.training.level == 6
        return Status(
            speed = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.SPEED,
                base.speed,
                overseas,
                friendTraining
            ),
            stamina = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.STAMINA,
                base.stamina,
                overseas,
                friendTraining
            ),
            power = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.POWER,
                base.power,
                overseas,
                friendTraining
            ),
            guts = calcLArcStatusSingle(lArcStatus, trainingType, StatusType.GUTS, base.guts, overseas, friendTraining),
            wisdom = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.WISDOM,
                base.wisdom,
                overseas,
                friendTraining
            ),
            skillPt = calcLArcStatusSingle(
                lArcStatus,
                trainingType,
                StatusType.SKILL,
                base.skillPt,
                overseas,
                friendTraining
            ),
            hp = -(base.hp * lArcStatus.hpCost(overseas) / 100.0).toInt()
        )
    }

    private fun calcLArcStatusSingle(
        lArcStatus: LArcStatus,
        trainingType: StatusType,
        target: StatusType,
        baseValue: Int,
        overseas: Boolean,
        friendTraining: Boolean,
    ): Int {
        if (baseValue == 0) return 0
        val bonus = lArcStatus.getStatusBonus(target)
        val factor = (baseValue + bonus) * lArcStatus.getTrainingFactor(trainingType, overseas) / 100.0
        val friend = if (friendTraining) {
            (baseValue + bonus + factor) * lArcStatus.friendFactor / 100.0
        } else 0.0
        return bonus + (factor + friend).toInt()
    }

    override fun calcBaseRaceStatus(state: SimulationState, race: RaceEntry, goal: Boolean): Status? {
        return if (race.courseName == "ロンシャン") when (state.turn) {
            41 -> state.raceStatus(5, 5, 40)
            43 -> state.raceStatus(5, 7, 50)
            65 -> state.raceStatus(5, 7, 40)
            67 -> if (state.lArcStatus!!.consecutiveVictories >= 2) {
                state.raceStatus(5, 30, 140)
            } else {
                state.raceStatus(5, 10, 60)
            }

            else -> throw IllegalArgumentException()
        } else null
    }

    override fun raceScenarioActionParam(state: SimulationState, race: RaceEntry, goal: Boolean): ScenarioActionParam? {
        return when {
            state.turn == 43 -> LArcActionParam(supporterPt = 3000)
            state.turn == 41 || state.turn == 65 -> LArcActionParam(supporterPt = 2000)
            state.turn == 67 -> null
            race.grade == RaceGrade.G1 -> LArcActionParam(supporterPt = 1300)
            race.grade == RaceGrade.G2 -> LArcActionParam(supporterPt = 900)
            race.grade == RaceGrade.G3 -> LArcActionParam(supporterPt = 700)
            else -> null
        }
    }

    override fun predictSleep(state: SimulationState): Array<Action>? {
        return if (state.isLevelUpTurn) {
            arrayOf(
                Sleep(
                    listOf(
                        StatusActionResult(Status(hp = 50, motivation = 1), LArcActionParam(aptitudePt = 100)) to 1
                    )
                )
            )
        } else null
    }

    override fun predictScenarioActionParams(state: SimulationState, baseActions: List<Action>): List<Action> {
        return baseActions.map {
            when (it) {
                is Training -> {
                    val param = if (state.isLevelUpTurn) LArcActionParam(
                        aptitudePt = 50 + it.member.size * 20 + it.friendCount * 20 - (if (it.type == StatusType.WISDOM) 20 else 0),
                        mayEventChance = it.member.any { member -> member.charaName == "佐岳メイ" },
                    ) else LArcActionParam(
                        mayEventChance = it.member.any { member -> member.charaName == "佐岳メイ" }
                    )
                    it.copy(candidates = it.addScenarioActionParam(param))
                }

                is Race -> {
                    val supporterPt = when (state.turn) {
                        41, 65 -> 2000
                        43 -> 3000
                        67 -> 0
                        else -> when (it.grade) {
                            RaceGrade.G1 -> 1300
                            RaceGrade.G2 -> 900
                            RaceGrade.G3 -> 700
                            else -> 0
                        }
                    }
                    it.copy(result = it.result.addScenarioActionParam(LArcActionParam(supporterPt = supporterPt)))
                }

                else -> it
            }
        }
    }

    override fun predictScenarioAction(state: SimulationState, goal: Boolean): Array<Action> {
        val lArcStatus = state.lArcStatus ?: return emptyArray()
        return if (goal) {
            lArcStatus.predictGetAptitude().toTypedArray()
        } else {
            state.predictSSMatch(lArcStatus) + lArcStatus.predictGetAptitude()
        }
    }

    private fun SimulationState.predictSSMatch(lArcStatus: LArcStatus): Array<Action> {
        if (isLevelUpTurn) return emptyArray()
        val joinMember = lArcStatus.ssMatchMember
        if (joinMember.isEmpty()) return emptyArray()
        val supporterRank = (listOf(-1 to lArcStatus.supporterPt) + member
            .filter { !it.outingType }
            .map { it.index to (it.scenarioState as LArcMemberState).supporterPt })
            .sortedByDescending { it.second }
            .mapIndexed { order, (index, _) -> index to order }
            .toMap()

        var status = Status()
        var lArcParam = LArcActionParam()
        joinMember.forEach {
            val memberState = it.scenarioState as LArcMemberState
            val effect = memberState.nextStarEffect[0]
            status += Status(
                speed = memberState.predictSSMatchStatus(StatusType.SPEED, it.isScenarioLink),
                stamina = memberState.predictSSMatchStatus(StatusType.STAMINA, it.isScenarioLink),
                power = memberState.predictSSMatchStatus(StatusType.POWER, it.isScenarioLink),
                guts = memberState.predictSSMatchStatus(StatusType.GUTS, it.isScenarioLink),
                wisdom = memberState.predictSSMatchStatus(StatusType.WISDOM, it.isScenarioLink),
                skillPt = 5,
            ) + when (effect) {
                StarEffect.Status -> Status().add(memberState.starType to (if (it.isScenarioLink) 15 else 10))
                StarEffect.SkillPt -> Status(skillPt = 20)
                StarEffect.Hp -> Status(hp = 20)
                StarEffect.MaxHp -> Status(hp = 20, maxHp = 4)
                StarEffect.Motivation -> Status(hp = 20, motivation = 1)
                else -> Status()
            }
            val rank = supporterRank[it.index]!! + supporterRank[-1]!! + 2
            lArcParam += LArcActionParam(
                supporterPt = 800 + (13 - rank) * (if (rank >= 13) 10 else 15),
                aptitudePt = 10,
            ) + when (effect) {
                StarEffect.AptitudePt -> LArcActionParam(aptitudePt = 50)
                StarEffect.StarGauge -> LArcActionParam(starGaugeMember = setOf(it))
                StarEffect.Aikyou -> LArcActionParam(condition = setOf("愛嬌○"))
                StarEffect.GoodTraining -> LArcActionParam(condition = setOf("練習上手○"))
                else -> LArcActionParam()
            }
            // スキルヒントは無視
        }
        val isSSSMatch = lArcStatus.isSSSMatch == true
        if (isSSSMatch) {
            status += Status(15, 15, 15, 15, 15, 25)
            lArcParam += LArcActionParam(supporterPt = lArcParam.supporterPt)
        }
        return arrayOf(SSMatch(isSSSMatch, joinMember, StatusActionResult(status, lArcParam)))
    }

    private fun LArcMemberState.predictSSMatchStatus(type: StatusType, scenarioLink: Boolean): Int {
        val baseValue = min(5, max(1, (status.get(type) / 150)))
        val typeValue = when {
            type != starType -> 0
            scenarioLink -> 6
            else -> 4
        }
        return baseValue + typeValue
    }

    private fun LArcStatus.predictGetAptitude(): List<Action> {
        return LArcAptitude.entries.mapNotNull {
            val level = it.getLevel(this)
            if (level in 1..<it.maxLevel && it.getCost(this) <= aptitudePt) {
                LArcGetAptitude(LArcGetAptitudeResult(it, level + 1))
            } else null
        }
    }

    override fun normalRaceBlocked(state: SimulationState): Boolean {
        return state.isLevelUpTurn
    }
}