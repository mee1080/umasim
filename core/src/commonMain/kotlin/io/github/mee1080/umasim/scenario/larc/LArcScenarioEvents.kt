package io.github.mee1080.umasim.scenario.larc

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.ScenarioEvents
import io.github.mee1080.umasim.scenario.updateFactor
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.applyIf
import kotlin.random.Random

class LArcScenarioEvents : ScenarioEvents {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return state.updateFactor().copy(
            goalRace = listOf(
                state.goalRace.first(),
                RaceEntry(34, "東京優駿（日本ダービー）", 6000, 20000, RaceGrade.G1, 2400, RaceGround.TURF, "東京"),
                RaceEntry(41, "ニエル賞", 2000, 5000, RaceGrade.G2, 2400, RaceGround.TURF, "ロンシャン"),
                RaceEntry(43, "凱旋門賞", 5000, 44000, RaceGrade.G1, 2400, RaceGround.TURF, "ロンシャン"),
                RaceEntry(60, "宝塚記念", 20000, 15000, RaceGrade.G1, 2200, RaceGround.TURF, "阪神"),
                RaceEntry(65, "フォワ賞", 2000, 5000, RaceGrade.G2, 2400, RaceGround.TURF, "ロンシャン"),
                RaceEntry(67, "凱旋門賞", 5000, 44000, RaceGrade.G1, 2400, RaceGround.TURF, "ロンシャン"),
            )
        )
    }

    override suspend fun beforeAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = state.turnUpdate()
        return when (base.turn) {
            // 3T行動前 メイちゃん/理事長参加、L'Arcメンバー加入
            3 -> base.createLArcStatus()

            // クラシック継承
            31 -> state
                .updateFactor()

            // クラシック新年
            49 -> state
                .addStatus(Status(hp = 30))

            // シニア継承
            55 -> state
                .updateFactor()

            // C7前行動前 海外適性獲得
            37 -> base.updateLArcStatus {
                copy(
                    frenchSkill = 1,
                    overseasExpedition = 1,
                )
            }

            else -> base
        }
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = state.applyExpectationEvent()
        return when (base.turn) {
            // J10前行動後 適性Pt+100
            19 -> base.addAptitudePt(100)

            // J12後行動後 全ステ+3スキルPt20、L'Arc代表交流戦（勝利前提）
            24 -> base
                .addStatus(Status(3, 3, 3, 3, 3, 20))
                .friendlyMatch(50, 3, 10, 5)

            // C3前行動後 選択目標キャラに応じたステ上昇（単純化のためメイちゃん固定）
            29 -> base.addStatus(Status(speed = 10, motivation = 1))

            // C6後行動後 L'Arc代表交流戦（勝利前提）
            36 -> base.friendlyMatch(50, 5, 20, 5)

            // ニエル賞後 適性Pt+50（勝利前提）、海外適性獲得
            41 -> base
                .addAptitudePt(50)
                .updateLArcStatus {
                    copy(
                        strongHeart = 1,
                        mentalStrength = 1,
                    )
                }

            // クラシック凱旋門賞後 適性Pt80（勝利前提）
            43 -> base
                .addAptitudePt(80)
                .updateLArcStatus {
                    copy(consecutiveVictories = 1)
                }

            // クラシック10後行動後 やる気-2
            44 -> base.addStatus(Status(motivation = -2))

            // クラシック11前行動後 適性Pt+30やる気+3
            45 -> base.addStatus(Status(motivation = 3)).addAptitudePt(30)

            // シニア3後行動後 （イベント）、L'Arc代表交流戦（勝利前提）
            54 -> base
                .addStatus(Status(3, 3, 3, 3, 3, 20))
                .friendlyMatch(80, 7, 30, 6)

            // シニア6後行動後 L'Arc代表交流戦（勝利前提）、選択目標キャラに応じたステ上昇（単純化のためヒント無視）、海外適性獲得
            60 -> base
                .friendlyMatch(130, 10, 40, 7)
                .addStatus(Status(10, 10, 10, 10, 10, motivation = 1))
                .applyIf({ it.lArcStatus!!.totalSSMatchCount >= 40 }) {
                    updateLArcStatus {
                        copy(hopeOfLArc = 1)
                    }
                }

            // フォワ賞後 適性Pt+100、想いを背負ってLv1（勝利前提、単純化のためヒント無視）
            65 -> base.addAptitudePt(100)

            else -> base
        }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        // 交流戦全勝、凱旋門賞連覇
        return state.addStatus(Status(30, 30, 30, 30, 30, 60))
    }

    private fun SimulationState.createLArcStatus(): SimulationState {
        val linkChara = Scenario.LARC.scenarioLink
        val outingMember = member.filter { it.outingType }
        val supportMember = member.filter { !it.outingType }
        var memberIndex = 6
        val linkSupportMember = supportMember.filter { linkChara.contains(it.charaName) }
        val linkSupportCharaNames = linkSupportMember.map { it.charaName }.toSet()
        val linkGuestMember = linkChara
            .filter { !linkSupportCharaNames.contains(it) }
            .take(linkChara.size - linkSupportMember.size)
            .map { Store.guestSupportCardMap[it]!! }
            .map { MemberState(memberIndex++, it, StatusType.NONE, null, LArcMemberState()) }

        val notLinkSupportMember = supportMember.filterNot { linkChara.contains(it.charaName) }
        val notLinkSupportCharaNames = notLinkSupportMember.map { it.charaName }.toSet()
        val notLinkGuestMember = Store.guestSupportCardList
            .filter { !linkChara.contains(it.chara) && !notLinkSupportCharaNames.contains(it.chara) }
            .shuffled()
            .take(15 - linkChara.size - notLinkSupportMember.size)
            .map { MemberState(memberIndex++, it, StatusType.NONE, null, LArcMemberState()) }

        val linkShuffled = (linkSupportMember + linkGuestMember).shuffled()
        val notLinkShuffled = (notLinkSupportMember + notLinkGuestMember).shuffled()
        val initializedMember = (linkShuffled + notLinkShuffled).mapIndexed { index, memberState ->
            val scenarioState =
                (memberState.scenarioState as LArcMemberState).initialize(index + 1, memberState.charaName)
            memberState.copy(scenarioState = scenarioState)
        }
        val newMember = (outingMember + initializedMember).sortedBy { it.index }

        return copy(member = newMember, scenarioStatus = LArcStatus())
    }

    private fun SimulationState.turnUpdate(): SimulationState {
        val lArcStatus = lArcStatus ?: return this
        val newMember = member.map {
            if (it.outingType) return@map it
            val lArcMemberState = it.scenarioState as LArcMemberState
            val addStatus = when (lArcMemberState.starType) {
                StatusType.SPEED -> Status(random(6, 8), random(3, 5), random(5, 7), random(3, 5), random(3, 5))
                StatusType.STAMINA -> Status(random(3, 5), random(6, 8), random(3, 5), random(5, 7), random(3, 5))
                StatusType.POWER -> Status(random(3, 5), random(5, 7), random(6, 8), random(3, 5), random(3, 5))
                StatusType.GUTS -> Status(random(4, 6), random(3, 5), random(4, 6), random(6, 8), random(3, 5))
                StatusType.WISDOM -> Status(random(5, 7), random(3, 5), random(3, 5), random(3, 5), random(6, 8))
                else -> Status()
            }
            val addSupporterPt = if (turn in 37..43 || turn >= 61) 0 else {
                val supporterPtBase = when (lArcMemberState.initialRank) {
                    1 -> random(14, 28)
                    2 -> random(13, 27)
                    3 -> random(12, 26)
                    4 -> random(11, 25)
                    5 -> random(9, 23)
                    6 -> random(8, 22)
                    7 -> random(7, 22)
                    8 -> random(6, 22)
                    9 -> random(5, 19)
                    10, 11 -> random(2, 18)
                    12, 13 -> random(1, 16)
                    else -> random(0, 14)
                }
                val supporterPtFactor = when {
                    turn >= 49 -> 12
                    turn >= 25 -> 10
                    else -> 4
                }
                supporterPtBase * supporterPtFactor
            }
            it.copy(
                scenarioState = lArcMemberState.copy(
                    status = lArcMemberState.status + addStatus,
                    supporterPt = lArcMemberState.supporterPt + addSupporterPt,
                )
            )
        }
        val totalSupporterPt = newMember.sumOf { (it.scenarioState as LArcMemberState).supporterPt }
        val newSSMatchMember = newMember
            .filter { (it.scenarioState as LArcMemberState).starGauge >= 3 }
            .shuffled()
            .take(5)
            .toSet()
        val newIsSSSMatch = if (lArcStatus.isSSSMatch == null && newSSMatchMember.size == 5) {
            val rate = when {
                lArcStatus.ssMatchCount >= 8 -> 100
                lArcStatus.ssMatchCount >= 4 -> 50
                else -> 10
            }
            Random.nextInt(100) < rate
        } else lArcStatus.isSSSMatch

        return copy(
            member = newMember,
            scenarioStatus = lArcStatus.copy(
                memberSupporterPt = totalSupporterPt,
                ssMatchMember = newSSMatchMember,
                isSSSMatch = newIsSSSMatch,
            )
        )
    }

    private fun SimulationState.addAptitudePt(value: Int): SimulationState {
        return updateLArcStatus { copy(aptitudePt = aptitudePt + value) }
    }

    private fun SimulationState.friendlyMatch(
        aptitudePt: Int,
        status: Int,
        skillPt: Int,
        memberCount: Int,
    ): SimulationState {
        val memberSupporterPt = member
            .sortedByDescending { (it.scenarioState as LArcMemberState).supporterPt }
            .take(memberCount)
            .shuffled()
            .take(4)
            .mapIndexed { index, memberState ->
                // ジュニアとクラシックは2～5位、シニア3月以降は3～5位に入る想定
                val rank = index + if (memberCount >= 6) 3 else 2
                val supporterPt = when (rank) {
                    2 -> 900
                    3 -> 600
                    4, 5 -> 400
                    else -> 0
                }
                memberState.index to supporterPt
            }.toMap()
        val newMember = member.map {
            val addSupporterPt = memberSupporterPt[it.index] ?: return@map it
            val oldState = it.scenarioState as LArcMemberState
            it.copy(scenarioState = oldState.copy(supporterPt = oldState.supporterPt + addSupporterPt))
        }

        return copy(member = newMember)
            .addStatus(raceStatus(5, status, skillPt))
            .addAptitudePt(aptitudePt)
            .updateLArcStatus { copy(supporterPt = supporterPt + 1500) }
    }

    private fun SimulationState.applyExpectationEvent(): SimulationState {
        val lArcStatus = lArcStatus ?: return this
        val expectationLevel = lArcStatus.expectationLevel
        val lastExpectationEvent = lArcStatus.lastExpectationEvent
        val eventLevel = listOf(20, 40, 60, 80, 100).firstOrNull {
            it in (lastExpectationEvent + 1)..expectationLevel
        } ?: return this
        val addStatus = when (eventLevel) {
            20 -> Status(2, 2, 2, 2, 2, fanCount = 2000)
            40 -> Status(3, 3, 3, 3, 3, fanCount = 2000)
            60 -> Status(4, 4, 4, 4, 4, fanCount = 4000)
            80 -> Status(5, 5, 5, 5, 5, fanCount = 6000)
            else -> Status(5, 5, 5, 5, 5, fanCount = 8000)
        }
        return addStatus(addStatus).copy(
            scenarioStatus = lArcStatus.copy(lastExpectationEvent = eventLevel),
        ).applyIf(eventLevel in arrayOf(20, 60, 100)) { allTrainingLevelUp() }
    }
}
