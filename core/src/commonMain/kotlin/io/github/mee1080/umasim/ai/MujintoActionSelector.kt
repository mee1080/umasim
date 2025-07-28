/*
 * Copyright 2024 mee1080
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
package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.mujinto.MujintoStatus
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

class MujintoActionSelector(
    private val options: List<Option>,
) : ActionSelector {

    companion object {
        private const val DEBUG = false

        val defaultOption = Option()
    }

    @Serializable
    data class Option(
        val status: Int = 10,
        val skillPt: Int = 100,
        val hp: Int = 5,
        val motivation: Int = 10000,

        val training: Int = 100,
        val race: Int = 60,

        val relation: Int = 2200,
        val risk: Int = 120,
        val ignoreFailureRate: Int = 0,
        val sleepHp: Int = 60,

        val restPointBuffer: Int = 70,
        val keepRestPoint: Int = 300,

        val baseIslandTraining: Int = 6500,
    ) : SerializableActionSelectorGenerator {
        override fun generateSelector() = MujintoActionSelector(listOf(this))
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    private class Context(
        val option: Option,
        val state: SimulationState,
    ) {
        operator fun component1() = option
        operator fun component2() = state
    }

    override fun toString(): String {
        return "MujintoActionSelector(${options.joinToString()})"
    }

    override suspend fun select(
        state: SimulationState,
        selection: List<Action>
    ): Action {
        return selectWithScore(state, selection).first
    }

    override suspend fun selectWithScore(
        state: SimulationState,
        selection: List<Action>
    ): Triple<Action, List<Double>, Double> {
        val first = selection.first()
        if (selection.size == 1) {
            return Triple(first, emptyList(), 0.0)
        }
        val mujintoStatus = state.mujintoStatus ?: return Triple(first, emptyList(), 0.0)
        val context = Context(options[0], state)

        // 施設計画
        if (first is MujintoAddPlan) {
            return selectWithScore(context, selection, ::calcMujintoAddPlanScore).convert()
        }

        // 友人
        if (first is FriendAction) {
            return selectWithScore(context, selection, ::calcFriendActionScore).convert()
        }

        // 島トレ強制判定
        val islandTraining = selection.firstOrNull { it is MujintoTraining }
        val threeFacility = mujintoStatus.facilities.count { entry ->
            state.support.any {
                it.card.type == entry.key && it.friendTrainingEnabled
            }
        } >= 3 && mujintoStatus.getFacilityLevel(StatusType.FRIEND) >= 1
        if (islandTraining is MujintoTraining && forceIslandTraining(context, islandTraining, threeFacility)) {
            return Triple(islandTraining, emptyList(), 0.0)
        }

        val result = if (state.isLevelUpTurn) {
            // 島合宿
            selectWithScoreCamp(context, selection)
        } else if (state.turn <= 2 || state.turn >= 65 || mujintoStatus.pioneerPoint >= mujintoStatus.requiredPoint2) {
            // 最序盤 or シニア後半 or 2枚目島トレ券獲得済み（島トレ券獲得の考慮が不要）
            selectWithScoreTicketCompleted(context, selection)
        } else if (mujintoStatus.islandTrainingTicket >= 1) {
            // 島トレ券キープ中
            selectWithScoreKeepTicket(context, selection)
        } else {
            // 島トレ券なし
            selectWithScoreGetTicket(context, selection)
        }

        // 3施設友情不可、島トレ可能で、選択した行動の評価が一定以下なら島トレ
        if (!threeFacility && islandTraining != null && result.first.second < context.option.baseIslandTraining) {
            val selected = result.second.firstOrNull { it.first is MujintoTraining }
            if (selected != null) {
                return (selected to result.second).convert()
            }
        }

        return result.convert()
    }

    private fun calcStatusScore(option: Option, status: Status): Int {
        return status.speed * option.status +
                status.stamina * option.status +
                status.power * option.status +
                status.guts * option.status +
                status.wisdom * option.status +
                status.skillPt * option.skillPt +
                status.hp * option.hp +
                status.motivation * option.motivation
    }

    private fun calcActionScore(context: Context, action: Action): Double {
        val total = action.candidates.sumOf { (result, rate) ->
            rate * if (result.success) 100 else context.option.risk
        }.toDouble()
        return action.candidates.sumOf { (result, rate) ->
            val statusResult = result as? StatusActionResult ?: return@sumOf 0.0
            val statusScore = calcStatusScore(context.option, statusResult.status)
            val relationScore = if (action is Training && result.success) {
                action.support.sumOf {
                    if (it.relation < it.card.requiredRelation) context.option.relation else 0
                }
            } else 0
            if (DEBUG) println("  $rate $statusScore $relationScore $result $rate/$total")
            (statusScore + relationScore) * rate / total
        }
    }

    private fun selectWithScoreCamp(
        context: Context,
        selection: List<Action>,
    ): Pair<Pair<Action, Double>, List<Pair<Action, Double>>> {
        // 島合宿ではスキルPtが最も高いトレーニングを選択
        return selectWithScore(context, selection) { context, action ->
            if (action !is Training) return@selectWithScore 0.0
            context.option.training * calcActionScore(context, action)
        }
    }

    private fun selectSleep(list: List<Pair<Action, Double>>): Pair<Action, Double>? {
        val friendOuting = list.firstOrNull { (it.first as? Outing)?.support != null }
        if (friendOuting != null) {
            return friendOuting
        } else {
            val sleep = list.firstOrNull { it.first is Sleep }
            if (sleep != null) {
                return sleep
            }
        }
        return null
    }

    private fun selectWithScoreTicketCompleted(
        context: Context,
        selection: List<Action>
    ): Pair<Pair<Action, Double>, List<Pair<Action, Double>>> {
        // 島トレ券獲得の考慮が不要の場合、スキルPtが高いトレーニングまたはレースを選択
        val (option, state) = context
        var (selected, list) = selectWithScore(context, selection) { context, action ->
            val factor = when (action) {
                is Training -> option.training
                is Race -> option.race
                else -> return@selectWithScore 0.0
            }
            factor * calcActionScore(context, action)
        }
        // レースor失敗率の高いトレーニング選択時は体力一定以下でお休み
        if (
            (selected.first is Race && state.status.maxHp - state.status.hp >= option.sleepHp)
            || ((selected.first as? Training)?.failureRate ?: 0) > option.ignoreFailureRate
        ) {
            selectSleep(list)?.let {
                selected = it
            }
        }
        return selected to list
    }

    private fun selectWithScoreKeepTicket(
        context: Context,
        selection: List<Action>
    ): Pair<Pair<Action, Double>, List<Pair<Action, Double>>> {
        // 島トレ券キープ中の場合
        val (option, state) = context
        val mujintoStatus = state.mujintoStatus!!
        val restPoint = calcRestPoint(mujintoStatus)

        // 次の島トレ券が遠い場合、島トレ券が無い場合と同じ
        if (restPoint >= option.keepRestPoint) {
            return selectWithScoreGetTicket(context, selection)
        }

        // 次の島トレ券が近い場合、島トレ券獲得の考慮が不要の場合と同じ
        var (selected, list) = selectWithScoreTicketCompleted(context, selection)

        // 選択した行動で島トレ券を獲得する場合は島トレorレース
        val maxPioneerPoint = selected.first.candidates.maxOf {
            (it.first.scenarioActionParam as? MujintoActionParam)?.pioneerPoint ?: 0
        }
        if (DEBUG) println("check islandTraining maxPioneerPoint: $maxPioneerPoint restPoint: $restPoint")
        if (maxPioneerPoint + option.restPointBuffer >= restPoint) {
            val islandTraining = list.firstOrNull { it.first is MujintoTraining }
            if (selected.first is Race) {
                if (islandTraining != null) {
                    selected = islandTraining
                }
            } else {
                val race = list.firstOrNull { it.first is Race }
                val racePioneerPoint = (race?.first?.randomSelectResult()?.scenarioActionParam as? MujintoActionParam)
                    ?.pioneerPoint ?: 0
                if (race == null || racePioneerPoint >= restPoint || race.first.randomSelectResult().status.motivation < 0) {
                    if (state.status.maxHp - state.status.hp >= option.sleepHp) {
                        selected = selectSleep(list) ?: islandTraining ?: selected
                    } else if (islandTraining != null) {
                        selected = islandTraining
                    }
                } else {
                    selected = race
                }
            }
        }
        return selected to list
    }

    private fun selectWithScoreGetTicket(
        context: Context,
        selection: List<Action>
    ): Pair<Pair<Action, Double>, List<Pair<Action, Double>>> {
        // 島トレ券未獲得の場合、トレーニングを優先
        var (selected, list) = selectWithScore(context, selection) { context, action ->
            if (action !is Training) return@selectWithScore 0.0
            context.option.training * calcActionScore(context, action)
        }
        // レースor失敗率の高いトレーニング選択時は体力一定以下でお休み
        val (option, state) = context
        if (
            (selected.first is Race && state.status.maxHp - state.status.hp >= option.sleepHp)
            || ((selected.first as? Training)?.failureRate ?: 0) > option.ignoreFailureRate
        ) {
            selectSleep(list)?.let {
                selected = it
            }
        }
        if (DEBUG) println("selected: ${selected.first.toShortString()} list: ${list.joinToString { it.second.toString() }}")
        return selected to list
    }

    private fun Pair<Pair<Action, Double>, List<Pair<Action, Double>>>.convert(): Triple<Action, List<Double>, Double> {
        val (selected, list) = this
        return Triple(selected.first, list.map { it.second }, selected.second)
    }

    private fun selectWithScore(
        context: Context,
        selection: List<Action>,
        calc: (Context, Action) -> Double,
    ): Pair<Pair<Action, Double>, List<Pair<Action, Double>>> {
        val list = selection.map {
            val score = calc(context, it)
            if (DEBUG) println("action: ${it.toShortString()} score: $score")
            it to score
        }
        val selected = list.maxBy { it.second }
        return selected to list
    }

    private fun calcMujintoAddPlanScore(context: Context, action: Action): Double {
        val facility = (action as? MujintoAddPlan)?.result?.facility ?: return 0.0
        return when (context.state.turn) {
            2 -> {
                // ジュニア前半：海/スピスタ
                when (facility.type) {
                    StatusType.FRIEND -> 2.0
                    StatusType.SPEED, StatusType.STAMINA -> 1.0
                    else -> 0.0
                }
            }

            12 -> {
                // ジュニア後半：パワ賢さ/根性スピスタ
                when (facility.type) {
                    StatusType.POWER, StatusType.WISDOM -> 2.0
                    StatusType.SPEED, StatusType.STAMINA, StatusType.GUTS -> 1.0
                    else -> 0.0
                }
            }

            24 -> {
                // クラシック前半：スピパワ/スタ根性
                when (facility.type) {
                    StatusType.SPEED if (!facility.jukuren) -> 2.0
                    StatusType.POWER -> 2.0
                    StatusType.STAMINA if (!facility.jukuren) -> 1.0
                    StatusType.GUTS -> 1.0
                    else -> 0.0
                }
            }

            36 -> {
                // クラシック後半：スピ/スタパワ
                when (facility.type) {
                    StatusType.SPEED -> 2.0
                    StatusType.STAMINA -> 1.0
                    StatusType.POWER if (!facility.jukuren) -> 1.0
                    else -> 0.0
                }
            }

            48 -> {
                // クラシック後半：スピ/スタ根性
                when (facility.type) {
                    StatusType.SPEED -> 2.0
                    StatusType.STAMINA -> 1.0
                    StatusType.GUTS if (!facility.jukuren) -> 1.0
                    else -> 0.0
                }
            }

            else -> 0.0
        }
    }

    private fun calcFriendActionScore(context: Context, action: Action): Double {
        val mujintoStatus = context.state.mujintoStatus ?: return 0.0
        if (action.randomSelectResult().scenarioActionParam !is MujintoActionParam) return 0.0
        return if (mujintoStatus.islandTrainingTicket == 0 || calcRestPoint(mujintoStatus) >= 80 + context.option.restPointBuffer) 1.0 else -1.0
    }

    private fun forceIslandTraining(context: Context, action: MujintoTraining, threeFacility: Boolean): Boolean {
        val (option, state) = context
        val mujintoStatus = state.mujintoStatus ?: return false

        // クラシック合宿直前で島トレ券がある場合は島トレ
        if (state.turn >= 34 && state.turn <= 36) {
            val restTurn = 37 - state.turn - state.goalRace.count { it.turn <= 36 && it.turn >= state.turn }
            if (DEBUG) println("check islandTraining restTurn: $restTurn")
            if (restTurn <= mujintoStatus.islandTrainingTicket) {
                return true
            }
        }

        // ファイナルズ直前で島トレ券が残る場合は島トレ
        if (state.turn >= 64) {
            val restTurn = 73 - state.turn - (state.goalRace.count { it.turn >= state.turn } - 3)
            if (DEBUG) println("check islandTraining restTurn: $restTurn")
            if (restTurn <= mujintoStatus.islandTrainingTicket) {
                return true
            }
        }

        // 3施設以上友情が可能な場合、3施設以上友情なら実行、それ以外は待つ
        if (
            mujintoStatus.getFacilityLevel(StatusType.FRIEND) >= 1 &&
            mujintoStatus.facilities.count {
                it.value.type != StatusType.FRIEND && state.support.any { it.friendTrainingEnabled }
            } >= 3
        ) {
            val friendFacilityCount = action.result.member
                .groupBy { it.position }
                .count { position -> position.value.any { it.isFriendTraining(position.key) } }
            if (DEBUG) println("  friendFacilityCount: $friendFacilityCount")
            if (friendFacilityCount >= 3) {
                return true
            }
        }

        return false
    }

    /**
     * 次の島トレ券までに必要な発展Pt
     */
    private fun calcRestPoint(status: MujintoStatus): Int {
        return if (status.pioneerPoint >= status.requiredPoint2) {
            Int.MAX_VALUE
        } else if (status.pioneerPoint >= status.requiredPoint1) {
            status.requiredPoint2 - status.pioneerPoint
        } else {
            status.requiredPoint1 - status.pioneerPoint
        }
    }
}
