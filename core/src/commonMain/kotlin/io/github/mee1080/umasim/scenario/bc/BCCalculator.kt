package io.github.mee1080.umasim.scenario.bc

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.simulation2.Calculator.CalcInfo
import io.github.mee1080.utility.mapIf
import io.github.mee1080.utility.replaced
import kotlin.math.max
import kotlin.math.min

object BCCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean
    ): Status {
        val bcStatus = info.bcStatus ?: return Status()
        val position = info.training.type
        val trainingEffect = bcStatus.trainingEffect(position)
        val friendBonus = if (friendTraining) bcStatus.friendBonus else 1.0
        if (Calculator.DEBUG) {
            val log = buildString {
                append("BC TrainingEffect: ")
                append(bcStatus.teamMemberIn(position).joinToString(",") {
                    "${it.trainingEffect}(${it.memberRankString}/${it.dreamGauge})"
                })
                append(" FriendBonus: $friendBonus")
            }
            println(log)
        }
        val base = Status(
            speed = calcSingleStatus(base.speed, trainingEffect, friendBonus),
            stamina = calcSingleStatus(base.stamina, trainingEffect, friendBonus),
            power = calcSingleStatus(base.power, trainingEffect, friendBonus),
            guts = calcSingleStatus(base.guts, trainingEffect, friendBonus),
            wisdom = calcSingleStatus(base.wisdom, trainingEffect, friendBonus),
            skillPt = calcSingleStatus(base.skillPt, trainingEffect + bcStatus.skillPtEffect, friendBonus),
        )
        return applyLimitOrSubParameterRate(info, base)
    }

    private fun calcSingleStatus(base: Int, trainingEffect: Int, friendBonus: Double): Int {
        return (base * (100 + trainingEffect) * friendBonus / 100).toInt() - base
    }

    private val scenarioCalcBonus = Calculator.ScenarioCalcBonus(maxValue = 300.0)

    override fun getScenarioCalcBonus(baseInfo: CalcInfo) = scenarioCalcBonus

    override fun modifyBaseStatus(
        info: CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        // サブ基礎能力倍率は最終結果に対して反映
        return applyLimitOrSubParameterRate(info, base)
    }

    private fun applyLimitOrSubParameterRate(info: CalcInfo, base: Status): Status {
        val bcStatus = info.bcStatus ?: return base
        if (Calculator.DEBUG) println("applySubParameterRate active: ${bcStatus.dreamsTrainingActive}, subParameterRate: ${bcStatus.subParameterRate}")
        if (!bcStatus.dreamsTrainingActive) return base
        return base.copy(
            speed = applyLimitOrSubParameterRateSingle(info, StatusType.SPEED, base.speed),
            stamina = applyLimitOrSubParameterRateSingle(info, StatusType.STAMINA, base.stamina),
            power = applyLimitOrSubParameterRateSingle(info, StatusType.POWER, base.power),
            guts = applyLimitOrSubParameterRateSingle(info, StatusType.GUTS, base.guts),
            wisdom = applyLimitOrSubParameterRateSingle(info, StatusType.WISDOM, base.wisdom),
            skillPt = applyLimitOrSubParameterRateSingle(info, StatusType.SKILL, base.skillPt),
        )
    }

    private fun applyLimitOrSubParameterRateSingle(info: CalcInfo, type: StatusType, base: Int): Int {
        if (base == 0) return 0
        val bcStatus = info.bcStatus ?: return base
        return when (type) {
            StatusType.SKILL -> min(100 + bcStatus.skillPtLimitUp, base)
            info.training.type -> min(100 + bcStatus.mainLimitUp, base)
            else -> max(1, min(100, base) * bcStatus.subParameterRate / 100)
        }
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> {
        val bcStatus = state.bcStatus ?: return baseActions
        return baseActions.map { action ->
            when (action) {
                is Training -> {
                    val param = BCActionParam(action.type, bcStatus.teamMemberIn(action.type))
                    action.copy(
                        candidates = action.addScenarioActionParam(param)
                    )
                }

                is Sleep -> action.copy(candidates = action.addScenarioActionParam(BCActionParam()))

                is Outing if (action.support == null) -> action.copy(candidates = action.addScenarioActionParam(BCActionParam()))

                is Race if (action.goal) -> action.copy(result = action.result.addScenarioActionParam(BCActionParam()))

                else -> action
            }
        }
    }

    fun applyScenarioActionParam(
        state: SimulationState,
        result: ActionResult,
        params: BCActionParam,
    ): SimulationState {
        if (!result.success) return state
        return state.updateBCStatus {
            if (params.position == StatusType.NONE) {
                addRandomDreamGauge()
            } else {
                var newDreamsPoint = dreamsPoint
                val newTeamMember = teamMember.mapIf({ params.member.contains(it) }) {
                    if (it.dreamGauge == 3) {
                        newDreamsPoint += dpMemberRankUp[casinoRarity]
                        it.copy(dreamGauge = 0, memberRank = it.memberRank + 1)
                    } else {
                        it.copy(dreamGauge = it.dreamGauge + 1)
                    }
                }
                copy(teamMember = newTeamMember, dreamsPoint = newDreamsPoint)
            }
        }
    }

    internal fun BCStatus.addRandomDreamGauge(): BCStatus {
        return addDreamGauge(teamMember.filter { it.dreamGauge < 3 }.randomOrNull())
    }

    internal fun addLowestDreamGauge(bcStatus: BCStatus): BCStatus {
        var result = bcStatus
        repeat(3) {
            val target = result.teamMember.mapIndexed { index, member -> index to member }
                .minBy { it.second.dreamGauge * 10 + it.first }.second
            result = result.addDreamGauge(target)
        }
        return result
    }

    private fun BCStatus.addDreamGauge(target: BCTeamMember?): BCStatus {
        return if (target == null) this else {
            copy(
                teamMember = teamMember.mapIf({ it == target }) {
                    it.copy(dreamGauge = it.dreamGauge + 1)
                }
            )
        }
    }

    override fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> {
        val bcStatus = state.bcStatus ?: return emptyArray()
        if (goal || bcStatus.dreamsTrainingActive || bcStatus.dreamsTrainingCount == 0) return emptyArray()
        return arrayOf(BCDreamsTraining)
    }

    fun applyScenarioAction(
        state: SimulationState,
        result: BCActionResult,
    ): SimulationState {
        return when (result) {
            BCDreamsTrainingResult -> {
                val positioned = state.member.filter { !it.outingType }.shuffled().take(5).map { it.index }
                val allPosition = setOf(StatusType.STAMINA, StatusType.POWER, StatusType.GUTS, StatusType.WISDOM)
                val newMember = state.member.map { member ->
                    if (positioned.contains(member.index)) {
                        member.copy(position = StatusType.SPEED, additionalPosition = allPosition)
                    } else {
                        member.copy(position = StatusType.NONE, additionalPosition = emptySet())
                    }
                }
                state.copy(member = newMember).updateBCStatus {
                    copy(
                        dreamsTrainingCount = dreamsTrainingCount - 1,
                        dreamsTrainingActive = true,
                    )
                }
            }

            is BCTeamParameterUpResult -> state.updateBCStatus {
                copy(
                    teamParameter = teamParameter.replaced(result.parameter) { it + 1 },
                    dreamsPoint = dreamsPoint - 5,
                )
            }
        }
    }

    override fun updateScenarioTurn(state: SimulationState): SimulationState {
        // チームメンバーをランダムなトレーニングに配置
        return state.updateBCStatus {
            copy(
                teamMember = teamMember.map { it.copy(position = trainingType.random()) },
                dreamsTrainingActive = false,
            )
        }
    }

    override fun modifyShuffledMember(
        state: SimulationState,
        member: List<MemberState>
    ): List<MemberState> {
        // シナリオリンクサポカ不在時はチームメンバーの位置に配置
        // チームメンバーをサポカと同じ位置に配置する処理はBCScenarioEvents
        val bcStatus = state.bcStatus ?: return member
        val teamMemberPosition = bcStatus.teamMember.associate { it.charaName to it.position }
        return member.map {
            if (it.position != StatusType.NONE) it else {
                it.copy(position = teamMemberPosition[it.charaName] ?: StatusType.NONE)
            }
        }
    }

    override fun calcBaseRaceStatus(
        state: SimulationState,
        race: RaceEntry,
        goal: Boolean
    ): Status? {
        if (!goal) return null
        if (race.grade == RaceGrade.DEBUT) {
            return state.raceStatus(4, 9, 105).copy(fanCount = 2000)
        }
        return when (state.goalRace.indexOfFirst { it.turn == race.turn }) {
            1 -> state.raceStatus(4, 9, 105).copy(fanCount = 14000)
            2 -> state.raceStatus(5, 9, 135).copy(fanCount = 40000)
            3 -> state.raceStatus(5, 9, 135).copy(fanCount = 60000)
            4 -> state.raceStatus(5, 9, 135).copy(fanCount = 30000)
            5 -> state.raceStatus(5, 40, 150)
            else -> null
        }
    }

    override fun getSpecialityRateUp(
        state: SimulationState,
        cardType: StatusType,
    ): Int {
        return state.bcStatus?.specialityRateUp ?: 0
    }

    override fun getHintFrequencyUp(
        state: SimulationState,
        position: StatusType,
    ): Int {
        return state.bcStatus?.hintFrequencyUp ?: 0
    }

    override fun isAllSupportHint(
        state: SimulationState,
        position: StatusType
    ): Boolean {
        return state.bcStatus?.hintAll == true
    }

    override fun getFailureRateDown(
        state: SimulationState,
    ): Int {
        return state.bcStatus?.failureRateDown ?: 0
    }

    override fun getHpCostDown(
        scenarioStatus: ScenarioStatus,
    ): Int {
        return (scenarioStatus as? BCStatus)?.hpCostDown ?: 0
    }

    override fun getTrainingRelationBonus(
        state: SimulationState,
    ): Int {
        return state.bcStatus?.trainingRelationUp ?: 0
    }

    override fun getForceHintCount(
        state: SimulationState,
    ): Int {
        return state.bcStatus?.minHintCount ?: 0
    }
}
