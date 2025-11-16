package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.RaceGrade
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.onsen.Gensen
import io.github.mee1080.umasim.scenario.onsen.OnsenCalculator
import io.github.mee1080.umasim.scenario.onsen.OnsenStatus
import io.github.mee1080.umasim.scenario.onsen.StratumType
import io.github.mee1080.umasim.simulation2.*
import kotlinx.serialization.Serializable

class OnsenActionSelector2(
    val option: Option,
) : ActionSelector {

    companion object {
        const val DEBUG = false
        const val FORCE = 10000000.0
    }

    @Serializable
    data class Option(
        val status: Int = 10,
        val skillPt: Int = 100,
        val hp: Int = 1,
        val motivation: Int = 100,
        val relation: Int = 1000,
        val risk: Int = 145,
        val keepHp: Int = 88,
        val dig: Int = 100,
        val bathThreshold: Int = 145,

        val digSpeed: Int = 268,

        val gensenOrder: List<Pair<String, Int>> = listOf(
            "伝説の秘湯" to 69,
            "秘湯ゆこま" to 57,
            "明晰の湯" to 11,
            "堅忍の湯" to 21,
            "天翔の古湯" to 33,
            "剛脚の古湯" to 48,
            "駿閃の古湯" to 99,
            "疾駆の湯" to 65,
            "健壮の古湯" to 99,
        ),

        val equipmentOrder: List<Pair<StratumType, Int>> = listOf(
            StratumType.ROCK to 2,
            StratumType.SOIL to 2,
            StratumType.SAND to 2,
            StratumType.SOIL to 3,
            StratumType.SAND to 4,
            StratumType.ROCK to 3,
            StratumType.SOIL to 6,
            StratumType.SAND to 6,
            StratumType.ROCK to 6,
        ),

        val outingStepRange: List<Triple<Int, Int, Int>> = listOf(
            Triple(1, 2, 3),
            Triple(24, 3, 3),
            Triple(25, 3, 5),
            Triple(36, 4, 5),
            Triple(41, 4, 6),
            Triple(43, 5, 6),
            Triple(48, 6, 6),
            Triple(53, 6, 7),
            Triple(55, 7, 7),
        ),

        val fixStartTurn: Int = 49,

        val keepTicketCount: List<Pair<Int, Int>> = listOf(
            49 to 2,
            50 to 1,
            52 to 3,
            51 to 0,
        ),

        val forceRaceTurn: List<Int> = listOf(
            24,
        ),
    ) : SerializableActionSelectorGenerator {
        override fun generateSelector() = OnsenActionSelector2(this)
        override fun serialize() = serializer.encodeToString(this)
        override fun deserialize(serialized: String) = serializer.decodeFromString<Option>(serialized)
    }

    override suspend fun select(
        state: SimulationState,
        selection: List<Action>,
    ) = selectWithScore(state, selection).first

    override suspend fun selectWithScore(
        state: SimulationState,
        selection: List<Action>,
    ): Triple<Action, List<Double>, Double> {
        val restDigTurn = calcRestDigTurn(state)
        val canUseTicket = canUseTicket(state)
        val scores = selection.map { it to calc(state, it, canUseTicket, restDigTurn) }
        var selected = scores.maxBy { it.second }
        selected = checkAlternative(state, scores, selected, canUseTicket, restDigTurn) to selected.second + 1.0
        return Triple(selected.first, scores.map { it.second }, selected.second)
    }

    private fun checkAlternative(
        state: SimulationState,
        scores: List<Pair<Action, Double>>,
        selected: Pair<Action, Double>,
        canUseTicket: Boolean,
        restDigTurn: Double,
    ): Action {
        val (action, score) = selected
        if (score == FORCE) return action
        val onsenStatus = state.onsenStatus ?: return action

        val (outing, _) = scores.firstOrNull { it.first is Outing && it.second > 0.0 } ?: (null to null)
        val (bathing, _) = if (canUseTicket) {
            scores.firstOrNull { it.first is OnsenBathing } ?: (null to null)
        } else (null to null)
        if (DEBUG) println("checkAlternative ${action.name}:$score to ${outing?.let { "out" }} ${bathing?.let { "bath" }}")
        if (outing == null && bathing == null) return action

        // HP不足でトレーニングが選べない場合はお出かけまたは入浴
        if (state.status.hp < 50 && (action !is Training || action.type == StatusType.WISDOM)) {
            val tempState = state.addStatus(Status(hp = 100))
            val maxTraining = OnsenCalculator.predictScenarioActionParams(
                state, tempState.predictTrainingResult().toList()
            ).map { it to calc(tempState, it, canUseTicket, restDigTurn) }.maxBy { it.second }
            if (DEBUG) println("LowHP ${maxTraining.first.name}:${maxTraining.second}")
            if (maxTraining.second > score) return outing ?: bathing!!
        }

        if (outing != null) {
            // GI以外のレースはお出かけ
            if (action is Race && action.grade != RaceGrade.G1) return outing

            // 下振れトレーニングはお出かけ（人数で判定）
            if (action is Training) {
                if (action.support.size <= 1) return outing
            }
        }

        if (bathing != null) {
            if (!canUseTicket) return action

            // 超回復なら入浴
            if (onsenStatus.superRecoveryAvailable) return bathing

            // 特定ターン以降は強制入浴
            if (state.turn >= option.fixStartTurn) return bathing

            // 目標レースは入浴
            if (action is Race && action.goal) return bathing

            if (action is Training) {
                // 失敗率が0でない場合は入浴
                if (action.failureRate > 0) return bathing

                // 友情トレーニングでボーナスが入る場合は入浴
                if (action.friendTraining && onsenStatus.excavatedGensenContinuousEffect.friendBonus.getOrElse(action.type) { 0 } > 0) {
                    return bathing
                }
            }

            // 入浴時の強化幅が大きい場合は入浴
            val tempState = OnsenCalculator.applyScenarioAction(
                state, bathing.randomSelectResult() as OnsenActionResult,
            )
            val maxTraining = OnsenCalculator.predictScenarioActionParams(
                state, tempState.predictTrainingResult().toList()
            ).map { it to calc(tempState, it, canUseTicket, restDigTurn) }.maxBy { it.second }
            if (DEBUG) println("LowHP ${maxTraining.first.name}:${maxTraining.second}")
            if (maxTraining.second > score * option.bathThreshold / 100.0) return bathing
        }

        return action
    }

    private fun canUseTicket(state: SimulationState): Boolean {
        val onsenStatus = state.onsenStatus ?: return false
        val keep = option.keepTicketCount.lastOrNull { state.turn >= it.first }?.second ?: 0
        if (DEBUG) println("canUseTicket keep=$keep ticket=${onsenStatus.onsenTicket}")
        return onsenStatus.onsenTicket > keep
    }

    private fun calc(state: SimulationState, action: Action, canUseTicket: Boolean, restDigTurn: Double): Double {
        return when (action) {
            is OnsenSelectGensen -> calcGensen(state, action)
            is OnsenSelectEquipment -> calcEquipment(state, action)
            is OnsenPR -> 0.0
            is OnsenBathing -> if (canUseTicket) 1.0 else 0.0
            is Race -> calcRace(state, action, restDigTurn, canUseTicket)
            is Sleep if state.turn >= 3 -> 0.0
            is Outing -> calcOuting(state, action, restDigTurn)
            else -> calcActionScore(state, action, canUseTicket)
        }
    }

    private fun calcGensen(state: SimulationState, action: OnsenSelectGensen): Double {
        if (state.turn in 57..60) {
            val onsenStatus = state.onsenStatus!!
            val progress = onsenStatus.suspendedGensen[action.gensen.name] ?: 0
            val restTurn = calcRestDigTurn(onsenStatus, action.gensen, state.status, progress)
            if (DEBUG) println("Gensen ${action.gensen.name}:$restTurn")
            if (restTurn <= 60 - state.turn) return 0.0
            return 100.0 - restTurn
        } else if (state.turn == 66) {
            val onsenStatus = state.onsenStatus!!
            val selected = onsenStatus.selectedGensen!!
            if (selected.name == action.gensen.name) {
                val restTurn = calcRestDigTurn(onsenStatus, selected, state.status, onsenStatus.digProgress)
                if (restTurn < 1.0) return FORCE
            }
        }
        val order = option.gensenOrder.indexOfFirst { it.first == action.gensen.name }
        return if (order < 0) 0.0 else 100.0 - order
    }

    private fun calcEquipment(state: SimulationState, action: OnsenSelectEquipment): Double {
        val onsenStatus = state.onsenStatus ?: return 0.0
        val order = option.equipmentOrder.indexOfFirst {
            action.equipment == it.first && onsenStatus.equipmentLevel[it.first]!! < it.second
        }
        return if (order < 0) 0.0 else 100.0 - order
    }

    private fun calcOuting(state: SimulationState, action: Outing, restDigTurn: Double): Double {
        if (action.support == null) return 0.0
        val onsenStatus = state.onsenStatus ?: return 0.0

        // 最小回数未満なら強制、最大回数以上なら不可
        val stepRange = option.outingStepRange.lastOrNull { state.turn >= it.first }
        if (stepRange == null) return 0.0
        val step = action.support.supportState?.outingStep ?: Int.MAX_VALUE
        if (step < stepRange.second) return FORCE
        if (step >= stepRange.third) return 0.0

        // 入浴中は回避
        if (onsenStatus.onsenActiveTurn > 0) return 0.0

        // 入浴券0枚で直近で掘削完了しない場合は入浴
        if (onsenStatus.onsenTicket == 0 && restDigTurn >= 2.0) return FORCE

        // 超回復可能状態なら回避
        if (onsenStatus.superRecoveryAvailable) return 0.0

        // 入浴券が溢れる場合は回避
        if (onsenStatus.onsenTicket + 1 - (restDigTurn / 2).toInt() > 3) return 0.0

        return 1.0
    }

    private fun calcRace(state: SimulationState, action: Race, restDigTurn: Double, canUseTicket: Boolean): Double {
        if (action.goal) return 1.0
        if (action.result.status.motivation < 0) return 0.0
        if (option.forceRaceTurn.contains(state.turn)) return FORCE
        val onsenStatus = state.onsenStatus ?: return 0.0
        val gensen = onsenStatus.selectedGensen ?: return 0.0
        val digLimit = option.gensenOrder.firstOrNull { it.first == gensen.name }?.second ?: return 0.0
        if (digLimit - state.turn - restDigTurn < 15.0 / option.digSpeed) return 0.0
        return calcActionScore(state, action, canUseTicket)
    }

    private fun calcActionScore(state: SimulationState, action: Action, canUseTicket: Boolean): Double {
        val total = action.candidates.sumOf { (result, rate) ->
            rate * if (result.success) 100 else option.risk
        }.toDouble()
        return action.candidates.sumOf { (result, rate) ->
            val statusResult = result as? StatusActionResult ?: return@sumOf 0.0
            val statusScore = calcStatusScore(statusResult.status)
            val relationScore = if (action is Training && result.success) {
                action.support.sumOf {
                    if (it.relation < it.card.requiredRelation) option.relation else 0
                }
            } else 0
            val scenarioScore = ((result.scenarioActionParam as? OnsenActionParam)?.digPoint ?: 0) * option.dig
            val restHp = state.status.hp + result.status.hp
            val keepHpFactor = if (restHp < 50 && state.turn < option.fixStartTurn && !canUseTicket) {
                option.keepHp
            } else 100
            val totalScore = (statusScore + relationScore + scenarioScore) * keepHpFactor
            if (DEBUG) println("  $rate ($statusScore + $relationScore + $scenarioScore) * $keepHpFactor = $totalScore $result $rate/$total")
            totalScore * rate / total
        }
    }

    private fun calcStatusScore(status: Status): Int {
        return status.speed * option.status +
                status.stamina * option.status +
                status.power * option.status +
                status.guts * option.status +
                status.wisdom * option.status +
                status.skillPt * option.skillPt +
                status.hp * option.hp +
                status.motivation * option.motivation
    }

    private fun calcRestDigTurn(state: SimulationState): Double {
        val onsenStatus = state.onsenStatus ?: return Double.MAX_VALUE
        val gensen = onsenStatus.selectedGensen ?: return Double.MAX_VALUE
        return calcRestDigTurn(onsenStatus, gensen, state.status, onsenStatus.digProgress)
    }

    private fun calcRestDigTurn(
        onsenStatus: OnsenStatus,
        gensen: Gensen,
        status: Status,
        currentProgress: Int,
    ): Double {
        var totalTurn = 0.0
        var progress = currentProgress
        if (DEBUG) println("restDigTurn ${gensen.name} progress:$currentProgress")
        gensen.strata.forEach {
            if (progress >= it.second) {
                progress -= it.second
                if (DEBUG) println("  ${it.first} finished")
            } else {
                val digPower = OnsenCalculator.calcDigPower(onsenStatus, status, it.first)
                totalTurn += (it.second - progress) / (option.digSpeed * (100 + digPower) / 1000.0)
                progress = 0
                if (DEBUG) println("  ${it.first}: (${it.second} - $progress) / (${option.digSpeed} * (100 + $digPower) / 1000.0)")
            }
        }
        if (DEBUG) println("  total $totalTurn")
        return totalTurn
    }
}
