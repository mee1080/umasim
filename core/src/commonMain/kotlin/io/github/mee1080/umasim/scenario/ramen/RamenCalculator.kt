package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.min

object RamenCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean
    ): Status {
        val ramenStatus = info.ramenStatus ?: return Status()
        val region = ramenStatus.activeTastingRegion?.first
        val excitePtBonus = ramenStatus.excitePtBonus
        val rmjBonus = ramenStatus.rmjBonus
        val baseEffect = ramenStatus.baseEffect
        val regionRankBonus = ramenStatus.activeTastingRegion?.second ?: 0

        var totallTrainingEffect = excitePtBonus.trainingEffect
        var totalFriendBonus = 0
        var totalSkillPtEffect = 0
        var statusLimitOver = 0
        var targetStatusLimitOver = 0
        var targetTypes = emptySet<StatusType>()
        if (friendTraining) {
            totalFriendBonus += rmjBonus.friendBonus
        }
        if (region != null) {
            targetTypes = region.targetTypes.toSet()
            val targetType = region.targetAll || targetTypes.contains(info.training.type)
            totallTrainingEffect += baseEffect.trainingEffect
            totalSkillPtEffect += baseEffect.skillPtTrainingEffect
            if (targetType) {
                if (region.trainingEffect > 0) {
                    totallTrainingEffect += region.trainingEffect + regionRankBonus
                }
                if (region.skillPtTrainingEffect > 0) {
                    totalSkillPtEffect += region.skillPtTrainingEffect + regionRankBonus
                }
            }
            if (friendTraining) {
                totalFriendBonus += baseEffect.friendBonus
                if (targetType && region.friendBonus > 0) {
                    totalFriendBonus += region.friendBonus + regionRankBonus
                }
            }
            statusLimitOver = baseEffect.statusLimitOver
            targetStatusLimitOver = region.targetStatusLimitOver
        }
        if (Calculator.DEBUG) {
            println("exPtTraining: ${excitePtBonus.trainingEffect}")
            if (friendTraining) {
                println("rmjFriend: ${rmjBonus.friendBonus}")
            }
            if (region != null) {
                val targetType = region.targetAll || region.targetTypes.contains(info.training.type)
                println("baseTraining: ${baseEffect.trainingEffect}")
                if (targetType) {
                    println("regionTraining: ${region.trainingEffect + regionRankBonus}")
                    println("regionSpTraining: ${region.skillPtTrainingEffect + regionRankBonus}")
                }
                if (friendTraining) {
                    println("baseFriend: ${baseEffect.friendBonus}")
                    if (targetType) {
                        println("regionFriend: ${region.friendBonus + regionRankBonus}")
                    }
                }
            }
            println("totalTrainingEffect: $totallTrainingEffect, totalFriendBonus: $totalFriendBonus, totalSkillPtEffect: $totalSkillPtEffect")
        }
        val factor = (100 + totallTrainingEffect) * (100 + totalFriendBonus) / 10000.0
        val skillPtFactor = (100 + totalSkillPtEffect) / 100.0

        fun calcSingleStatus(type: StatusType): Int {
            val baseValue = base.get(type)
            val total = ((baseValue * factor) - baseValue).toInt()
            var limit = 100 + statusLimitOver
            if (targetStatusLimitOver > 0 && targetTypes.contains(type)) {
                limit += targetStatusLimitOver
            }
            return min(total, limit)
        }

        val skillPt = (base.skillPt * factor * skillPtFactor) - base.skillPt

        return Status(
            speed = calcSingleStatus(StatusType.SPEED),
            stamina = calcSingleStatus(StatusType.STAMINA),
            power = calcSingleStatus(StatusType.POWER),
            guts = calcSingleStatus(StatusType.GUTS),
            wisdom = calcSingleStatus(StatusType.WISDOM),
            skillPt = skillPt.toInt().coerceAtMost(100 + statusLimitOver + targetStatusLimitOver),
        )
    }

    override fun updateScenarioTurn(state: SimulationState): SimulationState {
        return state.updateRamenStatus {
            copy(turn = state.turn)
        }
    }

    override fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean
    ): Array<Action> {
        val status = state.ramenStatus ?: return emptyArray()
        val availableTasting = status.selectedRegions.filter { region ->
            val tips = status.tips
            val hidden = tips[RamenTipType.HIDDEN] ?: 0

            fun canAfford(type: RamenTipType, amount: Int): Int {
                return (amount - (tips[type] ?: 0)).coerceAtLeast(0)
            }

            val neededHidden = canAfford(RamenTipType.NOODLE, region.noodle) +
                    canAfford(RamenTipType.SOUP, region.soup) +
                    canAfford(RamenTipType.TOPPING, region.topping)

            hidden >= neededHidden
        }
        return availableTasting.map { RamenTasting(it) }.toTypedArray()
    }

    override fun modifyShuffledMember(
        state: SimulationState,
        member: List<MemberState>
    ): List<MemberState> {
        return member
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>
    ): List<Action> {
        val ramenStatus = state.ramenStatus ?: return baseActions
        val baseParam = ramenStatus.baseGauge
        return baseActions.map { action ->
            when (action) {
                is Training -> {
                    // TODO トレーニング配置コツ+2 他
                    val param = baseParam.add(ramenStatus.trainingTip[action.type]!!)
                    action.copy(
                        candidates = action.addScenarioActionParam(param)
                    )
                }

                is Race -> action.copy(result = action.result.addScenarioActionParam(baseParam))
                is Sleep -> action.copy(candidates = action.addScenarioActionParam(baseParam))
                is Outing -> action.copy(candidates = action.addScenarioActionParam(baseParam))

                else -> action
            }
        }
    }

    fun applyScenarioAction(state: SimulationState, result: ActionResult): SimulationState {
        return when (result) {
            is RamenSelectRegionResult -> applyRamenSelectRegionResult(state, result)
            is RamenTastingResult -> applyRamenTastingResult(state, result)
            else -> state
        }
    }

    private fun applyRamenSelectRegionResult(state: SimulationState, result: RamenSelectRegionResult): SimulationState {
        return state.updateRamenStatus {
            addRegion(result.region)
        }
    }

    private fun applyRamenTastingResult(state: SimulationState, result: RamenTastingResult): SimulationState {
        val region = result.region
        var newState = state.updateRamenStatus {
            activateTasting(region)
        }

        // hintCount: targetTypesのサポカのヒント獲得
        repeat(region.hintCount) {
            newState = newState.addRandomSupportHint(region.targetTypes, hintLevel = 2)
        }

        // addMember: targetTypesのトレーニングに、未参加のサポートカードを1種追加で参加させる
        repeat(region.addMember) {
            val candidates = newState.member.filter { !it.guest && it.positions.isEmpty() }
            val targetCard = candidates.randomOrNull() ?: return@repeat
            val targetTypes = if (region.targetAll) trainingType.toList() else region.targetTypes
            val supportCount = trainingType.associateWith { type ->
                newState.member.count { it.positions.contains(type) }
            }
            val availableTypes = targetTypes.filter { (supportCount[it] ?: 0) < 5 }
            val targetType = availableTypes.randomOrNull() ?: return@repeat

            newState = newState.copy(
                member = newState.member.map {
                    if (it.index == targetCard.index) {
                        it.copy(additionalPosition = it.additionalPosition + targetType)
                    } else it
                }
            )
        }

        // hintSkill: 極のヒント獲得
        if (region.hintSkill.isNotEmpty()) {
            newState = newState.addStatus(Status(skillHint = mapOf(region.hintSkill to 2)))
        }

        return newState
    }

    fun applyScenarioActionParam(state: SimulationState, param: RamenActionParam): SimulationState {
        return state.updateRamenStatus {
            addGauges(
                param.noodleGauge,
                param.soupGauge,
                param.toppingGauge
            ).addHiddenTaste(param.hiddenTaste)
        }
    }

    override fun getSpecialityRateUp(state: SimulationState, cardType: StatusType): Int {
        val ramenStatus = state.ramenStatus ?: return 0
        return ramenStatus.excitePtBonus.specialityRateUp + ramenStatus.rmjBonus.specialityRateUp
    }

    override fun getHintFrequencyUp(state: SimulationState, position: StatusType): Int {
        val ramenStatus = state.ramenStatus ?: return 0
        return ramenStatus.excitePtBonus.hintRateUp + ramenStatus.rmjBonus.hintRateUp
    }

    override fun getFailureRateDown(state: SimulationState): Int {
        val ramenStatus = state.ramenStatus ?: return 0
        return ramenStatus.baseEffect.failureRateDown
    }

    override fun getTrainingRelationBonus(state: SimulationState): Int {
        val ramenStatus = state.ramenStatus ?: return 0
        return ramenStatus.baseEffect.relationGauge
    }

    override fun isAllSupportHint(state: SimulationState, position: StatusType): Boolean {
        val ramenStatus = state.ramenStatus ?: return false
        return ramenStatus.baseEffect.allHintEvent
    }

    override fun getScenarioCalcBonus(baseInfo: Calculator.CalcInfo): Calculator.ScenarioCalcBonus? {
        val ramenStatus = baseInfo.ramenStatus ?: return null
        val limitOver = ramenStatus.baseEffect.statusLimitOver
        return Calculator.ScenarioCalcBonus(maxValue = 100.0 + limitOver)
    }
}
