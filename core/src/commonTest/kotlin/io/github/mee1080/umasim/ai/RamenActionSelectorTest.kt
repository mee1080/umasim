package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.ramen.RamenRegion
import io.github.mee1080.umasim.scenario.ramen.RamenStatus
import io.github.mee1080.umasim.simulation2.*
import io.github.mee1080.umasim.test.loadTestStore
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RamenActionSelectorTest {

    init {
        loadTestStore()
    }

    class TestRamenActionSelector : RamenActionSelector() {
        public override suspend fun calcScenarioActionScore(
            context: Context,
            action: Action
        ): Double? {
            return super.calcScenarioActionScore(context, action)
        }
    }

    private val sampleState by lazy {
        Simulator(
            scenario = Scenario.RAMEN,
            chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
            supportCardList = emptyList()
        ).initialState.copy(scenarioStatus = RamenStatus())
    }

    @Test
    fun testGetContext() {
        val selector = RamenActionSelector()

        val contextJunior = selector.getContext(sampleState.copy(turn = 10))
        assertEquals(selector.options[0], contextJunior.option)

        val contextClassic = selector.getContext(sampleState.copy(turn = 30))
        assertEquals(selector.options[1], contextClassic.option)

        val contextSenior = selector.getContext(sampleState.copy(turn = 50))
        assertEquals(selector.options[2], contextSenior.option)

        val contextFinals = selector.getContext(sampleState.copy(turn = 75))
        assertEquals(selector.options[3], contextFinals.option)
    }

    @Test
    fun testCalcScenarioActionScore_SelectRegion() = runTest {
        val selector = TestRamenActionSelector()
        val context = selector.getContext(sampleState.copy(turn = 10))

        // Region selection actions
        val selectSapporo = RamenSelectRegion(RamenRegion.SAPPORO)
        val selectHakodate = RamenSelectRegion(RamenRegion.HAKODATE)

        val scoreSapporo = selector.calcScenarioActionScore(context, selectSapporo)
        val scoreHakodate = selector.calcScenarioActionScore(context, selectHakodate)

        assertNotNull(scoreSapporo)
        assertNotNull(scoreHakodate)
        // Since SAPPORO is indexed 0 in regionPriority and HAKODATE is 1, Sapporo score should be higher.
        assertTrue(scoreSapporo > scoreHakodate)
    }

    @Test
    fun testCalcScenarioActionScore_Tasting() = runTest {
        val selector = TestRamenActionSelector()

        // Setup state with a simulated best Training action on SPEED, not at year-end.
        // Tasting region: SAPPORO targets SPEED
        val tastingAction = RamenTasting(RamenRegion.SAPPORO)

        // Scenario 1: Non-matching action or low score -> Tasting should not trigger
        val context1 = selector.getContext(sampleState.copy(turn = 10))
        context1.add(Sleep(emptyList()), 5.0) // best turn change is sleep

        val score1 = selector.calcScenarioActionScore(context1, tastingAction)
        assertEquals(null, score1)

        // Scenario 2: Year end -> Tasting should trigger even if best action is Sleep
        val context2 = selector.getContext(sampleState.copy(turn = 24))
        context2.add(Sleep(emptyList()), 5.0)

        val score2 = selector.calcScenarioActionScore(context2, tastingAction)
        assertNotNull(score2)
        assertTrue(score2 > 0.0)

        // Scenario 3: Strong matching Training -> Tasting should trigger
        val context3 = selector.getContext(sampleState.copy(turn = 10))
        val trainingSpeed = Training(
            type = StatusType.SPEED,
            failureRate = 0,
            level = 1,
            member = emptyList(),
            candidates = emptyList(),
            baseStatus = Status(),
            friendTraining = false
        )
        context3.add(trainingSpeed, 20.0) // 20.0 is >= tastingScoreThreshold (15.0)

        val score3 = selector.calcScenarioActionScore(context3, tastingAction)
        assertNotNull(score3)
        assertTrue(score3 > 1000.0)
    }
}
