package io.github.mee1080.umasim.scenario.ramen

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.applyIf
import io.github.mee1080.utility.mapIf
import kotlin.math.max
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
        }.applyIf({ state.turn >= 73 }) {
            addStatus(Status(hp = 20, motivation = 1))
        }
    }

    override fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean
    ): Array<Action> {
        val status = state.ramenStatus ?: return emptyArray()
        if (state.turn >= 73 || status.activeTastingRegion != null) return emptyArray()
        val tips = status.tips
        val noodle = tips[RamenTipType.NOODLE] ?: 0
        val soup = tips[RamenTipType.SOUP] ?: 0
        val topping = tips[RamenTipType.TOPPING] ?: 0
        val hiddenTips = min(2, status.hiddenTips)

        val actions = mutableListOf<Action>()
        for (region in status.selectedRegions) {
            val needNoodle = max(0, region.noodle - noodle)
            if (needNoodle > hiddenTips) continue
            for (cn in needNoodle..min(hiddenTips, region.noodle)) {
                val hiddenTips2 = hiddenTips - cn
                val needSoup = max(0, region.soup - soup)
                if (needSoup > hiddenTips2) continue
                for (cs in needSoup..min(hiddenTips2, region.soup)) {
                    val hiddenTips3 = hiddenTips2 - cs
                    val needTopping = max(0, region.topping - topping)
                    if (needTopping > hiddenTips3) continue
                    for (ct in needTopping..min(hiddenTips3, region.topping)) {
                        val changeHiddenList = buildList {
                            repeat(cn) { add(RamenTipType.NOODLE) }
                            repeat(cs) { add(RamenTipType.SOUP) }
                            repeat(ct) { add(RamenTipType.TOPPING) }
                        }
                        actions.add(RamenTasting(region, changeHiddenList))
                    }
                }
            }
        }
        return actions.toTypedArray()
    }

    override fun modifyShuffledMember(
        state: SimulationState,
        member: List<MemberState>
    ): List<MemberState> {
        if (state.turn <= 72) return member
        return addmember(member, trainingType.toList())
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>
    ): List<Action> {
        val ramenStatus = state.ramenStatus ?: return baseActions
        if (state.turn >= 73) return baseActions
        val baseParam = (if (Scenario.RAMEN.levelUpTurns.contains(state.turn)) {
            RamenActionParam(7, 7, 7)
        } else ramenStatus.baseGauge).adjustMax(ramenStatus)
        return baseActions.map { action ->
            when (action) {
                is Training -> {
                    val tipType = ramenStatus.trainingTip[action.type]!!
                    val tipCount = 1 + action.member.sumOf { if (it.guest) 1 else 2 } / 2
                    val param = baseParam.add(tipType, tipCount, action.friendTraining)
                        .adjustMax(ramenStatus)
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

    override fun calcBaseRaceStatus(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean
    ): Status? {
        if (!goal || race.grade == RaceGrade.FINALS) return null
        val bonusValue = 5 * 150 * (100 + state.totalRaceBonus) / 10000
        val targets = trainingType.map { it to bonusValue }.toTypedArray()
        return Status(skillPt = 45 * 150 * (100 + state.totalRaceBonus) / 10000).add(*targets)
    }

    override fun applyScenarioRaceBonus(
        state: SimulationState,
        base: Status
    ): Status {
        return if (state.turn >= 73) base * 2 else base
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
        }.applyIf({ result.region.hintSkill.isNotEmpty() }) {
            addStatus(Status(skillHint = mapOf(result.region.hintSkill to 2))).updateRamenStatus {
                copy(
                    activeTastingRegion = result.region to 0,
                )
            }
        }
    }

    private fun applyRamenTastingResult(state: SimulationState, result: RamenTastingResult): SimulationState {
        val ramenStatus = state.ramenStatus ?: return state
        val region = result.region
        var newState = state.updateRamenStatus {
            activateTasting(region, result.changeHiddenTips)
        }

        // relationGauge: 絆上昇
        val baseEffect = ramenBaseEffect.getOrElse(ramenStatus.period) { RamenBaseEffect.Empty }
        if (baseEffect.relationGauge > 0) {
            newState = newState.addRelationAll(baseEffect.relationGauge)
        }

        // hintCount: targetTypesのサポカのヒント獲得
        repeat(region.hintCount) {
            newState = newState.addRandomSupportHint(region.targetTypes)
        }

        // addMember: targetTypesのトレーニングに、未参加のサポートカードを1種追加で参加させる
        repeat(region.addMember) {
            newState = newState.copy(
                member = addmember(
                    newState.member,
                    if (region.targetAll) trainingType.toList() else region.targetTypes,
                )
            )
        }

        return newState
    }

    private fun addmember(
        memberList: List<MemberState>,
        targetTypes: List<StatusType>,
    ): List<MemberState> {
        val supportMember = memberList.filter { !it.guest && !it.outingType }
        val candidates = supportMember.filter { it.positions.isEmpty() }.shuffled().toMutableList()
        val positionMap = buildMap {
            memberList.forEach { member ->
                member.positions.forEach { type ->
                    getOrPut(type) { mutableListOf() }.add(member)
                }
            }
        }
        var newMemberList = memberList
        targetTypes.forEach { targetType ->
            val positioned = positionMap[targetType] ?: emptyList()
            val positionedSupport = positioned.filter { !it.guest }.map { it.index }.toSet()
            if (positionedSupport.size >= 5) return@forEach
            var targetIndex = candidates.indexOfFirst { !positionedSupport.contains(it.index) }
            if (targetIndex == -1 && candidates.size < 6) {
                candidates += supportMember.shuffled()
                targetIndex = candidates.indexOfFirst { !positionedSupport.contains(it.index) }
                if (targetIndex == -1) return@forEach
            }
            val target = candidates.removeAt(targetIndex)
            newMemberList = newMemberList.mapIf({ it.index == target.index }) {
                it.copy(additionalPosition = it.additionalPosition + targetType)
            }
            if (positioned.size >= 5) {
                val removeIndex = positioned.last { it.guest }.index
                newMemberList = newMemberList.mapIf({ it.index == removeIndex }) {
                    it.copy(position = StatusType.NONE)
                }
            }
        }

        return newMemberList
    }

    fun applyScenarioActionParam(state: SimulationState, param: RamenActionParam): SimulationState {
        return state.updateRamenStatus {
            addGauges(
                param.noodleGauge,
                param.soupGauge,
                param.toppingGauge
            )
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

    override fun isAllSupportHint(state: SimulationState, position: StatusType): Boolean {
        val ramenStatus = state.ramenStatus ?: return false
        return ramenStatus.baseEffect.allHintEvent
    }
}
