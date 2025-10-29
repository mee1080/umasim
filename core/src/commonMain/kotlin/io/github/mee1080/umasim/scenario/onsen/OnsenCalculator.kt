package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.roundToInt

data class OnsenActionParam(
    val excavationProgress: Map<StrataType, Int>
) : ScenarioActionParam

data class PRActivityResult(
    override val status: Status,
    val tickets: Int,
    val excavationProgress: Map<StrataType, Int>,
    override val success: Boolean = true,
) : ActionResult, ScenarioActionResult

data class PRActivity(
    override val result: PRActivityResult
) : Action, ScenarioAction

object OnsenCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val onsenStatus = info.onsenStatus ?: return Status()
        val innRankBonus = innRankBonuses.getOrElse(onsenStatus.innRank - 1) { innRankBonuses.last() }
        val trainingEffect = innRankBonus.trainingEffectUp
        return Status(
            speed = base.speed * trainingEffect / 100,
            stamina = base.stamina * trainingEffect / 100,
            power = base.power * trainingEffect / 100,
            guts = base.guts * trainingEffect / 100,
            wisdom = base.wisdom * trainingEffect / 100,
            skillPt = base.skillPt * trainingEffect / 100,
        )
    }

    private fun calcExcavationPower(state: SimulationState, trainingType: StatusType?): Map<StrataType, Int> {
        val onsenStatus = state.onsenStatus ?: return emptyMap()
        val chara = state.chara
        val blueFactorBonus = (chara.speedFactor + chara.staminaFactor + chara.powerFactor + chara.gutsFactor + chara.wisdomFactor) / 20.0

        val powerMap = mutableMapOf<StrataType, Double>()

        fun getStatForPower(type: EquipmentType): Double {
            val stats = equipmentTrainingType[type]!!
            return (chara.status.get(stats[0]) + chara.status.get(stats[1]) + chara.status.get(stats[2])) / 3.0
        }

        val holeDiggerPower = (getStatForPower(EquipmentType.HOLE_DIGGER) / 10.0) * (1 + onsenStatus.equipmentLevel[EquipmentType.HOLE_DIGGER]!! * 0.1) + onsenStatus.sandPower + blueFactorBonus
        powerMap[StrataType.SAND] = holeDiggerPower

        val earthDrillPower = (getStatForPower(EquipmentType.EARTH_DRILL) / 10.0) * (1 + onsenStatus.equipmentLevel[EquipmentType.EARTH_DRILL]!! * 0.1) + onsenStatus.earthPower + blueFactorBonus
        powerMap[StrataType.EARTH] = earthDrillPower

        val metalCrownPower = (getStatForPower(EquipmentType.METAL_CROWN) / 10.0) * (1 + onsenStatus.equipmentLevel[EquipmentType.METAL_CROWN]!! * 0.1) + onsenStatus.rockPower + blueFactorBonus
        powerMap[StrataType.ROCK] = metalCrownPower

        // Training type bonus
        if (trainingType != null) {
            equipmentTrainingType.forEach { (eq, types) ->
                if (trainingType in types) {
                    val strata = equipmentStrata[eq]!!
                    powerMap[strata] = powerMap[strata]!! * 1.2
                }
            }
        }

        return powerMap.mapValues { it.value.roundToInt() }
    }

    override fun predictScenarioActionParams(state: SimulationState, baseActions: List<Action>): List<Action> {
        if (state.isLevelUpTurn) return baseActions // No excavation during summer camp

        val onsenStatus = state.onsenStatus ?: return baseActions
        if (onsenStatus.activeGensen == null) return baseActions

        return baseActions.map { action ->
            when (action) {
                is Training -> {
                    val progress = calcExcavationPower(state, action.type)
                    action.copy(candidates = action.addScenarioActionParam(OnsenActionParam(progress)))
                }
                is Race -> {
                    val progress = calcExcavationPower(state, null)
                    action.copy(result = action.result.addScenarioActionParam(OnsenActionParam(progress)))
                }
                is Rest, is GoOut -> {
                     val progress = calcExcavationPower(state, null).mapValues { (it.value * 0.5).roundToInt() }
                     action.copy(result = action.result.addScenarioActionParam(OnsenActionParam(progress)))
                }
                else -> action
            }
        }
    }

    override fun predictScenarioAction(state: SimulationState, goal: Boolean): Array<Action> {
        val onsenStatus = state.onsenStatus ?: return emptyArray()
        if (goal || state.isLevelUpTurn || onsenStatus.activeGensen == null) return emptyArray()

        // PR活動
        val progress = calcExcavationPower(state, null)
        val tickets = if (state.support.any { it.card.chara == "保科健子" }) 2 else 1
        val result = PRActivityResult(
            status = Status(all = 5, hp = -10),
            tickets = tickets,
            excavationProgress = progress,
        )
        return arrayOf(PRActivity(result))
    }

    suspend fun applyScenarioAction(state: SimulationState, result: OnsenActionResult, selector: ActionSelector): SimulationState {
        return when (result) {
            is PRActivityResult -> {
                state.updateOnsenStatus {
                    copy(bathTickets = (bathTickets + result.tickets).coerceAtMost(3))
                }.applyStatusAction(
                    PRActivity(result),
                    result,
                    selector
                )
            }
            else -> state
        }
    }

    fun applyScenarioActionParam(state: SimulationState, result: ActionResult, params: OnsenActionParam): SimulationState {
        if (!result.success) return state
        val onsenStatus = state.onsenStatus ?: return state
        val activeGensenType = onsenStatus.activeGensen ?: return state

        // Determine which strata to excavate based on active gensen
        val targetStrata = when (activeGensenType) {
            GensenType.SHIKKU -> StrataType.SAND
            GensenType.KENNIN -> StrataType.EARTH
            GensenType.MEISEKI -> StrataType.ROCK
        }

        val progress = params.excavationProgress[targetStrata] ?: 0
        val currentGensen = onsenStatus.gensen[activeGensenType]!!
        val newProgress = currentGensen.progress + progress

        val threshold = (100 * Math.pow(1.5, currentGensen.level.toDouble())).toInt()

        return if (newProgress >= threshold) {
            val newGensen = currentGensen.copy(level = currentGensen.level + 1, progress = newProgress - threshold)
            val newGensenMap = onsenStatus.gensen + (activeGensenType to newGensen)
            state.updateOnsenStatus {
                copy(gensen = newGensenMap).updateInnRank()
            }
        } else {
            val newGensen = currentGensen.copy(progress = newProgress)
            val newGensenMap = onsenStatus.gensen + (activeGensenType to newGensen)
            state.updateOnsenStatus { copy(gensen = newGensenMap) }
        }
    }
}
