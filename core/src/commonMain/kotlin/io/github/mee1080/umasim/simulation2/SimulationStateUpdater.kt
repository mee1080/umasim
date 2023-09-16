/*
 * Copyright 2021 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.util.applyIf
import io.github.mee1080.umasim.util.applyIfNotNull
import io.github.mee1080.umasim.util.mapIf
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun SimulationState.onTurnChange(): SimulationState {
    val turn = turn + 1
    val levelOverride = if (levelUpTurns.contains(turn)) {
        if (scenario == Scenario.LARC) 6 else 5
    } else null
    return copy(
        turn = turn,
        status = status.adjustRange(),
        training = training.map { it.copy(levelOverride = levelOverride) },
        enableItem = enableItem.onTurnChange(),
    )
}

fun SimulationState.shuffleMember(): SimulationState {
    // 各トレーニングの配置数が5以下になるよう調整
    var newMember: List<MemberState>
    var supportPosition: Map<StatusType, List<MemberState>>
    do {
        newMember = member.map { it.onTurnChange(turn, this) }
        supportPosition = trainingType.associateWith { mutableListOf() }
        newMember.forEach {
            if (it.position != StatusType.NONE) supportPosition[it.position]!!.add(it)
            if (it.secondPosition != StatusType.NONE) supportPosition[it.secondPosition]!!.add(it)
        }
    } while (supportPosition.any { it.value.size > 5 })
    return copy(
        member = newMember,
    )
}

fun SimulationState.updateStatus(update: (status: Status) -> Status) = copy(status = update(status).adjustRange())

fun SimulationState.addStatus(status: Status) = updateStatus { it + status }

private fun MemberState.onTurnChange(turn: Int, state: SimulationState): MemberState {
    // シナリオ友人不在判定
    if (state.scenario == Scenario.LARC && turn < 3 && charaName == "佐岳メイ") return this

    // 各トレーニングに配置
    var position: StatusType
    var secondPosition: StatusType
    do {
        position = randomSelect(*Calculator.calcCardPositionSelection(card, state.liveStatus?.specialityRateUp ?: 0))
        secondPosition = if (card.hasSecondPosition(relation)) {
            randomSelect(*Calculator.calcCardPositionSelection(card, state.liveStatus?.specialityRateUp ?: 0))
        } else StatusType.NONE
    } while (position == secondPosition && position != StatusType.NONE)

    val scenarioState = when (scenarioState) {
        is AoharuMemberState -> scenarioState.copy(
            // アオハルアイコン表示
            aoharuIcon = turn >= 3 && Random.nextDouble() < 0.4
        )

        else -> scenarioState
    }
    // ヒントアイコン表示、情熱ゾーン減少
    val supportState = supportState?.copy(
        hintIcon = !scenarioState.hintBlocked && card.checkHint(state.hintFrequencyUp),
        passionTurn = max(0, supportState.passionTurn - 1),
        // TODO 13ターン目以降お出かけ可能と仮定
        outingEnabled = card.type.outingType && turn > 12,
    )
    return copy(
        position = position,
        supportState = supportState,
        scenarioState = scenarioState,
        secondPosition = secondPosition,
    )
}

fun EnableItem.onTurnChange(): EnableItem {
    return if (megaphoneTurn > 1) {
        EnableItem(
            megaphone = megaphone,
            megaphoneTurn = megaphoneTurn - 1,
        )
    } else EnableItem()
}

fun SimulationState.applyAction(action: Action, result: Status): SimulationState {
    // FIXME トレーニング失敗時にもLv上昇など発生
    // ステータス更新
    val newStatus = status + result
    // Action結果反映
    val newState = if (action is Training) {
        val newTraining = training.map {
            if (action.type == it.type) {
                it.applyAction(action, scenario.trainingAutoLevelUp)
            } else it
        }
        val memberIndices = action.member.map { it.index }
        val trainingHint = selectTrainingHint(action.member)
        val trainingHintIndices = trainingHint.second.map { it.index }
        val newMember = member.map {
            if (memberIndices.contains(it.index)) {
                it.applyTraining(action, charm, chara, trainingHintIndices.contains(it.index), this)
            } else it
        }
        copy(
            member = newMember,
            training = newTraining,
            status = newStatus + trainingHint.first + selectAoharuTrainingHint(action.member),
        )
    } else if (action is Race && itemAvailable && action.grade != RaceGrade.FINALS) {
        copy(
            status = newStatus,
            shopCoin = shopCoin + 100,
        )
    } else {
        copy(status = newStatus)
    }
    // シナリオ別結果反映
    return newState.applyScenarioAction(action)
}

private fun MemberState.applyTraining(
    action: Training,
    charm: Boolean,
    chara: Chara,
    hint: Boolean,
    state: SimulationState,
): MemberState {
    // 絆上昇量を反映
    val supportState = supportState?.copy(
        relation = min(100, supportState.relation + getTrainingRelation(charm, hint)),
        // FIXME GMの三女神の情熱ゾーンは欠片獲得と同時確定なので別処理、その他は実装保留
//        passionTurn = if (card.type == StatusType.GROUP) {
//            // FIXME 13T以降に20%で情熱突入と仮定、アプデで情熱ゾーン中に踏むと消えにくくなった件は未反映
//            if (turn <= 12 || supportState.passion || Random.nextDouble() > 0.2) supportState.passionTurn else {
//                randomSelect(
//                    3 to 60,
//                    4 to 20,
//                    5 to 10,
//                    6 to 10,
//                )
//            }
//        } else 0
    )
    // アオハル特訓上昇量を反映
    val scenarioState = when (scenarioState) {
        is AoharuMemberState -> scenarioState.applyTraining(action, chara)
        is LArcMemberState -> scenarioState.applyTraining(action, state.isLevelUpTurn)
        else -> scenarioState
    }
    return copy(
        supportState = supportState,
        scenarioState = scenarioState,
    )
}

private fun AoharuMemberState.applyTraining(action: Training, chara: Chara): AoharuMemberState {
    if (!aoharuIcon) return this
    var status = status
    var maxStatus = maxStatus
    if (aoharuBurn) {
        // 爆発時はメンバーのタイプに応じて最大値と現在値が上昇
        val burnStatus = Store.Aoharu.getBurnTeam(member.type).getStatus(chara)
        maxStatus += burnStatus
        status += burnStatus
    } else {
        // アオハル特訓時はトレーニングタイプに応じて現在値が上昇
        status += Store.Aoharu.getTrainingTeam(action.type, action.level).getRandomStatus(chara)
    }
    // 最大値を反映
    status = status.adjustRange(maxStatus)
    return copy(
        status = status,
        maxStatus = maxStatus,
        aoharuTrainingCount = aoharuTrainingCount + 1,
    )
}

private fun TrainingState.applyAction(action: Training, autoLevelUp: Boolean): TrainingState {
    val count = count + 1
    return if (autoLevelUp && level < 5 && count >= 4) {
        copy(level = level + 1, count = 0)
    } else {
        copy(count = count)
    }
}

private fun SimulationState.selectTrainingHint(support: List<MemberState>): Pair<Status, List<MemberState>> {
    return if (gmStatus?.hintFrequencyUp == true) {
        // ステータス上昇はCalculatorで計算済み
        val hintSupportList = support.filter { !it.outingType }
        hintSupportList.map { hintSupport ->
            val hintSkill = (hintSupport.card.skills.filter { !status.skillHint.containsKey(it) } + "").random()
            Status(
                skillHint = if (hintSkill.isEmpty()) emptyMap() else mapOf(hintSkill to 1 + hintSupport.card.hintLevel),
            )
        }.fold(Status()) { acc, status ->
            acc + status
        } to hintSupportList
    } else {
        val hintSupportList = support.filter { it.hint }
        if (hintSupportList.isEmpty()) return Status() to emptyList()
        val hintSupport = hintSupportList.random()
        val hintSkill = (hintSupport.card.skills.filter { !status.skillHint.containsKey(it) } + "").random()
        if (hintSkill.isEmpty()) {
            hintSupport.card.hintStatus
        } else {
            Status(skillHint = mapOf(hintSkill to 1 + hintSupport.card.hintLevel))
        } to listOf(hintSupport)
    }
}

private fun SimulationState.selectAoharuTrainingHint(support: List<MemberState>): Status {
    if (scenario != Scenario.AOHARU) return Status()
    val aoharuList = support.filter { it.scenarioState is AoharuMemberState && it.scenarioState.aoharuIcon }
    if (aoharuList.isEmpty()) return Status()
    // TODO アオハル爆発スキル
    // val burnList = aoharuList.filter { it.aoharuBurn }
    if (Random.nextInt(100) >= 15) return Status()
    val skillList = aoharuList.filter { it.scenarioState is AoharuMemberState && !it.scenarioState.aoharuBurn }
        .flatMap { member -> member.card.skills.map { member to it } }
        .filter { !status.skillHint.containsKey(it.second) }
    if (skillList.isEmpty()) return Status()
    val target = skillList.random()
    return Status(skillHint = mapOf(target.second to 1 + if (!target.first.guest) target.first.card.hintLevel else 0))
}

fun SimulationState.applySelectedScenarioAction(action: SelectedScenarioAction?): SimulationState {
    return when (action) {
        null -> this

        is SelectedClimaxAction -> applyIfNotNull(action.buyItem) { buyItem(it) }.applyIfNotNull(action.useItem) {
            applyItem(
                it
            )
        }

        is SelectedLiveAction -> purchaseLesson(action.lesson)

        is SelectedGmAction -> applySelectedGmAction(action)

        is SelectedLArcAction -> applySelectedLArcAction(action)
    }
}

fun SimulationState.buyItem(itemList: List<ShopItem>): SimulationState {
    return itemList.fold(this) { state, item -> state.buyItem(item) }
}

fun SimulationState.buyItem(item: ShopItem): SimulationState {
    return copy(
        possessionItem = possessionItem + item,
        shopCoin = shopCoin - item.coin,
    )
}

fun SimulationState.applyItem(itemList: List<ShopItem>): SimulationState {
    return itemList.fold(this) { state, item -> state.applyItem(item) }
}

fun SimulationState.applyItem(item: ShopItem): SimulationState {
    return when (item) {
        is StatusItem -> copy(status = status + item.status)
        is UniqueItem -> when (item.name) {
            "おいしい猫缶" -> this
            "にんじんBBQセット" -> {
                val relation = if (condition.contains("愛嬌○")) 7 else 5
                copy(member = member.map { it.addRelation(relation) })
            }

            "リセットホイッスル" -> shuffleMember()
            "健康祈願のお守り" -> copy(enableItem = enableItem.copy(unique = item))
            else -> this
        }

        is AddConditionItem -> copy(condition = condition + item.condition)
        is RemoveConditionItem -> copy(condition = condition - item.condition.toSet())
        is TrainingLevelItem -> copy(training = training.map {
            if (it.type == item.type) it.copy(level = min(5, it.level + 1)) else it
        })

        is MegaphoneItem -> copy(enableItem = enableItem.copy(megaphone = item, megaphoneTurn = item.turn))
        is WeightItem -> copy(enableItem = enableItem.copy(weight = item))
        is RaceBonusItem -> copy(enableItem = enableItem.copy(raceBonus = item))
        is FanBonusItem -> copy(enableItem = enableItem.copy(fanBonus = item))
    }.copy(possessionItem = possessionItem - item)
}

fun MemberState.addRelation(relation: Int): MemberState {
    return copy(
        supportState = supportState?.copy(
            relation = min(100, supportState.relation + relation)
        ),
    )
}

fun SimulationState.updateLesson(): SimulationState {
    val liveStatus = liveStatus ?: return this
    val currentPeriod = LivePeriod.turnToPeriod(turn)
    val lessonCount = if (currentPeriod == liveStatus.currentPeriod) liveStatus.lessonCount else 1
    val lessonSelection = LessonProvider.provide(
        currentPeriod,
        lessonCount,
        liveStatus.learnedSongs,
    )
    return copy(
        liveStatus = liveStatus.copy(
            currentPeriod = currentPeriod,
            lessonCount = lessonCount + 1,
            lessonSelection = lessonSelection,
        )
    )
}

fun SimulationState.purchaseLesson(lesson: Lesson): SimulationState {
    val liveStatus = liveStatus ?: return this
    val performance = status.performance ?: return this
    val lessonStatus = when (val learnBonus = lesson.learnBonus) {
        is PerformanceBonus -> Status(performance = learnBonus.performance)
        is SkillHintBonus -> Status(skillHint = mapOf("スキル" to learnBonus.level))
        is StatusBonus -> learnBonus.status
        is TrainingBonus -> Status()
    }
    return copy(
        status = status.copy(performance = performance - lesson.cost) + lessonStatus,
        liveStatus = liveStatus.copy(
            learnedLesson = liveStatus.learnedLesson + lesson,
        ),
    ).updateLesson()
}

fun SimulationState.addLesson(lesson: Lesson): SimulationState {
    val liveStatus = liveStatus ?: return this
    return copy(
        liveStatus = liveStatus.copy(
            learnedLesson = liveStatus.learnedLesson + lesson,
        ),
    )
}

fun SimulationState.purchaseBeforeLive(selector: ActionSelector): SimulationState {
    var state = this
    while (true) {
        val lesson = selector.selectBeforeLiveLesson(state) ?: break
        state = state.purchaseLesson(lesson)
    }
    return state
}

fun SimulationState.applyLive(selector: ActionSelector): SimulationState {
    if (liveStatus == null) return this
    val state = purchaseBeforeLive(selector)
    val liveStatus = state.liveStatus!!
    val newLesson = liveStatus.newLesson
    var special = ""
    var songCount = 0
    var techniqueCount = 0
    newLesson.forEach {
        if (it is SongLesson) {
            if (it.specialSong) {
                special = it.name
            } else {
                songCount++
            }
        } else {
            techniqueCount++
        }
    }
    val skillPtUp = techniqueCount * 5 + songCount * 25
    val fanCountBase = when (LivePeriod.turnToPeriod(turn)) {
        LivePeriod.Junior -> 100
        LivePeriod.Classic1 -> 200
        LivePeriod.Classic2 -> 550
        else -> 650
    }
    val statusUp = when (special) {
        // グランドライブ（特別）
        specialSongs[2].name -> Status(
            speed = 15, stamina = 15, power = 15, guts = 15, wisdom = 15,
            skillPt = skillPtUp, fanCount = 9000,
        )
        // グランドライブ（通常）
        specialSongs[1].name -> if (songCount >= 3) Status(
            speed = 12, stamina = 12, power = 12, guts = 12, wisdom = 12,
            skillPt = skillPtUp, fanCount = 2000,
        ) else Status(
            speed = 5, stamina = 5, power = 5, guts = 5, wisdom = 5,
            skillPt = skillPtUp, fanCount = 200,
        )
        // 告知ライブ
        else -> Status(
            performance = Performance(10, 10, 10, 10, 10),
        ) + if (songCount >= 3) Status(
            speed = 10, stamina = 10, power = 10, guts = 10, wisdom = 10,
            skillPt = skillPtUp, fanCount = fanCountBase * 10,
        ) else Status(
            speed = 3, stamina = 3, power = 3, guts = 3, wisdom = 3,
            skillPt = skillPtUp, fanCount = fanCountBase,
        )
    }
    return state.copy(
        status = status + statusUp,
        liveStatus = liveStatus.applyLive(),
    )
}

private fun LiveStatus.applyLive(): LiveStatus {
    return copy(
        livedLesson = livedLesson + learnedLesson.subList(livedLesson.size + 1, learnedLesson.size)
    )
}

private fun SimulationState.applyScenarioAction(action: Action): SimulationState {
    val scenarioAction = action.scenarioActionParam ?: return this
    return when (scenarioAction) {
        is GmActionParam -> applyGmAction(scenarioAction)
        is LArcActionParam -> applyLArcAction(action, scenarioAction)
    }
}

private fun SimulationState.applyGmAction(action: GmActionParam): SimulationState {
    val oldGmStatus = gmStatus ?: return this
    if (action.knowledgeCount == 0) return this
    var newMember = member
    var addStatus = Status()
    val newGmStatus = oldGmStatus.addKnowledge(Knowledge(action.knowledgeFounder, action.knowledgeType))
        .applyIf(action.knowledgeCount == 2) {
            addKnowledge(Knowledge(action.knowledgeFounder, action.knowledgeType))
        }.applyIf(action.knowledgeEventRate > Random.nextDouble()) {
            newMember = member.mapIf({ it.card.chara == "ダーレーアラビアン" }) {
                val newSupportState = supportState?.copy(
                    passionTurn = if (!supportState.outingEnabled || supportState.passion) supportState.passionTurn else {
                        randomSelect(
                            3 to 50,
                            4 to 20,
                            5 to 15,
                            6 to 15,
                        )
                    },
                    relation = min(100, supportState.relation + 5),
                )
                copy(supportState = newSupportState)
            }
            // FIXME 色選択
            val founder = Founder.entries.random()
            addStatus = when (founder) {
                Founder.Red -> Status(skillPt = 9)
                Founder.Blue -> Status(speed = 4, skillPt = 4)
                Founder.Yellow -> Status(stamina = 4, skillPt = 4)
            }
            addKnowledge(Knowledge(founder, trainingTypeOrSkill.random()))
        }
    return copy(gmStatus = newGmStatus, member = newMember, status = status + addStatus)
}

private fun SimulationState.applySelectedGmAction(action: SelectedGmAction): SimulationState {
    val gmStatus = gmStatus ?: return this
    return when (action) {
        is GmActivateWisdom -> {
            val effect = gmStatus.activateWisdom()
            copy(gmStatus = effect.first, status = status + effect.second)
        }
    }
}

fun SimulationState.updateLArcStatus(update: LArcStatus.() -> LArcStatus): SimulationState {
    return copy(lArcStatus = lArcStatus?.update())
}

private fun SimulationState.applyLArcAction(action: Action, lArcAction: LArcActionParam): SimulationState {
    return if (action is SSMatch) {
        val ssMatchMemberIndex = action.member.map { it.index }
        val starGaugeMemberIndex = lArcAction.starGaugeMember.map { it.index }
        val newMember = member.mapIf({ ssMatchMemberIndex.contains(it.index) }) {
            val memberState = scenarioState as LArcMemberState
            val next = when (memberState.starLevel % 3) {
                1 -> StarEffect.SkillHint
                2 -> randomSelect(StarEffect.Status to 1, memberState.specialStarEffect to 1)
                else -> if (memberState.nextStarEffect[2] == StarEffect.Status) memberState.specialStarEffect else StarEffect.Status
            }
            val newNextStarEffect = memberState.nextStarEffect.takeLast(2) + next
            val newMemberState = memberState.copy(
                starLevel = memberState.starLevel + 1,
                starGauge = if (starGaugeMemberIndex.contains(index)) 3 else 0,
                nextStarEffect = newNextStarEffect,
            )
            copy(scenarioState = newMemberState)
        }
        updateLArcStatus {
            copy(
                supporterPt = supporterPt + lArcAction.supporterPt,
                aptitudePt = aptitudePt + lArcAction.aptitudePt,
                ssMatchCount = if (isSSSMatch == true) 0 else ssMatchCount + ssMatchMemberIndex.size,
                totalSSMatchCount = totalSSMatchCount + ssMatchMemberIndex.size,
                isSSSMatch = null,
            ).applyIf(totalSSMatchCount >= 2 && overseasTurfAptitude == 0) {
                copy(
                    overseasTurfAptitude = 1,
                    longchampAptitude = 1,
                )
            }.applyIf(totalSSMatchCount >= 10 && lifeRhythm == 0) {
                copy(
                    lifeRhythm = 1,
                    nutritionManagement = 1,
                )
            }
        }.copy(
            member = newMember,
            condition = condition + lArcAction.condition,
        )
    } else {
        val newState = if (lArcAction.mayEventChance && Random.nextDouble() < 0.4) {
            if (isLevelUpTurn) {
                updateLArcStatus { copy(aptitudePt = aptitudePt + 50) }
            } else {
                val training = action as Training
                val joinTargets = training.member
                    .filter { !it.outingType && (it.scenarioState as LArcMemberState).starGauge < 2 - training.friendCount }
                    .map { it.index }
                val notJoinTargets = member
                    .filter {
                        !it.outingType && (it.scenarioState as LArcMemberState).starGauge < 3 && !joinTargets.contains(
                            it.index
                        )
                    }.shuffled().take(5 - joinTargets.size).map { it.index }
                val targets = joinTargets + notJoinTargets
                val newMember = member.mapIf({ targets.contains(it.index) }) {
                    val lArcState = scenarioState as LArcMemberState
                    copy(scenarioState = lArcState.copy(starGauge = lArcState.starGauge + 1))
                }
                copy(member = newMember)
            }
        } else this
        newState.updateLArcStatus {
            copy(aptitudePt = aptitudePt + lArcAction.aptitudePt)
        }
    }
}

fun LArcMemberState.applyTraining(action: Training, isLevelUpTurn: Boolean): LArcMemberState {
    if (starType == StatusType.NONE) return this
    val addStatus = when (action.type) {
        StatusType.SPEED -> Status(random(15, 20), random(2, 5), random(7, 12), random(2, 5), random(2, 5))
        StatusType.STAMINA -> Status(random(2, 5), random(15, 20), random(2, 5), random(7, 12), random(2, 5))
        StatusType.POWER -> Status(random(2, 5), random(7, 12), random(15, 20), random(2, 5), random(2, 5))
        StatusType.GUTS -> Status(random(6, 11), random(2, 5), random(6, 11), random(15, 20), random(2, 5))
        StatusType.WISDOM -> Status(random(7, 12), random(2, 5), random(2, 5), random(2, 5), random(15, 20))
        else -> Status()
    }
    return copy(
        status = status + addStatus,
        starGauge = if (isLevelUpTurn) starGauge else min(3, starGauge + 1 + action.friendCount),
    )
}

fun SimulationState.applySelectedLArcAction(action: SelectedLArcAction): SimulationState {
    return updateLArcStatus { addAptitude(action.aptitude) }
}