package io.github.mee1080.umasim.ai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.scenario.ramen.*
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

    @Test
    fun testJuniorFriendOutingBlocked() = runTest {
        val selector = TestRamenActionSelector()
        val baseState = Simulator(
            scenario = Scenario.RAMEN,
            chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
            supportCardList = Store.getSupportByName("[一杯のノスタルジア]駿川たづな" to 4)
        ).initialState
        val state = baseState.copy(
            scenarioStatus = RamenStatus(),
            turn = 10,
            status = baseState.status.copy(hp = 50) // Ensure HP is low so not blocked by HP check
        )

        val friendCard = state.member.first { it.outingType }
        val friendOuting = Outing(friendCard, listOf(StatusActionResult(Status()) to 1))
        val context = selector.getContext(state)

        val score = selector.calcScenarioActionScore(context, friendOuting)
        assertEquals(Double.MIN_VALUE, score)
    }

    @Test
    fun testClassicFriendOutingsLimit() = runTest {
        val selector = TestRamenActionSelector()

        // Classic period, turn 30
        val baseState = Simulator(
            scenario = Scenario.RAMEN,
            chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5),
            supportCardList = Store.getSupportByName("[一杯のノスタルジア]駿川たづな" to 4)
        ).initialState
        val state = baseState.copy(
            scenarioStatus = RamenStatus(),
            turn = 30,
            status = baseState.status.copy(hp = 50) // Ensure HP is low so not blocked by HP check
        )

        val friendCardIndex = state.member.indexOfFirst { it.outingType }
        val friendCard = state.member[friendCardIndex]
        val friendOuting = Outing(friendCard, listOf(StatusActionResult(Status()) to 1))

        // Case 1: 0 previous outings (outingStep = 2)
        val memberWithStep2 = state.member.mapIndexed { index, member ->
            if (index == friendCardIndex) {
                member.copy(supportState = member.supportState?.copy(outingStep = 2))
            } else member
        }
        var context = selector.getContext(state.copy(member = memberWithStep2))
        var score = selector.calcScenarioActionScore(context, friendOuting)
        assertTrue(score != Double.MIN_VALUE)

        // Case 2: 3 previous outings (outingStep = 5)
        val memberWithStep5 = state.member.mapIndexed { index, member ->
            if (index == friendCardIndex) {
                member.copy(supportState = member.supportState?.copy(outingStep = 5))
            } else member
        }
        context = selector.getContext(state.copy(member = memberWithStep5))
        score = selector.calcScenarioActionScore(context, friendOuting)
        assertEquals(Double.MIN_VALUE, score)
    }

    @Test
    fun testHighHpSleepAndOutingBlocked() = runTest {
        val selector = TestRamenActionSelector()

        // HP is 75 (>= 70)
        val state = sampleState.copy(status = sampleState.status.copy(hp = 75))
        val context = selector.getContext(state)

        val sleepAction = Sleep(listOf(StatusActionResult(Status()) to 1))
        val outingAction = Outing(null, listOf(StatusActionResult(Status()) to 1))

        assertEquals(Double.MIN_VALUE, selector.calcScenarioActionScore(context, sleepAction))
        assertEquals(Double.MIN_VALUE, selector.calcScenarioActionScore(context, outingAction))

        // HP is 65 (< 70) -> Should NOT be blocked
        val stateLowHp = sampleState.copy(status = sampleState.status.copy(hp = 65))
        val contextLowHp = selector.getContext(stateLowHp)
        assertTrue(selector.calcScenarioActionScore(contextLowHp, sleepAction) != Double.MIN_VALUE)
    }

    @Test
    fun testClassicPeriodSpeedPriority() {
        val selector = RamenActionSelector()

        val contextJunior = selector.getContext(sampleState.copy(turn = 10))
        val contextClassic = selector.getContext(sampleState.copy(turn = 30))

        assertEquals(100, contextJunior.option.speed)
        assertEquals(50, contextClassic.option.speed)
    }

    @Test
    fun testJuniorPeriodTastingHiddenTipsBlocked() = runTest {
        val selector = TestRamenActionSelector()

        // Junior period, turn 10
        val state = sampleState.copy(
            turn = 10,
            scenarioStatus = RamenStatus(
                selectedRegions = listOf(RamenRegion.SAPPORO)
            )
        )
        val context = selector.getContext(state)

        val tastingWithHidden = RamenTasting(RamenRegion.SAPPORO, listOf(RamenTipType.NOODLE))
        assertEquals(Double.MIN_VALUE, selector.calcScenarioActionScore(context, tastingWithHidden))
    }

    @Test
    fun testCampAndClassicEndAdjustments() = runTest {
        val selector = TestRamenActionSelector()

        // Turn 35 (Before Camp)
        // Target: 7 total tips, 2 hidden tips. (3 or more hidden tips is NOT allowed)
        // Case 1: hiddenTips is 3 (>= 3). Only tastings reducing hiddenTips to <= 2 are allowed.
        val stateHighHidden = sampleState.copy(
            turn = 35,
            scenarioStatus = RamenStatus(
                selectedRegions = listOf(RamenRegion.SAPPORO),
                hiddenTips = 3,
                tips = mapOf(RamenTipType.NOODLE to 5, RamenTipType.SOUP to 5, RamenTipType.TOPPING to 5)
            )
        )
        val contextHighHidden = selector.getContext(stateHighHidden)

        // Tasting reducing hiddenTips to 2 (used 1) -> Allowed
        val tastingReducing = RamenTasting(RamenRegion.SAPPORO, listOf(RamenTipType.NOODLE))
        val scoreReducing = selector.calcScenarioActionScore(contextHighHidden, tastingReducing)
        // Not Double.MIN_VALUE (and should taste can be false, but it's not strictly blocked prior to testing analysis if there's valid training)
        assertTrue(scoreReducing != Double.MIN_VALUE)

        // Tasting not reducing (used 0) -> Blocked
        val tastingNotReducing = RamenTasting(RamenRegion.SAPPORO, emptyList())
        assertEquals(Double.MIN_VALUE, selector.calcScenarioActionScore(contextHighHidden, tastingNotReducing))

        // Non-tasting action -> Blocked if a valid tasting exists
        val sleepAction = Sleep(listOf(StatusActionResult(Status()) to 1))
        assertEquals(Double.MIN_VALUE, selector.calcScenarioActionScore(contextHighHidden, sleepAction))
    }

    @Test
    fun testPriorityUsingHiddenTips() = runTest {
        val selector = TestRamenActionSelector()

        // Classic period, turn 30. Set hidden tips to 4.
        val state = sampleState.copy(
            turn = 30,
            scenarioStatus = RamenStatus(
                selectedRegions = listOf(RamenRegion.SAPPORO),
                hiddenTips = 4,
                tips = mapOf(RamenTipType.NOODLE to 0, RamenTipType.SOUP to 2, RamenTipType.TOPPING to 2)
            )
        )
        val context = selector.getContext(state)
        // Add a training action with high score so tasting can trigger
        val trainingSpeed = Training(
            type = StatusType.SPEED,
            failureRate = 0,
            level = 1,
            member = emptyList(),
            candidates = emptyList(),
            baseStatus = Status(),
            friendTraining = false
        )
        context.add(trainingSpeed, 20.0)

        // Case A: convert 1, remaining = 3 (>= 2)
        val tastingRemaining3 = RamenTasting(RamenRegion.SAPPORO, listOf(RamenTipType.NOODLE))
        // Case B: convert 3, remaining = 1 (< 2)
        val tastingRemaining1 = RamenTasting(RamenRegion.SAPPORO, listOf(RamenTipType.NOODLE, RamenTipType.SOUP, RamenTipType.TOPPING))

        val score3 = selector.calcScenarioActionScore(context, tastingRemaining3)
        val score1 = selector.calcScenarioActionScore(context, tastingRemaining1)

        assertNotNull(score3)
        assertNotNull(score1)
        // score3 should be higher due to the +50.0 priority bonus for remaining >= 2
        assertTrue(score3 > score1)
    }

    @Test
    fun testTieBreakingMinimizingRemainingConverted() = runTest {
        val selector = TestRamenActionSelector()

        // Noodle has 3 tips, Soup has 1 tip.
        // We use 1 hidden tip to convert either Noodle or Soup.
        // Converting Soup leaves 1 - 1 = 0 Soup tips (remaining minimized).
        // Converting Noodle leaves 3 - 1 = 2 Noodle tips.
        val state = sampleState.copy(
            turn = 30,
            scenarioStatus = RamenStatus(
                selectedRegions = listOf(RamenRegion.SAPPORO),
                hiddenTips = 2,
                tips = mapOf(RamenTipType.NOODLE to 3, RamenTipType.SOUP to 1, RamenTipType.TOPPING to 5)
            )
        )
        val context = selector.getContext(state)
        val trainingSpeed = Training(
            type = StatusType.SPEED,
            failureRate = 0,
            level = 1,
            member = emptyList(),
            candidates = emptyList(),
            baseStatus = Status(),
            friendTraining = false
        )
        context.add(trainingSpeed, 20.0)

        // Tasting A: converts Soup (remaining Soup tips after conversion = 0)
        val tastingConvertSoup = RamenTasting(RamenRegion.SAPPORO, listOf(RamenTipType.SOUP))
        // Tasting B: converts Noodle (remaining Noodle tips after conversion = 2)
        val tastingConvertNoodle = RamenTasting(RamenRegion.SAPPORO, listOf(RamenTipType.NOODLE))

        val scoreConvertSoup = selector.calcScenarioActionScore(context, tastingConvertSoup)
        val scoreConvertNoodle = selector.calcScenarioActionScore(context, tastingConvertNoodle)

        assertNotNull(scoreConvertSoup)
        assertNotNull(scoreConvertNoodle)
        // scoreConvertSoup should be higher because remainingConverted tips is 0, which is smaller than 2.
        assertTrue(scoreConvertSoup > scoreConvertNoodle)
    }
}
