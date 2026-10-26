package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.ExpectedStatus
import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.ActionSelector
import io.github.mee1080.umasim.simulation2.Simulator
import io.github.mee1080.umasim.test.loadTestStore
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ShuntaisaiCalculatorTest {

    init {
        loadTestStore()
    }

    private val simulator by lazy {
        Simulator(
            scenario = Scenario.SHUNTAISAI,
            chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
            supportCardList = Store.getSupportByName(
                "[世界を変える眼差し]アーモンドアイ" to 4,
                "[心覚えし、京の華]エアグルーヴ" to 4,
                "[アルストロメリアの夢]ヴィブロス" to 4,
                "[Devilish Whispers]スティルインラブ" to 4,
                "[The frontier]ジャングルポケット" to 4,
                "[永久の誓い、永久の輝き]サトノダイヤモンド" to 4,
            ),
        )
    }

    private val baseStatus = Status(10, 0, 5, 0, 0, 5)

    private val speedTraining by lazy { simulator.initialState.training.first { it.type == StatusType.SPEED }.base[0] }

    @Test
    fun testNoBonus() {
        val info = simulator.initialState.baseCalcInfo.copy(
            training = speedTraining,
            member = emptyList(),
            scenarioStatus = ShuntaisaiStatus(turn = 5),
        )
        val result = ShuntaisaiCalculator.calcScenarioStatus(info, baseStatus, ExpectedStatus(), false)
        assertEquals(Status(), result)
    }

    @Test
    fun testPractice() {
        val shuntaisaiStatus = ShuntaisaiStatus(turn = 5).addGauge(StatusType.SPEED, 5)
        val info = simulator.initialState.baseCalcInfo.copy(
            training = speedTraining,
            member = emptyList(),
            scenarioStatus = shuntaisaiStatus,
        )
        val result = ShuntaisaiCalculator.calcScenarioStatus(info, baseStatus, ExpectedStatus(), false)
        // 全基礎能力+10
        assertEquals(10, result.speed)
        assertEquals(10, result.wisdom)
        assertEquals(0, result.skillPt)
    }

    @Test
    fun testTeaching() {
        val shuntaisaiStatus = ShuntaisaiStatus(turn = 30)
            .assignTeaching(ShuntaisaiTeaching.TOKKA, StatusType.SPEED)
        val member = simulator.initialState.member.filter { it.card.type == StatusType.SPEED }.take(1)
        val info = simulator.initialState.baseCalcInfo.copy(
            training = speedTraining,
            member = member,
            scenarioStatus = shuntaisaiStatus,
        )
        val base = Status(40, 0, 20, 0, 0, 20)
        val result = ShuntaisaiCalculator.calcScenarioStatus(info, base, ExpectedStatus(), false)
        // 特化Lv1: メイン基礎能力のみ5%アップ
        assertEquals(2, result.speed)
        assertEquals(0, result.power)
    }

    @Test
    fun testSimulation() = runTest {
        val summary = simulator.simulateWithHistory(ActionSelector.Random)
        val state = summary.second.last().afterTurnState
        val shuntaisaiStatus = assertNotNull(state.shuntaisaiStatus)
        // ジュニア、クラシックの進捗発表会と本番
        assertEquals(3, shuntaisaiStatus.presentationResults.size)
        // 2回目の発表会後は全ての教えが転成済み
        assertEquals(5, shuntaisaiStatus.teachings.size)
        assertTrue(trainingType.all { shuntaisaiStatus.teachings[it]?.transformedFrom != null })
        assertTrue(shuntaisaiStatus.teachings.values.all { it.teaching.period >= 1 })
        // ゲスト（各演目担当1人ずつ）
        assertEquals(5, state.member.count { it.guest })
    }
}
