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
        val ramenStatus = state.ramenStatus ?: return member
        val region = ramenStatus.activeTastingRegion?.first ?: return member
        if (region.targetAll) return member

        var currentMember = member
        val supportPosition = trainingType.associateWith { type ->
            currentMember.filter { it.positions.contains(type) }.toMutableList()
        }

        // hintCount logic
        if (region.hintCount > 0) {
            val targets = currentMember.filter {
                !it.guest && !it.outingType && region.targetTypes.contains(it.card.type) && it.positions.isNotEmpty()
            }
            val notHintTargets = targets.filter { !it.hint }
            val alreadyHintCount = targets.size - notHintTargets.size
            val needMore = region.hintCount - alreadyHintCount
            if (needMore > 0) {
                val toAddIndices = notHintTargets.shuffled().take(needMore).map { it.index }.toSet()
                currentMember = currentMember.map {
                    if (toAddIndices.contains(it.index)) {
                        it.copy(supportState = it.supportState?.copy(hintIcon = true))
                    } else it
                }
            }
        }

        // addMember logic
        if (region.addMember > 0) {
            val candidates = currentMember.filter { !it.guest && it.position != StatusType.NONE }.shuffled()
            if (candidates.isNotEmpty()) {
                for (i in 0 until region.addMember) {
                    val targetCandidate = candidates[i % candidates.size]
                    val target = currentMember.first { it.index == targetCandidate.index }
                    val possibleTrainings = region.targetTypes.filter { type ->
                        supportPosition[type]!!.size < 5 && !target.positions.contains(type)
                    }
                    if (possibleTrainings.isNotEmpty()) {
                        val selectedTraining = possibleTrainings.random()
                        supportPosition[selectedTraining]!!.add(target)
                        currentMember = currentMember.map {
                            if (it.index == target.index) {
                                it.copy(additionalPosition = it.additionalPosition + selectedTraining)
                            } else it
                        }
                    }
                }
            }
        }

        return currentMember
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
            is RamenSelectRegionResult -> {
                state.updateRamenStatus {
                    addRegion(result.region)
                }
            }

            is RamenTastingResult -> {
                state.updateRamenStatus {
                    activateTasting(result.region)
                }
            }

            else -> state
        }
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
}
