package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.scenario.ScenarioCalculator
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.simulation2.Calculator.CalcInfo
import io.github.mee1080.umasim.simulation2.Calculator.applyMember
import kotlin.math.min
import kotlin.random.Random

object MujintoCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        val mujintoStatus = info.mujintoStatus ?: return Status()
        val trainingEffect = if (info.isLevelUpTurn) {
            mujintoStatus.campTrainingEffect(info.training.type)
        } else {
            mujintoStatus.evaluationBonus.trainingEffect
        }
        return Status(
            speed = base.speed * trainingEffect / 100,
            stamina = base.stamina * trainingEffect / 100,
            power = base.power * trainingEffect / 100,
            guts = base.guts * trainingEffect / 100,
            wisdom = base.wisdom * trainingEffect / 100,
            skillPt = base.skillPt * trainingEffect / 100,
        )
    }

    override fun predictScenarioActionParams(
        state: SimulationState,
        baseActions: List<Action>,
    ): List<Action> {
        val mujintoStatus = state.mujintoStatus ?: return baseActions
        if (state.turn <= 2 || state.turn >= 61) return baseActions

        return baseActions.map { action ->
            when (action) {
                // トレーニング
                is Training -> {
                    val bonus = mujintoStatus.evaluationBonus.pioneerPointBonus + if (action.friendTraining) 20 else 0
                    val pioneerPoint = ((60 + action.member.size * 6) * (100 + bonus) / 100)
                    action.copy(
                        candidates = action.addScenarioActionParam(
                            MujintoActionParam(
                                pioneerPoint = pioneerPoint,
                                upgradeFacility = !state.isLevelUpTurn,
                            )
                        )
                    )
                }

                // 島トレ
                is MujintoTraining -> {
                    val bonus = mujintoStatus.evaluationBonus.pioneerPointBonus + if (action.friendTraining) 20 else 0
                    val pioneerPoint = ((60 + action.member.size * 6) * (100 + bonus) / 100)
                    action.copy(result = action.result.copy(pioneerPoint = pioneerPoint))
                }

                // レース
                is Race -> {
                    val bonus = mujintoStatus.evaluationBonus.pioneerPointBonus +
                            if (action.goal || mujintoStatus.notGoalRacePioneerPtBonusEnabled()) 200 else 0
                    val pioneerPoint = 20 * (100 + bonus) / 100
                    action.copy(
                        result = action.result.addScenarioActionParam(
                            MujintoActionParam(
                                pioneerPoint = pioneerPoint,
                                upgradeFacility = !state.isLevelUpTurn,
                            )
                        )
                    )
                }

                else -> action
            }
        }
    }

    override fun predictScenarioAction(
        state: SimulationState,
        goal: Boolean,
    ): Array<Action> {
        val mujintoStatus = state.mujintoStatus ?: return emptyArray()
        val scenarioActions = mutableListOf<Action>()

        if (!goal && !state.isLevelUpTurn && mujintoStatus.islandTrainingTicket > 0) {
            scenarioActions.add(predictIslandTraining(state))
        }

        return scenarioActions.toTypedArray()
    }

    /**
     * 島トレ
     */
    private fun predictIslandTraining(
        state: SimulationState,
    ): Action {
        val noPositionMember = state.member
            .filter { it.position == StatusType.NONE }
            .map {
                it to when {
                    it.guest -> 2000
                    it.card.type == StatusType.FRIEND -> 0
                    else -> 1000
                } + Random.nextDouble(1000.0)
            }
            .sortedBy { it.second }
            .take(5)
            .map { it.first.copy(position = StatusType.FRIEND) }
        val calcInfo = state.baseCalcInfo.copy(
            member = state.member.filter { it.position != StatusType.NONE } + noPositionMember,
        )
        val (base, scenario, friend) = calcIslandTrainingStatusSeparated(calcInfo)
        return MujintoTraining(
            member = calcInfo.member,
            friendTraining = friend,
            result = MujintoTrainingResult(
                status = base.first + scenario,
                member = calcInfo.member,
                friendTraining = friend,
            ),
        )
    }

    suspend fun applyScenarioAction(
        state: SimulationState,
        result: MujintoActionResult,
        selector: ActionSelector,
    ): SimulationState {
        return when (result) {
            is MujintoTrainingResult -> {
                val dummyResult = StatusActionResult(
                    status = result.status,
                    scenarioActionParam = MujintoActionParam(pioneerPoint = result.pioneerPoint),
                    success = true,
                )
                val dummyTraining = Training(
                    type = StatusType.FRIEND,
                    failureRate = 0,
                    level = 0,
                    member = result.member,
                    candidates = listOf(dummyResult to 1),
                    baseStatus = result.status,
                    friendTraining = result.friendTraining,
                )
                state.updateMujintoStatus { copy(islandTrainingTicket = islandTrainingTicket - 1) }
                    .applyStatusAction(dummyTraining, dummyResult, selector, 3)
            }

            is MujintoAddPlanResult -> {
                throw NotImplementedError("MujintoScenarioEventsで処理")
            }
        }
    }

    fun applyScenarioActionParam(
        state: SimulationState,
        result: ActionResult,
        params: MujintoActionParam,
    ): SimulationState {
        if (!result.success) return state
        return state.updateMujintoStatus {
            addPioneerPoint(params.pioneerPoint, params.upgradeFacility)
        }
    }

    fun calcIslandTrainingStatusSeparated(
        info: CalcInfo,
    ): Triple<Pair<Status, ExpectedStatus>, Status, Boolean> {
        val friendCount = info.support.count { it.isFriendTraining(it.position) }
        val member = info.member
        if (Calculator.DEBUG) {
            member.forEach {
                println("${it.position} ${it.charaName}")
            }
        }
        val raw = ExpectedStatus(
            speed = calcIslandTrainingStatus(info, StatusType.SPEED, friendCount),
            stamina = calcIslandTrainingStatus(info, StatusType.STAMINA, friendCount),
            power = calcIslandTrainingStatus(info, StatusType.POWER, friendCount),
            guts = calcIslandTrainingStatus(info, StatusType.GUTS, friendCount),
            wisdom = calcIslandTrainingStatus(info, StatusType.WISDOM, friendCount),
            skillPt = calcIslandTrainingStatus(info, StatusType.SKILL, friendCount),
        )
        val base = Status(
            speed = raw.speed.toInt(),
            stamina = raw.stamina.toInt(),
            power = raw.power.toInt(),
            guts = raw.guts.toInt(),
            wisdom = raw.wisdom.toInt(),
            skillPt = raw.skillPt.toInt(),
        )
        return Triple(
            base to raw,
            calcIslandTrainingBonus(info, friendCount, base),
            friendCount > 0,
        )
    }

    private fun calcIslandTrainingStatus(
        info: CalcInfo,
        targetType: StatusType,
        friendCount: Int,
    ): Double {
        val mujintoStatus = info.mujintoStatus ?: return 0.0
        val support = info.member.filter { !it.guest }
        val baseStatus = mujintoIslandTrainingBase.status.get(targetType)
        val baseCondition = info.baseSpecialUniqueCondition(
            trainingSupportCount = support.size,
            friendTraining = friendCount > 0,
        )

        // ステータスボーナス
        val base = baseStatus + support.sumOf {
            it.card.getBaseBonus(targetType, baseCondition.applyMember(it))
        } + mujintoStatus.islandTrainingBonus.get(targetType)

        // キャラボーナス
        val charaBonus = info.chara.getBonus(targetType) / 100.0

        // 友情
        val friend = support.map {
            if (it.isFriendTraining(it.position)) {
                val rate = mujintoIslandTrainingRate(it.card.type, it.position, targetType)
                calcFriendFactor(it.card, baseCondition.applyMember(it), rate)
            } else 1.0
        }.fold(1.0) { acc, d -> acc * d }

        // やる気
        val motivationBase = info.motivation / 10.0
        val motivationBonus = 1 + motivationBase * (1 + (support.sumOf {
            val rate = mujintoIslandTrainingRate(it.card.type, it.position, targetType)
            (it.card.motivationFactor(baseCondition.applyMember(it)) * rate).toInt()
        }) / 100.0)

        // トレ効果
        val trainingBonus =
            1 + (support.sumOf {
                val rate = mujintoIslandTrainingRate(it.card.type, it.position, targetType)
                (it.card.trainingFactor(baseCondition.applyMember(it)) * rate).toInt()
            }) / 100.0

        val trainingSupportCount = support.count { it.position != StatusType.FRIEND }
        val otherSupportCount = support.size - trainingSupportCount
        val trainingGuestCount = info.member.count { it.guest && it.position != StatusType.FRIEND }
        val otherGuestCount = info.member.size - support.size - trainingGuestCount
        val count =
            1.0 + trainingSupportCount * 0.05 + trainingGuestCount * 0.02 + otherSupportCount * 0.01 + otherGuestCount * 0.00
        val mujintoFriendBonus = (100 + friendCount * mujintoStatus.trainingEffectByFriend) / 100.0
        val raw = base * charaBonus * friend * motivationBonus * trainingBonus * count * mujintoFriendBonus
        if (Calculator.DEBUG) {
            println("$targetType $raw base=$baseStatus baseBonus=$base chara=$charaBonus friend=$friend motivation=$motivationBonus training=$trainingBonus count=$count mujintoFriend=$mujintoFriendBonus")
        }
        return min(100.0, raw + 0.0002)
    }

    private fun calcFriendFactor(
        card: SupportCard,
        condition: SpecialUniqueCondition,
        rate: Double,
    ) =
        (100 + (card.status.friend * rate).toInt()) * (100 + (card.unique.friend * rate).toInt()) * (100 + (card.specialUnique.sumOf {
            it.friendFactor(card, condition)
        } * rate).toInt()) / 1000000.0

    private fun calcIslandTrainingBonus(
        info: CalcInfo,
        friendCount: Int,
        base: Status,
    ): Status {
        val mujintoStatus = info.mujintoStatus ?: return Status()
        val friendPositionCount = info.support
            .filter { it.isFriendTraining(it.position) }
            .groupBy { it.position }
            .size
        val friendPositionBonus = if (mujintoStatus.friendFacilityCountBonusEnabled()) when (friendPositionCount) {
            5 -> 80
            4 -> 70
            3 -> 65
            2 -> 25
            else -> 0
        } else 0
        if (Calculator.DEBUG) {
            println("friendPositionCount=$friendPositionCount friendPositionBonus=$friendPositionBonus")
        }
        return Status(
            speed = min(
                100, base.speed * (friendPositionBonus + mujintoStatus.islandTrainingEffect(
                    StatusType.SPEED, friendCount
                )) / 100
            ),
            stamina = min(
                100, base.stamina * (friendPositionBonus + mujintoStatus.islandTrainingEffect(
                    StatusType.STAMINA, friendCount
                )) / 100
            ),
            power = min(
                100, base.power * (friendPositionBonus + mujintoStatus.islandTrainingEffect(
                    StatusType.POWER, friendCount
                )) / 100
            ),
            guts = min(
                100, base.guts * (friendPositionBonus + mujintoStatus.islandTrainingEffect(
                    StatusType.GUTS, friendCount
                )) / 100
            ),
            wisdom = min(
                100, base.wisdom * (friendPositionBonus + mujintoStatus.islandTrainingEffect(
                    StatusType.WISDOM, friendCount
                )) / 100
            ),
            skillPt = min(
                100, base.skillPt * (friendPositionBonus + mujintoStatus.islandTrainingEffect(
                    StatusType.SKILL, friendCount
                )) / 100
            ),
        )
    }

    override fun getTraining(
        state: SimulationState,
        trainingType: StatusType
    ): TrainingBase? {
        if (!state.isLevelUpTurn) return null
        val mujintoStatus = state.mujintoStatus ?: return null
        val training = getMujintoCampTrainingData(trainingType, mujintoStatus.getFacilityLevel(trainingType))
        val bonus = mujintoStatus.islandTrainingBonus
        val baseStatus = training.status + Status(
            speed = if (training.status.speed > 0) bonus.speed else 0,
            stamina = if (training.status.stamina > 0) bonus.stamina else 0,
            power = if (training.status.power > 0) bonus.power else 0,
            guts = if (training.status.guts > 0) bonus.guts else 0,
            wisdom = if (training.status.wisdom > 0) bonus.wisdom else 0,
            skillPt = bonus.skillPt,
        )
        return training.copy(status = baseStatus)
    }

    override fun getScenarioCalcBonus(
        baseInfo: CalcInfo,
    ): Calculator.ScenarioCalcBonus? {
        if (!baseInfo.isLevelUpTurn) return null
        val mujintoStatus = baseInfo.mujintoStatus ?: return null
        val friendCount = baseInfo.member.count { it.isFriendTraining(baseInfo.training.type) }
        val mujintoFriendBonus = (100 + friendCount * mujintoStatus.trainingEffectByFriend) / 100.0
        return Calculator.ScenarioCalcBonus(additionalFactor = mujintoFriendBonus)
    }

    override fun getSpecialityRateUp(
        state: SimulationState,
        cardType: StatusType
    ): Int {
        val mujintoStatus = state.mujintoStatus ?: return 0
        return if (state.isLevelUpTurn) {
            mujintoStatus.campSpecialityRate(cardType)
        } else {
            mujintoStatus.specialityRate(cardType)
        }
    }

    override fun getPositionRateUp(state: SimulationState): Int {
        return state.mujintoStatus?.positionRateUp() ?: 0
    }

    override fun getHintFrequencyUp(
        state: SimulationState,
        position: StatusType
    ): Int {
        val mujintoStatus = state.mujintoStatus ?: return 0
        return mujintoStatus.hintUp() + if (state.isLevelUpTurn) {
            mujintoStatus.campHintUp(position)
        } else 0
    }

    override fun isAllSupportHint(
        state: SimulationState,
        position: StatusType
    ): Boolean {
        return state.isLevelUpTurn && state.mujintoStatus?.campHintAll(position) ?: false
    }
}
