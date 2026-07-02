package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*

object RamenCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean
    ): Status {
        val ramenStatus = info.ramenStatus ?: return Status()
        val region = ramenStatus.activeTastingRegion
        val excitePtBonus = ramenStatus.excitePtBonus
        val rmjBonus = ramenStatus.rmjBonus
        val baseEffect = ramenStatus.baseEffect
        val regionRankBonus = ramenStatus.regionRankBonus

        var factor = 1.0
        var skillPtFactor = 1.0
        factor *= (100 + excitePtBonus.trainingEffect) / 100.0
        if (friendTraining) {
            factor *= (100 + rmjBonus.friendBonus) / 100.0
        }
        if (region != null && (region.targetAll || region.targetTypes.contains(info.training.type))) {
            factor *= (100 + baseEffect.trainingEffect) / 100.0
            factor *= (100 + region.trainingEffect + regionRankBonus) / 100.0
            if (friendTraining) {
                factor *= (100 + baseEffect.friendBonus) / 100.0
                factor *= (100 + region.friendBonus + regionRankBonus) / 100.0
            }
            skillPtFactor = (100 + region.skillPtTrainingEffect + regionRankBonus) / 100.0
        }
        if (Calculator.DEBUG) {
            println("exPtTraining: ${excitePtBonus.trainingEffect}")
            if (friendTraining) {
                println("rmjFriend: ${rmjBonus.friendBonus}")
            }
            if (region != null && (region.targetAll || region.targetTypes.contains(info.training.type))) {
                println("baseTraining: ${baseEffect.trainingEffect}")
                println("regionTraining: ${region.trainingEffect + regionRankBonus}")
                if (friendTraining) {
                    println("baseFriend: ${baseEffect.friendBonus}")
                    println("regionFriend: ${region.friendBonus + regionRankBonus}")
                }
                println("regionSpTraining: ${region.skillPtTrainingEffect + regionRankBonus}")
            }
            println("factor: $factor, spFactor: $skillPtFactor")
        }

        val speed = (base.speed * factor) - base.speed
        val stamina = (base.stamina * factor) - base.stamina
        val power = (base.power * factor) - base.power
        val guts = (base.guts * factor) - base.guts
        val wisdom = (base.wisdom * factor) - base.wisdom
        val skillPt = (base.skillPt * factor * skillPtFactor) - base.skillPt

        return Status(
            speed = speed.toInt(),
            stamina = stamina.toInt(),
            power = power.toInt(),
            guts = guts.toInt(),
            wisdom = wisdom.toInt(),
            skillPt = skillPt.toInt(),
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
        val region = ramenStatus.activeTastingRegion ?: return member
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

    override fun getScenarioCalcBonus(baseInfo: Calculator.CalcInfo): Calculator.ScenarioCalcBonus? {
        val ramenStatus = baseInfo.ramenStatus ?: return null
        val limitOver = ramenStatus.baseEffect.statusLimitOver
        return Calculator.ScenarioCalcBonus(maxValue = 100.0 + limitOver)
    }
}
