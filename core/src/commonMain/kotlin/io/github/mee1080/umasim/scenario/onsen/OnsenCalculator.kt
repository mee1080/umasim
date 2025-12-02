package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.utility.mapIf
import io.github.mee1080.utility.replaced
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.random.nextInt

object OnsenCalculator : ScenarioCalculator {
    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val onsenStatus = info.onsenStatus ?: return Status()
        if (onsenStatus.onsenActiveTurn == 0) return Status()
        val gensenEffect = onsenStatus.totalGensenContinuousEffect

        val trainingEffect = gensenEffect.trainingEffect
        val friendBonus = if (friendTraining) gensenEffect.friendBonus[info.training.type] ?: 0 else 0
        if (Calculator.DEBUG) {
            println("onsen trainingEffect: $trainingEffect, friendBonus: $friendBonus")
        }
        return Status(
            speed = calcSingleStatus(base.speed, trainingEffect, friendBonus),
            stamina = calcSingleStatus(base.stamina, trainingEffect, friendBonus),
            power = calcSingleStatus(base.power, trainingEffect, friendBonus),
            guts = calcSingleStatus(base.guts, trainingEffect, friendBonus),
            wisdom = calcSingleStatus(base.wisdom, trainingEffect, friendBonus),
            skillPt = calcSingleStatus(base.skillPt, trainingEffect, friendBonus),
        )
    }

    private fun calcSingleStatus(base: Int, trainingEffect: Int, friendBonus: Int): Int {
        return base * (100 + trainingEffect) * (100 + friendBonus) / 10000 - base
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> {
        val onsenStatus = state.onsenStatus ?: return baseActions
        return baseActions.map { action ->
            when (action) {
                // トレーニング
                is Training -> {
                    val digResult = calcDigResult(state, 25 + action.member.size)
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            digResult.copy(digBonus = Status())
                        ).mapIf({ it.first.success }) {
                            (it.first as StatusActionResult).copy(
                                status = it.first.status + digResult.digBonus
                            ) to it.second
                        }
                    )
                }

                // PR活動
                is OnsenPR -> {
                    action.copy(
                        result = action.result.addScenarioActionParam(
                            calcDigResult(state, 10 + action.memberCount).copy(
                                onsenTicket = if (onsenStatus.onsenTicket >= 3) 0 else 1,
                            )
                        ),
                    )
                }

                // レース
                is Race -> {
                    action.copy(
                        result = action.result.addScenarioActionParam(
                            calcDigResult(state, if (action.goal) 25 else 15)
                        )
                    )
                }

                // お休み
                is Sleep -> {
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            calcDigResult(state, 15)
                        )
                    )
                }

                // お出かけ
                is Outing -> {
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            calcDigResult(state, if (action.support?.charaName == "保科健子") 25 else 15),
                        )
                    )
                }

                else -> action
            }
        }
    }

    private fun calcDigResult(state: SimulationState, basePoint: Int): OnsenActionParam {
        if (state.isLevelUpTurn || state.turn >= 73) return OnsenActionParam()
        val onsenStatus = state.onsenStatus ?: return OnsenActionParam()
        val status = state.status
        return calcDigResult(onsenStatus, status, basePoint)
    }

    fun calcDigResult(
        onsenStatus: OnsenStatus,
        status: Status,
        basePoint: Int,
        noLimit: Boolean = false,
    ): OnsenActionParam {
        val (stratumType, progress, rest) = onsenStatus.currentStratum ?: return OnsenActionParam()
        val power = calcDigPower(onsenStatus, status, stratumType)
        var digPoint = floor(basePoint * (100 + power) / 100.0).toInt()
        var digBonus = calcDigBonus(stratumType, progress, rest, digPoint)
        if (digPoint > rest) {
            val next = onsenStatus.nextStratumType
            if (next == null) {
                if (!noLimit) {
                    digPoint = rest
                }
            } else {
                val usedBasePoint = ceil(rest * 100 / (100.0 + power)).toInt()
                val nextBasePoint = basePoint - usedBasePoint
                val nextPower = calcDigPower(onsenStatus, status, next)
                val nextDigPoint = floor(nextBasePoint * (100 + nextPower) / 100.0).toInt()
                digPoint = rest + nextDigPoint
                digBonus += calcDigBonus(next, 0, Int.MAX_VALUE, nextDigPoint)
            }
        }

        return OnsenActionParam(
            digPoint = digPoint,
            digBonus = digBonus,
        )
    }

    fun calcDigPower(onsenStatus: OnsenStatus, status: Status, type: StratumType): Int {
        val stats = stratumToStatus[type] ?: return 0

        val statusPower = stats.mapIndexed { index, statusType ->
            val rank = getStatusRank(status.get(statusType))
            statusToDigPower[rank][index]
        }.sum()

        val equipmentPower = equipmentLevelBonus[onsenStatus.equipmentLevel[type] ?: 0]

        val factorPower = onsenStatus.factorDigPower[type] ?: 0

        return statusPower + equipmentPower + factorPower
    }

    fun calcDigBonus(type: StratumType, progress: Int, rest: Int, point: Int): Status {
        val count = (progress + min(rest, point)) / 30 - progress / 30
        return if (count == 0) Status() else digBonus[type]!! * count
    }

    private fun getStatusRank(value: Int): Int {
        return when {
            value >= 1200 -> 7 // UG-
            value >= 1000 -> 6 // S-SS
            value >= 600 -> 5 // B-A
            value >= 400 -> 4 // C
            value >= 300 -> 3 // D
            value >= 200 -> 2 // E
            value >= 100 -> 1 // F
            else -> 0 // G
        }
    }

    override fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> {
        val onsenStatus = state.onsenStatus ?: return emptyArray()
        return buildList {
            if (state.turn >= 3 && !state.isGoalRaceTurn) {
                add(OnsenPR(Random.nextInt(0..5), StatusActionResult(Status(6, 6, 6, 6, 6, 15, -20))))
            }
            if (onsenStatus.onsenTicket > 0 && onsenStatus.onsenActiveTurn == 0) {
                add(OnsenBathing)
            }
        }.toTypedArray()
    }

    fun applyScenarioAction(
        state: SimulationState,
        result: OnsenActionResult,
    ): SimulationState {
        return when (result) {
            is OnsenBathingResult -> {
                val onsenStatus = state.onsenStatus ?: return state
                var newState = state
                // HP+X、絆低い3人10、バステ解除、やる気1
                // バステ解除は発生イベントを実装していないため無視
                state.support.shuffled().sortedBy { it.relation }.take(3).forEach {
                    newState = newState.addRelation(10, it)
                }
                var status = Status(hp = onsenStatus.totalGensenImmediateEffectHp, motivation = 1)
                var maxHp: Int? = null
                if (onsenStatus.superRecoveryAvailable) {
                    // 超回復時：体力+20、スキルPt+100、ランダムヒント2個、体力最大値一時的に150
                    status += Status(hp = 20, skillPt = 100)
                    maxHp = 150
                    newState = newState
                        .updateOnsenStatus {
                            copy(superRecoveryAvailable = false, superRecoveryUsedHp = 0)
                        }
                        .addRandomSupportHint()
                        .addRandomSupportHint()
                    repeat(onsenStatus.ryokanBonus.superRecoveryHintBonus) {
                        newState = newState.addRandomSupportHint()
                    }
                }
                newState = newState.addStatus(status, overrideMaxHp = maxHp)
                // 温泉チケット消費、ターン数更新、継続効果追加
                newState = newState.updateOnsenStatus {
                    copy(
                        onsenTicket = onsenTicket - 1,
                        onsenActiveTurn = 2,
                        totalGensenContinuousEffect = excavatedGensenContinuousEffect,
                    )
                }

                // 参加人数追加
                if (newState.onsenStatus?.totalGensenContinuousEffect?.extraSupportInTraining == true) {
                    val targets = newState.support
                        .filter { it.positions.isNotEmpty() }
                        .map { it.index }
                        .shuffled()
                        .take(3)
                    val memberCounts = mutableMapOf<StatusType, Int>()
                    newState.support.forEach {
                        if (it.position != StatusType.NONE) {
                            memberCounts[it.position] = memberCounts.getOrElse(it.position) { 0 } + 1
                        }
                    }
                    newState = newState.copy(
                        member = newState.member.mapIf({ targets.contains(it.index) }) { member ->
                            val position = trainingType.filter {
                                !member.positions.contains(it) && memberCounts.getOrElse(it) { 0 } < 5
                            }.random()
                            memberCounts[position] = memberCounts.getOrElse(position) { 0 } + 1
                            member.copy(additionalPosition = member.additionalPosition + position)
                        }
                    )
                }

                newState
            }

            is OnsenSelectGensenResult -> {
                state.updateOnsenStatus {
                    selectGensen(this, result.gensen)
                }
            }

            is OnsenSelectEquipmentResult -> {
                state.updateOnsenStatus {
                    selectEquipment(this, result.equipment)
                }
            }
        }
    }

    fun selectGensen(onsenStatus: OnsenStatus, gensen: Gensen): OnsenStatus {
        val selectedGensen = onsenStatus.selectedGensen
        val digProgress = onsenStatus.digProgress
        val suspendedGensen = onsenStatus.suspendedGensen
        val newSuspendedGensen = if (selectedGensen != null && digProgress < selectedGensen.totalProgress) {
            suspendedGensen + (selectedGensen.name to digProgress)
        } else suspendedGensen
        val progress = newSuspendedGensen[gensen.name] ?: 0
        return onsenStatus.copy(
            selectedGensen = gensen,
            digProgress = progress,
            suspendedGensen = newSuspendedGensen,
        )
    }

    fun selectEquipment(onsenStatus: OnsenStatus, equipment: StratumType): OnsenStatus {
        return onsenStatus.copy(equipmentLevel = onsenStatus.equipmentLevel.replaced(equipment) {
            onsenStatus.equipmentLevel[equipment]!! + 1
        })
    }

    suspend fun applyScenarioActionParam(
        state: SimulationState,
        result: ActionResult,
        params: OnsenActionParam,
        selector: ActionSelector,
    ): SimulationState {
        if (!result.success) return state
        var newState = state
            .updateOnsenStatus {
                copy(
                    onsenTicket = min(3, onsenTicket + params.onsenTicket),
                    digProgress = digProgress + params.digPoint,
                )
            }
            .addStatus(params.digBonus)
        val newOnsenStatus = newState.onsenStatus ?: return newState
        if (newOnsenStatus.digFinished(state.turn)) {
            newState = newState.updateOnsenStatus {
                copy(
                    excavatedGensen = excavatedGensen + selectedGensen!!,
                    excavatedGensenContinuousEffect = excavatedGensenContinuousEffect + selectedGensen.continuousEffect,
                    onsenTicket = min(3, onsenTicket + onsenTicketOnDig[hoshinaRarity]),
                    fixedDigFinishTurns = fixedDigFinishTurns?.drop(1),
                )
            }
            newState = newState.selectGensen(selector)
        }
        return newState
    }

    override fun updateOnAddStatus(
        state: SimulationState,
        status: Status
    ): SimulationState {
        val onsenStatus = state.onsenStatus ?: return state
        if (status.hp >= 0 || onsenStatus.superRecoveryAvailable) return state
        val usedHp = min(-status.hp, state.status.hp)
        // 超回復判定条件は仮実装
        return if (calcSuperRecoveryAvailable(onsenStatus, usedHp)) {
            state.updateOnsenStatus {
                copy(superRecoveryAvailable = true)
            }
        } else {
            state.updateOnsenStatus {
                copy(superRecoveryUsedHp = superRecoveryUsedHp + usedHp)
            }
        }
    }

    private fun calcSuperRecoveryAvailable(onsenStatus: OnsenStatus, usedHp: Int): Boolean {
        val threshold = if (onsenStatus.hoshinaRarity > 0) 42.5 else 50.0
        val oldRank = (onsenStatus.superRecoveryUsedHp / threshold).toInt()
        val newRank = ((onsenStatus.superRecoveryUsedHp + usedHp) / threshold).toInt()
        val rate = superRecoveryRate.getOrElse(newRank) { superRecoveryRate.last() }
        if (newRank > oldRank && Random.nextInt(99) < rate) return true
        return Random.nextDouble() < rate / 400.0
    }

    override fun calcBaseRaceStatus(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean
    ): Status? {
        if (!goal) return null
        return when (race.grade) {
            RaceGrade.DEBUT -> state.onsenRaceStatus(3, 3, 30)
            RaceGrade.PRE_OPEN -> state.onsenRaceStatus(3, 3, 30)
            RaceGrade.OPEN -> state.onsenRaceStatus(3, 3, 30)
            RaceGrade.G3 -> state.onsenRaceStatus(4, 3, 35)
            RaceGrade.G2 -> state.onsenRaceStatus(4, 3, 35)
            RaceGrade.G1 -> state.onsenRaceStatus(5, 3, 45)
            else -> null
        }
    }

    private fun SimulationState.onsenRaceStatus(count: Int, value: Int, skillPt: Int): Status {
        val bonusValue = value * 150 * (100 + totalRaceBonus) / 10000
        val targets = randomTrainingType(count).map { it to bonusValue }.toTypedArray()
        return Status(skillPt = skillPt * 150 * (100 + totalRaceBonus) / 10000).add(*targets)
    }

    override fun applyScenarioRaceBonus(
        state: SimulationState,
        base: Status
    ): Status {
        val onsenStatus = state.onsenStatus ?: return base
        if (!state.isGoalRaceTurn || state.turn >= 73) return base
        if (onsenStatus.onsenActiveTurn == 0) return base
        val bonus = onsenStatus.totalGensenContinuousEffect.goalBonus
        if (bonus == 0) return base
        return base.copy(
            speed = base.speed * (100 + bonus) / 100,
            stamina = base.stamina * (100 + bonus) / 100,
            power = base.power * (100 + bonus) / 100,
            guts = base.guts * (100 + bonus) / 100,
            wisdom = base.wisdom * (100 + bonus) / 100,
            skillPt = base.skillPt * (100 + bonus) / 100,
        )
    }

    override fun getSpecialityRateUp(
        state: SimulationState,
        cardType: StatusType
    ): Int {
        return state.onsenStatus?.ryokanBonus?.specialityRate ?: 0
    }

    override fun updateScenarioTurn(state: SimulationState): SimulationState {
        return state.updateOnsenStatus {
            copy(
                onsenActiveTurn = max(0, onsenActiveTurn - 1),
                superRecoveryAvailable = superRecoveryAvailable || ryokanBonus.superRecoveryGuaranteed,
            )
        }
    }

    override fun getFailureRateDown(state: SimulationState): Int {
        val onsenStatus = state.onsenStatus ?: return 0
        if (onsenStatus.onsenActiveTurn == 0) return 0
        return onsenStatus.totalGensenContinuousEffect.failureRateDown
    }

    override fun getHintFrequencyUp(state: SimulationState, position: StatusType): Int {
        val onsenStatus = state.onsenStatus ?: return 0
        if (onsenStatus.onsenActiveTurn == 0) return 0
        return onsenStatus.totalGensenContinuousEffect.hintRateUp
    }

    override fun getHpCostDown(scenarioStatus: ScenarioStatus): Int {
        val onsenStatus = scenarioStatus as? OnsenStatus ?: return 0
        if (onsenStatus.onsenActiveTurn == 0) return 0
        return onsenStatus.totalGensenContinuousEffect.hpCost
    }

    override fun getAdditionalMemberCount(state: SimulationState): Int {
        val onsenStatus = state.onsenStatus ?: return 0
        if (onsenStatus.onsenActiveTurn == 0) return 0
        return if (onsenStatus.totalGensenContinuousEffect.extraSupportInTraining) 3 else 0
    }
}
