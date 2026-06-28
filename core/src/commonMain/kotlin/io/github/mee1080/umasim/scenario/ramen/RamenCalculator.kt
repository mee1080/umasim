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

        var trainingBonus = 0
        var friendFactor = 1.0

        // 試食会の基礎効果 (トレ効果15)
        if (region != null) {
            trainingBonus += 15
        }

        // 盛り上がりPtによるボーナス (暫定: 1000ptごとにトレ効果1%?)
        val excitementBonus = (ramenStatus.excitementPt / 1000).coerceAtMost(10)
        trainingBonus += excitementBonus

        // 地域ごとの固有効果
        var skillPtBonus = 0
        if (region != null) {
            if (region.targetAll || region.targetTypes.contains(info.training.type)) {
                trainingBonus += region.trainingEffect
                if (friendTraining) {
                    friendFactor *= (100 + region.friendBonus) / 100.0
                }
                skillPtBonus = region.skillPtTrainingEffect
            }
        }

        val commonBonus = Calculator.ScenarioCalcBonus(
            trainingBonus = trainingBonus,
            friendFactor = friendFactor
        )

        val speed = (Calculator.calcTrainingStatus(info, StatusType.SPEED, friendTraining, bonus = commonBonus) - base.speed).toInt()
        val stamina = (Calculator.calcTrainingStatus(info, StatusType.STAMINA, friendTraining, bonus = commonBonus) - base.stamina).toInt()
        val power = (Calculator.calcTrainingStatus(info, StatusType.POWER, friendTraining, bonus = commonBonus) - base.power).toInt()
        val guts = (Calculator.calcTrainingStatus(info, StatusType.GUTS, friendTraining, bonus = commonBonus) - base.guts).toInt()
        val wisdom = (Calculator.calcTrainingStatus(info, StatusType.WISDOM, friendTraining, bonus = commonBonus) - base.wisdom).toInt()

        val skillBonus = commonBonus.copy(trainingBonus = trainingBonus + skillPtBonus)
        val skillPt = (Calculator.calcTrainingStatus(info, StatusType.SKILL, friendTraining, bonus = skillBonus) - base.skillPt).toInt()

        return Status(
            speed = speed,
            stamina = stamina,
            power = power,
            guts = guts,
            wisdom = wisdom,
            skillPt = skillPt,
        )
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

    override fun getAdditionalMemberCount(state: SimulationState): Int {
        val ramenStatus = state.ramenStatus ?: return 0
        val region = ramenStatus.activeTastingRegion ?: return 0
        return if (region.targetAll) region.addMember else 0
    }

    override fun getForceHintCount(state: SimulationState): Int {
        val ramenStatus = state.ramenStatus ?: return 0
        val region = ramenStatus.activeTastingRegion ?: return 0
        return if (region.targetAll) region.hintCount else 0
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
}
