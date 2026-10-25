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

    @Test
    fun testNoBonus() {
        val info = simulator.initialState.baseCalcInfo.copy(scenarioStatus = ShuntaisaiStatus(turn = 5))
        val result = ShuntaisaiCalculator.calcScenarioStatus(info, baseStatus, ExpectedStatus(), false)
        assertEquals(Status(), result)
    }

    @Test
    fun testPractice() {
        val shuntaisaiStatus = ShuntaisaiStatus(turn = 5).addGauge(StatusType.SPEED, 5)
        val training = simulator.initialState.training.first { it.type == StatusType.SPEED }.base[0]
        val info = simulator.initialState.baseCalcInfo.copy(training = training, scenarioStatus = shuntaisaiStatus)
        val result = ShuntaisaiCalculator.calcScenarioStatus(info, baseStatus, ExpectedStatus(), false)
        val practice = shuntaisaiPracticeStatus(0, 1)
        assertEquals(practice.speed, result.speed)
        assertEquals(practice.wisdom, result.wisdom)
        assertEquals(practice.skillPt, result.skillPt)
    }

    @Test
    fun testTeaching() {
        val shuntaisaiStatus = ShuntaisaiStatus(turn = 30)
            .assignTeaching(ShuntaisaiTeaching.TOKKA, StatusType.SPEED)
        val training = simulator.initialState.training.first { it.type == StatusType.SPEED }.base[0]
        val info = simulator.initialState.baseCalcInfo.copy(training = training, scenarioStatus = shuntaisaiStatus)
        val result = ShuntaisaiCalculator.calcScenarioStatus(info, baseStatus, ExpectedStatus(), false)
        // 特化: 主ステのみ上昇
        assertEquals(baseStatus.speed * ShuntaisaiTeaching.TOKKA.effect.mainStatusUp / 100, result.speed)
        assertEquals(0, result.power)
    }

    @Test
    fun testFriendBonus() {
        val shuntaisaiStatus = ShuntaisaiStatus(turn = 50)
            .assignTeaching(ShuntaisaiTeaching.KYORYOKU, StatusType.SPEED)
        val training = simulator.initialState.training.first { it.type == StatusType.SPEED }.base[0]
        val info = simulator.initialState.baseCalcInfo.copy(training = training, scenarioStatus = shuntaisaiStatus)
        val friend = ShuntaisaiCalculator.calcScenarioStatus(info, Status(20, 0, 10, 0, 0, 10), ExpectedStatus(), true)
        val notFriend = ShuntaisaiCalculator.calcScenarioStatus(info, Status(20, 0, 10, 0, 0, 10), ExpectedStatus(), false)
        assertEquals(20 * ShuntaisaiTeaching.KYORYOKU.effect.friendBonus / 100, friend.speed)
        assertEquals(0, notFriend.speed)
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
