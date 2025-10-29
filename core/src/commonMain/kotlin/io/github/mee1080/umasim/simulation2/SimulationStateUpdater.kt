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
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.climax.*
import io.github.mee1080.umasim.scenario.cook.activateDish
import io.github.mee1080.umasim.scenario.cook.updateCookStatus
import io.github.mee1080.umasim.scenario.gm.Founder
import io.github.mee1080.umasim.scenario.gm.Knowledge
import io.github.mee1080.umasim.scenario.larc.LArcMemberState
import io.github.mee1080.umasim.scenario.larc.LArcStatus
import io.github.mee1080.umasim.scenario.larc.StarEffect
import io.github.mee1080.umasim.scenario.legend.LegendCalculator
import io.github.mee1080.umasim.scenario.live.*
import io.github.mee1080.umasim.scenario.mecha.MechaCalculator
import io.github.mee1080.umasim.scenario.mecha.applyMechaOverdrive
import io.github.mee1080.umasim.scenario.mecha.applyTuning
import io.github.mee1080.umasim.scenario.mecha.updateMechaStatus
import io.github.mee1080.umasim.scenario.mujinto.MujintoCalculator
import io.github.mee1080.umasim.scenario.onsen.OnsenActionParam
import io.github.mee1080.umasim.scenario.onsen.OnsenCalculator
import io.github.mee1080.umasim.scenario.onsen.PRActivity
import io.github.mee1080.umasim.scenario.onsen.PRActivityResult
import io.github.mee1080.umasim.scenario.uaf.UafStatus
import io.github.mee1080.utility.applyIf
import io.github.mee1080.utility.applyIfNotNull
import io.github.mee1080.utility.mapIf
import io.github.mee1080.utility.sumMapOf
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

suspend fun SimulationState.onTurnChange(selector: ActionSelector): SimulationState {
    val turn = turn + 1
    val levelOverride = if (levelUpTurns.contains(turn)) {
        if (scenario == Scenario.LARC) 6 else 5
    } else null
    var newState = updateRefresh()
    newState = newState.copy(
        turn = turn,
        member = member.map { it.onTurnChange(turn, newState) },
        training = newState.training.map { it.copy(levelOverride = levelOverride) },
        enableItem = newState.enableItem.onTurnChange(),
    )
    newState = newState.updateOutingStep(selector)
    newState = scenario.calculator.updateScenarioTurn(newState)
    return newState
}

fun SimulationState.updateRefresh(): SimulationState {
    if (refreshTurn <= 0) return this
    return addStatus(Status(hp = 5)).copy(
        refreshTurn = refreshTurn - 1,
    )
}

suspend fun SimulationState.updateOutingStep(selector: ActionSelector): SimulationState {
    val targets = mutableSetOf<MemberState>()
    member.filter { it.outingType && it.supportState?.outingStep == 1 }.forEach {
        val rate = if (it.relation >= 60) 0.25 else 0.05
        if (Random.nextDouble() < rate) {
            targets.add(it)
        }
    }
    var state = this
    for (target in targets) {
        state = state.applyOutingEvent(target, selector)
    }
    return state
}

fun SimulationState.shuffleMember(): SimulationState {
    // 各トレーニングの配置数が5以下になるよう調整
    var newMember: List<MemberState>
    var supportPosition: Map<StatusType, MutableList<MemberState>>
    do {
        newMember = member.map { it.selectPosition(turn, this) }
        supportPosition = trainingType.associateWith { mutableListOf() }
        newMember.forEach {
            it.positions.forEach { status ->
                supportPosition[status]!!.add(it)
            }
        }
    } while (supportPosition.any { it.value.size > 5 })
    val forceHintCount = min(forceHintCount, support.count { !it.outingType })
    if (forceHintCount > 0) {
        val positionedSupport = newMember.filter { !it.guest && !it.outingType && it.position != StatusType.NONE }
        val notHintSupport = positionedSupport.filter { !it.hint }
        val hintSupportCount = positionedSupport.size - notHintSupport.size
        if (forceHintCount > hintSupportCount) {
            notHintSupport.shuffled().take(forceHintCount - hintSupportCount).forEach { member ->
                newMember = newMember.mapIf({ it.index == member.index }) {
                    it.copy(supportState = it.supportState?.copy(hintIcon = true))
                }
            }
        }
    }
    if (additionalMemberCount > 0) {
        var positionedSupport = newMember.filter { !it.guest && it.position != StatusType.NONE }.shuffled()
        if (positionedSupport.isNotEmpty()) {
            while (positionedSupport.size < additionalMemberCount) {
                positionedSupport = positionedSupport + positionedSupport.shuffled()
            }
            positionedSupport.take(additionalMemberCount).groupBy { it.index }.forEach { (index, members) ->
                val target = members[0]
                val additionalPositions = supportPosition.filter {
                    it.value.size < 5 && !target.positions.contains(it.key)
                }.keys.shuffled().take(members.size).toSet()
                additionalPositions.forEach {
                    supportPosition[it]!!.add(target)
                }
                newMember = newMember.mapIf({ it.index == index }) {
                    it.copy(additionalPosition = it.additionalPosition + additionalPositions)
                }
            }
        }
    }
    return copy(
        member = newMember,
    )
}

private fun SimulationState.updateStatus(update: (status: Status) -> Status): SimulationState {
    return copy(status = update(status).adjustRange().applyIf(motivationLimitOver) {
        copy(motivation = 3)
    })
}

fun SimulationState.addStatus(status: Status, applyScenario: Boolean = true): SimulationState {
    return applyIf(applyScenario) {
        scenario.calculator.updateOnAddStatus(this, status)
    }.updateStatus { it + status }
}

fun SimulationState.addAllStatus(
    status: Int,
    skillPt: Int = 0,
    skillHint: Map<String, Int> = emptyMap(),
    hp: Int = 0,
) = addStatus(
    Status(
        speed = status, stamina = status, power = status, guts = status, wisdom = status,
        skillPt = skillPt, skillHint = skillHint, hp = hp,
    )
)

private fun MemberState.onTurnChange(turn: Int, state: SimulationState): MemberState {
    val scenarioState = when (scenarioState) {
        is AoharuMemberState -> scenarioState.copy(
            // アオハルアイコン表示
            aoharuIcon = turn >= 3 && Random.nextDouble() < 0.4
        )

        else -> scenarioState
    }
    // ヒントアイコン表示、情熱ゾーン減少
    val supportState = supportState?.copy(
        hintIcon = !outingType && (
                state.forceHint || (!scenarioState.hintBlocked && card.checkHint(state.hintFrequencyUp(position)))
                ),
        passionTurn = max(0, supportState.passionTurn - 1),
        currentTurnSpecialityUp = supportState.nextTurnSpecialityUp,
        nextTurnSpecialityUp = 0,
    )
    return copy(
        supportState = supportState,
        scenarioState = scenarioState,
    )
}

private fun MemberState.selectPosition(turn: Int, state: SimulationState): MemberState {
    // シナリオ友人不在判定
    if (state.scenario == Scenario.LARC && turn < 3 && charaName == "佐岳メイ") return this
    if (state.scenario == Scenario.MUJINTO && turn < 3 && charaName == "タッカーブライン") return this

    // 各トレーニングに配置
    var position: StatusType
    var secondPosition: StatusType
    do {
        position = randomSelect(
            *Calculator.calcCardPositionSelection(
                state.baseCalcInfo,
                this,
                if (guest) 0 else state.specialityRateUp(card.type),
                state.positionRateUp,
                forceSpecialityEnabled = true,
            )
        )
        secondPosition = if (card.hasSecondPosition(relation)) {
            randomSelect(
                *Calculator.calcCardPositionSelection(
                    state.baseCalcInfo,
                    this,
                    supportState?.let {
                        it.currentTurnSpecialityUp + state.specialityRateUp(card.type)
                    } ?: 0,
                    state.positionRateUp,
                )
            )
        } else StatusType.NONE
    } while (position == secondPosition && position != StatusType.NONE)

    return copy(
        position = position,
        additionalPosition = if (secondPosition == StatusType.NONE) emptySet() else setOf(secondPosition),
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

suspend fun SimulationState.applyAction(
    action: Action,
    result: ActionResult,
    selector: ActionSelector = ActionSelector.Random,
): SimulationState {
    if (action is Outing && action.support != null) {
        return applyOutingEvent(action.support, selector).applyIfNotNull(result.scenarioActionParam) {
            applyScenarioActionParam(action, result)
        }
    }
    return when (result) {
        is StatusActionResult -> applyStatusAction(action, result, selector)

        is FriendActionResult -> applyFriendEvent(action, result)

        is ClimaxBuyUseItemResult -> applyIfNotNull(result.buyItem) { buyItem(it) }.applyIfNotNull(result.useItem) {
            applyItem(
                it
            )
        }

        is LiveGetLessonResult -> purchaseLesson(result.lesson)

        is GmActivateWisdomResult -> applySelectedGmAction()

        is LArcGetAptitudeResult -> applySelectedLArcAction(result)

        is UafConsultResult -> applySelectedUafAction(result)

        is CookActivateDishResult -> activateDish(result.dish)

        is CookMaterialLevelUpResult -> updateCookStatus { materialLevelUp(result.target) }

        MechaOverdriveResult -> applyMechaOverdrive()

        is MechaTuningResult -> updateMechaStatus { applyTuning(result) }

        is LegendActionResult -> LegendCalculator.applyScenarioAction(this, result)

        is PRActivityResult -> OnsenCalculator.applyScenarioAction(this, result, selector)

        is MujintoActionResult -> MujintoCalculator.applyScenarioAction(this, result, selector)
    }
}

suspend fun SimulationState.applyStatusAction(
    action: Action,
    result: StatusActionResult,
    selector: ActionSelector,
    baseRelationBonus: Int = 0,
): SimulationState {
    // Action結果反映
    val newState = when (action) {
        is Training -> {
            val newTraining = if (result.success && !isLevelUpTurn) training.map {
                if (action.type == it.type) {
                    it.applyAction(action, scenario.trainingAutoLevelUp)
                } else it
            } else training
            val memberIndices = action.member.map { it.index }
            val trainingHint = selectTrainingHint(action.member, action.type)
            val trainingHintIndices = trainingHint.second.map { it.index }
            val relationBonus = baseRelationBonus +
                    support.sumOf { it.card.trainingRelationAll } +
                    action.support.sumOf { it.card.trainingRelationJoin } +
                    trainingRelationBonus
            val nextTurnSpecialityRateUp = action.support.sumOf { it.card.trainingNextTurnSpecialityRateUp }
            val newMember = member.map {
                if (memberIndices.contains(it.index)) {
                    it.applyTraining(
                        action, charmBonus, relationBonus, nextTurnSpecialityRateUp,
                        chara, trainingHintIndices.contains(it.index), this
                    )
                } else it
            }
            copy(member = newMember, training = newTraining)
                .addStatus(result.status + trainingHint.first + selectAoharuTrainingHint(action.member))
                .applyFriendEvent(action, selector)
                .applyAfterTrainingEvent(action)
        }

        is Race -> {
            addStatus(result.status).copy(
                raceTurns = raceTurns + turn,
                shopCoin = if (itemAvailable && action.grade != RaceGrade.FINALS) shopCoin + 100 else shopCoin,
            )
        }

        else -> {
            addStatus(result.status)
        }
    }
    // シナリオ別結果反映
    return newState.applyScenarioActionParam(action, result)
}

private fun MemberState.applyTraining(
    action: Training,
    charmValue: Int,
    relationBonus: Int,
    nextTurnSpecialityRateUp: Int,
    chara: Chara,
    hint: Boolean,
    state: SimulationState,
): MemberState {
    val scenarioState = when (scenarioState) {
        is AoharuMemberState -> scenarioState.applyTraining(action, chara)
        is LArcMemberState -> scenarioState.applyTraining(action, state.isLevelUpTurn)
        else -> scenarioState
    }
    val relationUp = getTrainingRelation(charmValue, relationBonus, hint)
    // 20%で情熱突入と仮定
    val startPassion =
        supportState != null && (supportState.passionTurn > 0 || (supportState.outingEnabled && Random.nextDouble() < 0.2))
    return copy(scenarioState = scenarioState)
        .addRelation(relationUp)
        .applyIf(startPassion) { startPassion() }
        .applyIf({ action.friendTraining }) {
            copy(supportState = supportState?.copy(friendCount = supportState.friendCount + 1))
        }
        .addNextTurnSpecialityRateUp(nextTurnSpecialityRateUp)
}

private suspend fun SimulationState.applyFriendEvent(action: Training, selector: ActionSelector): SimulationState {
    var state = this
    for (support in action.member.filter { !it.guest && it.outingType }) {
        state = state.applyAfterTrainingEvent(support, selector)
    }
    return state
}

fun SimulationState.applyFriendEvent(action: Action, result: FriendActionResult): SimulationState {
    return applyFriendEvent(result.support, result.status, result.relation, result.outingStep)
        .applyIf({ result.otherRelation > 0 }) {
            val minRelation = support.filter { it.index != result.support.index }.minOf { it.relation }
            val relationTarget = support.filter {
                it.index != result.support.index && it.relation == minRelation
            }.random()
            addRelation(1, relationTarget)
        }
        .applyScenarioActionParam(action, result)
}

fun SimulationState.applyFriendEvent(
    support: MemberState,
    statusUp: Status,
    relation: Int,
    outingStep: Int = 0,
): SimulationState {
    val newMember = member.mapIf({ it.index == support.index }) { member ->
        member.copy(supportState = member.supportState?.let {
            it.copy(
                relation = min(100, it.relation + relation + charmBonus),
                outingStep = max(outingStep, it.outingStep),
            )
        })
    }
    val eventEffect = support.card.eventEffect
    val eventRecovery = support.card.eventRecovery
    val eventStatus = statusUp.copy(
        speed = (statusUp.speed * eventEffect).toInt(),
        stamina = (statusUp.stamina * eventEffect).toInt(),
        power = (statusUp.power * eventEffect).toInt(),
        guts = (statusUp.guts * eventEffect).toInt(),
        wisdom = (statusUp.wisdom * eventEffect).toInt(),
        hp = (statusUp.hp * eventRecovery).toInt(),
    )
    return copy(member = newMember).addStatus(eventStatus)
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

private fun SimulationState.selectTrainingHint(
    support: List<MemberState>,
    position: StatusType,
): Pair<Status, List<MemberState>> {
    return if (allSupportHint(position)) {
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
        hintSupport.selectHint(status, 1 + hintCountPlus) to listOf(hintSupport)
    }
}

private fun MemberState.selectHint(currentStatus: Status, count: Int = 1): Status {
    var result = Status()
    repeat(count) {
        val hintSkill = (card.skills.filter {
            !result.skillHint.containsKey(it) && currentStatus.skillHint.getOrElse(it) { 0 } < 5
        } + "").random()
        result += if (hintSkill.isEmpty()) {
            card.hintStatus
        } else {
            Status(skillHint = mapOf(hintSkill to 1 + card.hintLevel))
        }
    }
    return result
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

/**
 * トレーニング後汎用イベント
 * 20%の確率で体力+5
 */
private fun SimulationState.applyAfterTrainingEvent(action: Training): SimulationState {
    return if (Random.nextDouble() > 0.2) this else addStatus(Status(hp = 5))
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
        is StatusItem -> addStatus(item.status)
        is UniqueItem -> when (item.name) {
            "おいしい猫缶" -> this
            "にんじんBBQセット" -> addRelationAll(5)
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

private fun MemberState.addRelation(relation: Int): MemberState {
    return copy(
        supportState = supportState?.copy(
            relation = min(100, supportState.relation + relation)
        ),
        scenarioState = scenarioState.addRelation(relation),
    )
}

fun SimulationState.addRelationAll(relation: Int): SimulationState {
    return copy(member = member.map { it.addRelation(relation + charmBonus) })
}

fun SimulationState.addRelation(relation: Int, target: (MemberState) -> Boolean): SimulationState {
    return copy(member = member.mapIf(target) { it.addRelation(relation + charmBonus) })
}

fun SimulationState.addRelation(relation: Int, target: MemberState): SimulationState {
    return addRelation(relation) { it.index == target.index }
}

fun SimulationState.allTrainingLevelUp(): SimulationState {
    val newTraining = training.map {
        it.copy(level = min(5, it.level + 1))
    }
    return copy(training = newTraining)
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
        scenarioStatus = liveStatus.copy(
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
    return addStatus(Status(performance = -lesson.cost) + lessonStatus).copy(
        scenarioStatus = liveStatus.copy(
            learnedLesson = liveStatus.learnedLesson + lesson,
        ),
    ).updateLesson()
}

fun SimulationState.addLesson(lesson: Lesson): SimulationState {
    val liveStatus = liveStatus ?: return this
    return copy(
        scenarioStatus = liveStatus.copy(
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
    return state.addStatus(statusUp).copy(
        scenarioStatus = liveStatus.applyLive(),
    )
}

private fun LiveStatus.applyLive(): LiveStatus {
    return copy(
        livedLesson = livedLesson + learnedLesson.subList(livedLesson.size + 1, learnedLesson.size)
    )
}

private fun SimulationState.applyScenarioActionParam(action: Action, result: ActionResult): SimulationState {
    val param = result.scenarioActionParam ?: return this
    return when (param) {
        is GmActionParam -> applyGmAction(param)
        is LArcActionParam -> applyLArcAction(action, param)
        is UafScenarioActionParam -> applyUafAction(param)
        is CookActionParam -> updateCookStatus { addStamp(param.stamp) }
        is MechaActionParam -> MechaCalculator.applyScenarioAction(this, param)
        is LegendActionParam -> LegendCalculator.applyScenarioActionParam(this, action, result, param)
        is OnsenActionParam -> OnsenCalculator.applyScenarioActionParam(this, result, param)
        is MujintoActionParam -> MujintoCalculator.applyScenarioActionParam(this, result, param)
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
            newMember = member.mapIf({ it.card.chara == "ダーレーアラビアン" }) { oldMember ->
                val supportState = oldMember.supportState
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
                oldMember.copy(supportState = newSupportState)
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
    return copy(scenarioStatus = newGmStatus, member = newMember).addStatus(addStatus)
}

private fun SimulationState.applySelectedGmAction(): SimulationState {
    val gmStatus = gmStatus ?: return this
    val effect = gmStatus.activateWisdom()
    return copy(scenarioStatus = effect.first).addStatus(effect.second)
}

fun SimulationState.updateLArcStatus(update: LArcStatus.() -> LArcStatus): SimulationState {
    return copy(scenarioStatus = lArcStatus?.update())
}

fun SimulationState.updateUafStatus(update: UafStatus.() -> UafStatus): SimulationState {
    return copy(scenarioStatus = uafStatus?.update())
}

private fun SimulationState.applyLArcAction(action: Action, lArcAction: LArcActionParam): SimulationState {
    return if (action is SSMatch) {
        val ssMatchMemberIndex = action.member.map { it.index }
        val starGaugeMemberIndex = lArcAction.starGaugeMember.map { it.index }
        val relation = if (condition.contains("愛嬌○")) 9 else 7
        val newMember = member.mapIf({ ssMatchMemberIndex.contains(it.index) }) { oldMemver ->
            val memberState = oldMemver.scenarioState as LArcMemberState
            val next = when (memberState.starLevel % 3) {
                1 -> StarEffect.SkillHint
                2 -> randomSelect(StarEffect.Status to 1, memberState.specialStarEffect to 1)
                else -> if (memberState.nextStarEffect[2] == StarEffect.Status) memberState.specialStarEffect else StarEffect.Status
            }
            val newNextStarEffect = memberState.nextStarEffect.takeLast(2) + next
            val newMemberState = memberState.copy(
                starLevel = memberState.starLevel + 1,
                starGauge = if (starGaugeMemberIndex.contains(oldMemver.index)) 3 else 0,
                nextStarEffect = newNextStarEffect,
                supporterPt = memberState.supporterPt + random(300, 500),
            )
            oldMemver.copy(scenarioState = newMemberState).addRelation(relation + charmBonus)
        }
        updateLArcStatus {
            val newTotalSSMatchCount = totalSSMatchCount + ssMatchMemberIndex.size
            copy(
                supporterPt = supporterPt + lArcAction.supporterPt,
                aptitudePt = aptitudePt + lArcAction.aptitudePt,
                ssMatchCount = if (isSSSMatch == true) 0 else ssMatchCount + ssMatchMemberIndex.size,
                totalSSMatchCount = newTotalSSMatchCount,
                isSSSMatch = null,
            ).applyIf(newTotalSSMatchCount >= 2 && overseasTurfAptitude == 0) {
                copy(
                    overseasTurfAptitude = 1,
                    longchampAptitude = 1,
                )
            }.applyIf(newTotalSSMatchCount >= 10 && lifeRhythm == 0) {
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
            val newMember = member.mapIf({ it.charaName == "佐岳メイ" }) {
                it.addRelation(5 + charmBonus)
            }
            val addStatus = Status(guts = 3, skillPt = 3, motivation = if (Random.nextDouble() < 0.5) 1 else 0)
            val base = copy(member = newMember).addStatus(addStatus)
            if (isLevelUpTurn) {
                base.updateLArcStatus { copy(aptitudePt = aptitudePt + 50) }
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
                val matchMember = base.member.mapIf({ targets.contains(it.index) }) {
                    val lArcState = it.scenarioState as LArcMemberState
                    it.copy(scenarioState = lArcState.copy(starGauge = lArcState.starGauge + 1))
                }
                base.copy(member = matchMember)
            }
        } else if (action is Race) {
            val points = when {
                // 海外では6着以降
                turn == 41 || turn == 43 || turn == 65 || turn == 67 -> null
                // 国内では2～4着
                action.grade == RaceGrade.G1 -> arrayOf(700, 450, 300)
                action.grade == RaceGrade.G2 -> arrayOf(450, 300, 200)
                action.grade == RaceGrade.G3 -> arrayOf(300, 200, 100)
                else -> null
            }
            if (points == null) this else {
                val joinMember = member.filter { !it.outingType }.shuffled().take(3)
                    .mapIndexed { index, member -> member.index to index }.toMap()
                val newMember = member.map {
                    val point = joinMember[it.index]
                    if (point == null) it else {
                        val lArcState = it.scenarioState as LArcMemberState
                        it.copy(scenarioState = lArcState.copy(supporterPt = lArcState.supporterPt + point))
                    }
                }
                copy(member = newMember)
            }
        } else this
        newState.updateLArcStatus {
            copy(
                supporterPt = supporterPt + lArcAction.supporterPt,
                aptitudePt = aptitudePt + lArcAction.aptitudePt,
            )
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

fun SimulationState.applySelectedLArcAction(result: LArcGetAptitudeResult): SimulationState {
    return updateLArcStatus { addAptitude(result.aptitude) }
}

fun SimulationState.applySelectedUafAction(result: UafConsultResult): SimulationState {
    return updateUafStatus { consult(result.from, result.to) }
}

private fun SimulationState.applyUafAction(uafAction: UafScenarioActionParam): SimulationState {
    val uafStatus = uafStatus ?: return this
    val newLevel = sumMapOf(uafStatus.athleticsLevel, uafAction.athleticsLevelUp.mapKeys {
        uafStatus.trainingAthletics[it.key]!!
    })
    val newUafStatus = uafStatus.copy(
        athleticsLevel = newLevel,
        levelUpBonus = uafAction.notTraining,
    ).applyIf(!uafAction.notTraining) {
        copy(heatUp = heatUp.mapValues { max(0, it.value - 1) })
    }
    return copy(scenarioStatus = newUafStatus.applyHeatUpFrom(uafStatus))
}

fun SimulationState.startPassion(target: MemberState): SimulationState {
    return copy(
        member = member.mapIf({ it.index == target.index }) { it.startPassion() }
    )
}

private fun MemberState.startPassion(): MemberState {
    val supportState = supportState ?: return this
    if (card.type != StatusType.GROUP) return this
    return if (supportState.passion) {
        // 情熱ゾーン中に踏むと20%の確率で1ターン延長と仮定
        if (supportState.passionTurn < 6 && Random.nextDouble() < 0.2) {
            copy(supportState = supportState.copy(passionTurn = supportState.passionTurn + 1))
        } else this
    } else {
        val passionTurn = randomSelect(
            3 to 60, 4 to 20, 5 to 10, 6 to 10,
        )
        copy(supportState = supportState.copy(passionTurn = passionTurn))
    }
}

fun MemberState.addNextTurnSpecialityRateUp(rate: Int): MemberState {
    val supportState = supportState ?: return this
    return copy(
        supportState = supportState.copy(
            nextTurnSpecialityUp = supportState.nextTurnSpecialityUp + rate,
        )
    )
}

fun SimulationState.addNextTurnSpecialityRateUpAll(rate: Int): SimulationState {
    return copy(member = member.map { it.addNextTurnSpecialityRateUp(rate) })
}

fun SimulationState.addRandomSupportHint(): SimulationState {
    val target = support.filter { !it.outingType }.randomOrNull() ?: return this
    return addStatus(target.selectHint(status))
}
