package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import kotlin.math.max

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

        val period = (ramenStatus.turn - 1) / 24
        val baseEffect = if (region != null) ramenBaseEffect.getOrNull(period) else null

        var trainingEffect = 0
        var friendBonus = 0
        var skillPtTrainingEffect = 0

        trainingEffect += excitePtBonus.trainingEffect
        trainingEffect += rmjBonus.trainingEffect

        if (friendTraining) {
            friendBonus += excitePtBonus.friendBonus
            friendBonus += rmjBonus.friendBonus
        }

        if (baseEffect != null) {
            trainingEffect += baseEffect.trainingEffect
            if (friendTraining) {
                friendBonus += baseEffect.friendBonus
            }
        }

        if (region != null && (region.targetAll || region.targetTypes.contains(info.training.type))) {
            trainingEffect += region.trainingEffect
            if (friendTraining) {
                friendBonus += region.friendBonus
            }
            skillPtTrainingEffect += region.skillPtTrainingEffect
        }

        val factor = (100 + trainingEffect) / 100.0 * (100 + friendBonus) / 100.0
        val skillPtFactor = (100 + skillPtTrainingEffect) / 100.0

        val speed = base.speed * factor
        val stamina = base.stamina * factor
        val power = base.power * factor
        val guts = base.guts * factor
        val wisdom = base.wisdom * factor
        val skillPt = base.skillPt * factor * skillPtFactor

        return Status(
            speed = max(0, speed.toInt() - base.speed),
            stamina = max(0, stamina.toInt() - base.stamina),
            power = max(0, power.toInt() - base.power),
            guts = max(0, guts.toInt() - base.guts),
            wisdom = max(0, wisdom.toInt() - base.wisdom),
            skillPt = max(0, skillPt.toInt() - base.skillPt),
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
        if (ramenStatus.activeTastingRegion == null) return 0
        val period = (ramenStatus.turn - 1) / 24
        return ramenBaseEffect.getOrNull(period)?.failureRateDown ?: 0
    }

    override fun getTrainingRelationBonus(state: SimulationState): Int {
        val ramenStatus = state.ramenStatus ?: return 0
        if (ramenStatus.activeTastingRegion == null) return 0
        val period = (ramenStatus.turn - 1) / 24
        return ramenBaseEffect.getOrNull(period)?.relationGauge ?: 0
    }

    override fun isAllSupportHint(state: SimulationState, position: StatusType): Boolean {
        val ramenStatus = state.ramenStatus ?: return false
        if (ramenStatus.activeTastingRegion == null) return false
        val period = (ramenStatus.turn - 1) / 24
        return ramenBaseEffect.getOrNull(period)?.allHintEvent ?: false
    }

    override fun getScenarioCalcBonus(baseInfo: Calculator.CalcInfo): Calculator.ScenarioCalcBonus? {
        val ramenStatus = baseInfo.ramenStatus ?: return null
        if (ramenStatus.activeTastingRegion == null) return null
        val period = (ramenStatus.turn - 1) / 24
        val limitOver = ramenBaseEffect.getOrNull(period)?.statusLimitOver ?: return null
        return Calculator.ScenarioCalcBonus(maxValue = 100.0 + limitOver)
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
