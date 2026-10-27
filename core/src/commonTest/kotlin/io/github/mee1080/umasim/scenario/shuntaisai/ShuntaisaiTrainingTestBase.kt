package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.CalculatorTestBase
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.Calculator
import io.github.mee1080.umasim.simulation2.MemberState

abstract class ShuntaisaiTrainingTestBase(
    chara: Triple<String, Int, Int>,
    supportCardList: List<Pair<String, Int>>,
) : CalculatorTestBase(Scenario.SHUNTAISAI, chara, supportCardList) {

    protected fun Calculator.CalcInfo.updateShuntaisaiStatus(action: ShuntaisaiStatus.() -> ShuntaisaiStatus): Calculator.CalcInfo {
        return copy(scenarioStatus = shuntaisaiStatus?.run { action() })
    }

    fun Calculator.CalcInfo.initShuntaisaiStatus(): Calculator.CalcInfo {
        return copy(
            scenarioStatus = ShuntaisaiStatus(
                maxLevel = shuntaisaiAzumaMaxLevel,
                supportTypeCount = supportTypeCount,
            )
        )
    }

    /**
     * 演目Lvを設定（スピード、スタミナ、パワー、根性、賢さの順）
     */
    fun Calculator.CalcInfo.setShuntaisaiLevels(vararg levels: Int) = updateShuntaisaiStatus {
        copy(levels = trainingType.zip(levels.toList()).toMap())
    }

    fun Calculator.CalcInfo.setTeaching(
        type: StatusType,
        teaching: ShuntaisaiTeaching,
        transformedFrom: ShuntaisaiTeaching? = null,
    ) = updateShuntaisaiStatus {
        copy(teachings = teachings + (type to ShuntaisaiTeachingSlot(teaching, transformedFrom)))
    }

    fun Calculator.CalcInfo.setPresentationBonus(index: Int, result: ShuntaisaiPresentationResult) =
        updateShuntaisaiStatus {
            copy(presentationBonus = shuntaisaiPresentationBonus[index][result]!!)
        }

    fun Calculator.CalcInfo.setJoryokuSupport(count: Int) = updateShuntaisaiStatus {
        copy(joryokuSupportCount = count)
    }

    /**
     * 通常トレーニング
     *
     * @param guests 参加しているゲスト（編成外の演目担当）の担当演目
     * @param supportIndices 参加している編成サポカのインデックス
     * @param friends 友情トレーニングが発生している編成サポカのインデックス（絆を100に設定する）
     */
    fun testTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        guests: List<StatusType>,
        vararg supportIndices: Int,
        friends: List<Int> = emptyList(),
        base: Status? = null,
        scenario: Status? = null,
    ) {
        testShuntaisaiTraining(baseCalcInfo, type, level, guests, supportIndices.toList(), friends, false, base, scenario)
    }

    /**
     * 演目稽古（対象演目のゲージを最大にして計算）
     *
     * @param level トレーニングLv（演目Lvは[setShuntaisaiLevels]で設定する）
     */
    fun testEnmokuTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        guests: List<StatusType>,
        vararg supportIndices: Int,
        friends: List<Int> = emptyList(),
        base: Status? = null,
        scenario: Status? = null,
    ) {
        testShuntaisaiTraining(baseCalcInfo, type, level, guests, supportIndices.toList(), friends, true, base, scenario)
    }

    private fun testShuntaisaiTraining(
        baseCalcInfo: Calculator.CalcInfo,
        type: StatusType,
        level: Int,
        guests: List<StatusType>,
        supportIndices: List<Int>,
        friends: List<Int>,
        practice: Boolean,
        base: Status?,
        scenario: Status?,
    ) {
        val supportNames = supportCardList.filterIndexed { index, _ -> supportIndices.contains(index) }.map { it.first }
        val friendNames = supportCardList.filterIndexed { index, _ -> friends.contains(index) }.map { it.first }
        val supportMember = baseCalcInfo.member.filter { supportNames.contains(it.name) }.map {
            if (friendNames.contains(it.name)) it.copy(supportState = it.supportState?.copy(relation = 100)) else it
        }
        val guestMember = guests.mapIndexed { index, guestType ->
            val card = Store.guestSupportCardList.first { it.type == guestType }
            MemberState(100 + index, card, type, null, Scenario.SHUNTAISAI.memberState(card, true))
        }
        val shuntaisaiStatus = baseCalcInfo.shuntaisaiStatus!!.let {
            it.copy(gauges = it.gauges + (type to if (practice) shuntaisaiMaxGauge else 0))
        }
        val info = baseCalcInfo.copy(
            training = if (practice) {
                shuntaisaiStatus.practiceTraining(type, level)
            } else {
                state.training[type.ordinal].base[level - 1]
            },
            member = supportMember + guestMember,
            scenarioStatus = shuntaisaiStatus,
        )
        val bonus = info.scenario.calculator.getScenarioCalcBonus(info)
        val result = Calculator.calcTrainingSuccessStatusSeparated(info, bonus)
        val message = buildString {
            appendLine("expected:")
            appendLine(base)
            appendLine(scenario)
            appendLine("actual:")
            appendLine(result.first.first)
            appendLine(result.second)
            appendLine("params:")
            appendLine(info.chara.name)
            appendLine(info.member.joinToString { "${it.name} ${it.relation}" })
            appendLine(info.training)
            appendLine("motivation=${info.motivation}")
            appendLine(info.shuntaisaiStatus?.toShortString())
        }
        base?.let { assertStatus(it, result.first.first, message) }
        scenario?.let { assertStatus(it, result.second, message) }
    }
}
