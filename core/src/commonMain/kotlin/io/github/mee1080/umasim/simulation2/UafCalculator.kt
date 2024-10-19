package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import kotlin.math.min

object UafCalculator : ScenarioCalculator {

    override fun calcScenarioStatus(
        info: Calculator.CalcInfo,
        base: Status,
        raw: ExpectedStatus,
        friendTraining: Boolean,
    ): Status {
        return calcUafStatus(info, raw, friendTraining)
    }

    private fun calcUafStatus(
        info: Calculator.CalcInfo,
        raw: ExpectedStatus,
        isFriendTraining: Boolean,
    ): Status {
        val uafStatus = info.uafStatus ?: return Status()
        val trainingType = info.training.type
        val targetAthletic = uafStatus.trainingAthletics[trainingType]!!
        val linkAthletics = uafStatus.trainingAthletics.filter {
            it.key != trainingType && it.value.genre == targetAthletic.genre
        }
        return Status(
            speed = calcUafStatusSingle(info, linkAthletics, StatusType.SPEED, raw.speed, isFriendTraining),
            stamina = calcUafStatusSingle(info, linkAthletics, StatusType.STAMINA, raw.stamina, isFriendTraining),
            power = calcUafStatusSingle(info, linkAthletics, StatusType.POWER, raw.power, isFriendTraining),
            guts = calcUafStatusSingle(info, linkAthletics, StatusType.GUTS, raw.guts, isFriendTraining),
            wisdom = calcUafStatusSingle(info, linkAthletics, StatusType.WISDOM, raw.wisdom, isFriendTraining),
            skillPt = calcUafStatusSingle(info, linkAthletics, StatusType.SKILL, raw.skillPt, isFriendTraining),
            hp = when (linkAthletics.size) {
                4 -> -3
                3, 2 -> -2
                1 -> -1
                else -> 0
            }
        )
    }

    private fun calcUafStatusSingle(
        info: Calculator.CalcInfo,
        linkAthletics: Map<StatusType, UafAthletic>,
        target: StatusType,
        baseValue: Double,
        isFriendTraining: Boolean,
    ): Int {
        val uafStatus = info.uafStatus ?: return 0
        val trainingType = info.training.type
        val targetLink = linkAthletics[target]
        // リンク先の基本値上昇：各リンク先について、FLOOR((リンク先トレLv+1)/2)？
        val linkBonus = when (target) {
            trainingType -> 0
            StatusType.SKILL -> linkAthletics.size
            else -> targetLink?.let {
                (uafStatus.trainingLevel(it) + 1) / 2
            } ?: 0
        }
        val training = info.training.copy(status = info.training.status.add(target to linkBonus))
        val scenarioInfo = info.copy(training = training)
        val baseInt = baseValue.toInt()
        var total = Calculator.calcTrainingStatus(scenarioInfo, target, isFriendTraining, baseInt == 0)
        // リンク数によって基本上昇量(切り捨て前)に倍率がかかる
        if (target == trainingType && linkAthletics.isNotEmpty()) {
            val baseFactor = when (linkAthletics.size) {
                4 -> 1.3
                3 -> 1.25
                2 -> 1.2
                else -> 1.1
            }
            total *= baseFactor + if (info.isLevelUpTurn) 0.1 else 0.0
        }
        // 大会ボーナス
        val festivalFactor = 1.0 + uafStatus.festivalBonus / 100.0
        total *= festivalFactor
        // ヒートアップ効果
        if (uafStatus.heatUp[UafGenre.Red]!! > 0 && target == trainingType) {
            val heatUpRedFactor = when (linkAthletics.size) {
                4 -> 1.6
                3 -> 1.54
                2 -> 1.45
                1 -> 1.3
                else -> 1.0
            }
            total *= heatUpRedFactor
        }
        if (uafStatus.heatUp[UafGenre.Blue]!! > 0) {
            total += if (target == StatusType.SKILL) {
                // スキルPt：20
                20
            } else {
                // 5ステ：対応する箇所のトレーニングの競技Lv上昇量÷2+1（切り捨て）
                uafStatus.athleticsLevelUp[target]!! / 2 + 1
            }
        }
        return min(100, (total - baseInt).toInt())
    }

    override fun predictScenarioActionParams(state: SimulationState, baseActions: List<Action>): List<Action> {
        val uafStatus = state.uafStatus ?: return baseActions
        return baseActions.map {
            when (it) {
                is Training -> {
                    val genre = uafStatus.trainingAthletics[it.type]!!.genre
                    val levelUp = uafStatus.athleticsLevelUp.mapValues { (key, value) ->
                        if (uafStatus.trainingAthletics[key]!!.genre == genre) value else 0
                    }
                    it.copy(candidates = it.addScenarioActionParam(UafScenarioActionParam(athleticsLevelUp = levelUp)))
                }

                is Sleep -> {
                    it.copy(candidates = it.addScenarioActionParam(UafScenarioActionParam(notTraining = true)))
                }

                is Outing -> {
                    it.copy(candidates = it.addScenarioActionParam(UafScenarioActionParam(notTraining = true)))
                }

                is Race -> {
                    it.copy(result = it.result.addScenarioActionParam(UafScenarioActionParam(notTraining = true)))
                }

                else -> it
            }
        }
    }

    override fun predictScenarioAction(state: SimulationState, goal: Boolean): Array<Action> {
        val uafStatus = state.uafStatus ?: return emptyArray()
        if (goal) return emptyArray()
        if (uafStatus.consultCount == 0) return emptyArray()
        return buildList {
            UafGenre.entries.forEach { genre ->
                if (uafStatus.trainingAthletics.any { it.value.genre == genre }) {
                    addAll(UafConsult.instance[genre]!!)
                }
            }
        }.toTypedArray()
    }

}