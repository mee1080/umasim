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
import kotlin.math.min
import kotlin.random.Random

fun SimulationState.onTurnChange(): SimulationState {
    val turn = turn + 1
    val levelOverride = if (levelUpTurns.contains(turn)) 5 else null
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
    do {
        newMember = member.map { it.onTurnChange(turn, this) }
    } while (newMember.groupBy { it.position }.any { it.value.size > 5 })
    return copy(
        member = newMember,
    )
}

fun SimulationState.updateStatus(update: (status: Status) -> Status) = copy(status = update(status).adjustRange())

private fun MemberState.onTurnChange(turn: Int, state: SimulationState): MemberState {
    // 各トレーニングに配置
    val position = randomSelect(*Calculator.calcCardPositionSelection(card, state.liveStatus?.specialityRateUp ?: 0))
    val scenarioState = when (scenarioState) {
        is AoharuMemberState -> scenarioState.copy(
            // アオハルアイコン表示
            aoharuIcon = turn >= 3 && Random.nextDouble() < 0.4
        )

        else -> scenarioState
    }
    // ヒントアイコン表示
    val supportState = supportState?.copy(
        hintIcon = !scenarioState.hintBlocked && card.checkHint(state.hintFrequencyUp)
    )
    return copy(
        position = position,
        supportState = supportState,
        scenarioState = scenarioState,
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
                it.applyAction(action, charm, chara, trainingHintIndices.contains(it.index))
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

private fun MemberState.applyAction(action: Training, charm: Boolean, chara: Chara, hint: Boolean): MemberState {
    // 絆上昇量を反映
    val supportState = supportState?.copy(
        relation = min(100, supportState.relation + getTrainingRelation(charm, hint))
    )
    // アオハル特訓上昇量を反映
    val scenarioState = when (scenarioState) {
        is AoharuMemberState -> scenarioState.applyAction(action, chara)
        else -> scenarioState
    }
    return copy(
        supportState = supportState,
        scenarioState = scenarioState,
    )
}

private fun AoharuMemberState.applyAction(action: Training, chara: Chara): AoharuMemberState {
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
        val hintSupportList = support.filter { !it.card.type.outingType }
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

        is SelectedClimaxAction -> applyIfNotNull(action.buyItem) { buyItem(it) }
            .applyIfNotNull(action.useItem) { applyItem(it) }

        is SelectedLiveAction -> purchaseLesson(action.lesson)

        is SelectedGmAction -> applySelectedGmAction(action)
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
    }
}

private fun SimulationState.applyGmAction(action: GmActionParam): SimulationState {
    val oldState = gmStatus ?: return this
    if (action.knowledgeCount == 0) return this
    val newState = oldState
        .addKnowledge(Knowledge(action.knowledgeFounder, action.knowledgeType))
        .applyIf(action.knowledgeCount == 2) {
            addKnowledge(Knowledge(action.knowledgeFounder, action.knowledgeType))
        }
    return copy(gmStatus = newState)
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