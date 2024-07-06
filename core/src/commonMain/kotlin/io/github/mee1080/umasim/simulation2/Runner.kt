package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.SupportCard
import kotlin.math.roundToInt

object Runner {

    val shortEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 1200),
        StatusType.STAMINA to (1.4 to 400),
        StatusType.POWER to (1.2 to 1200),
        StatusType.GUTS to (0.8 to 600),
        StatusType.WISDOM to (0.9 to 1000),
        StatusType.SKILL to (0.4 to Int.MAX_VALUE),
    )

    val gmShortEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 1900),
        StatusType.STAMINA to (1.4 to 450),
        StatusType.POWER to (1.0 to 1500),
        StatusType.GUTS to (0.9 to 1250),
        StatusType.WISDOM to (0.85 to 1250),
        StatusType.SKILL to (1.0 to 3200),
    )

    val mileEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 1600),
        StatusType.STAMINA to (1.4 to 700),
        StatusType.POWER to (1.2 to 1200),
        StatusType.GUTS to (0.6 to 600),
        StatusType.WISDOM to (0.9 to 1200),
        StatusType.SKILL to (0.4 to Int.MAX_VALUE),
    )

    val grandLiveMileEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 2000),
        StatusType.STAMINA to (1.4 to 600),
        StatusType.POWER to (1.0 to 1100),
        StatusType.GUTS to (0.8 to 800),
        StatusType.WISDOM to (0.8 to 1100),
        StatusType.SKILL to (0.4 to Int.MAX_VALUE),
    )

    val gmMileEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 1800),
        StatusType.STAMINA to (1.4 to 600),
        StatusType.POWER to (1.0 to 1500),
        StatusType.GUTS to (0.9 to 1200),
        StatusType.WISDOM to (0.9 to 1200),
        StatusType.SKILL to (1.0 to 3200),
    )

    val middleEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 1200),
        StatusType.STAMINA to (1.4 to 800),
        StatusType.POWER to (1.0 to 1150),
        StatusType.GUTS to (0.8 to 600),
        StatusType.WISDOM to (0.9 to 1000),
        StatusType.SKILL to (0.4 to Int.MAX_VALUE),
    )

    val longEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 1200),
        StatusType.STAMINA to (1.4 to 1000),
        StatusType.POWER to (1.0 to 1100),
        StatusType.GUTS to (0.8 to 600),
        StatusType.WISDOM to (0.9 to 1000),
        StatusType.SKILL to (0.4 to Int.MAX_VALUE),
    )

    val grandLiveLongEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 2000),
        StatusType.STAMINA to (1.4 to 1000),
        StatusType.POWER to (1.0 to 1100),
        StatusType.GUTS to (0.8 to 700),
        StatusType.WISDOM to (0.7 to 1000),
        StatusType.SKILL to (0.4 to Int.MAX_VALUE),
    )

    val gmMiddleEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 1800),
        StatusType.STAMINA to (1.4 to 900),
        StatusType.POWER to (1.0 to 1400),
        StatusType.GUTS to (0.7 to 1200),
        StatusType.WISDOM to (1.0 to 1200),
        StatusType.SKILL to (1.0 to 3200),
    )

    val gmLongEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 1800),
        StatusType.STAMINA to (1.4 to 1200),
        StatusType.POWER to (1.0 to 1200),
        StatusType.GUTS to (0.7 to 1200),
        StatusType.WISDOM to (1.0 to 1200),
        StatusType.SKILL to (0.8 to 3200),
    )

    val lArcFlatEvaluateSetting = mapOf(
        StatusType.SPEED to (1.0 to 2000),
        StatusType.STAMINA to (1.0 to 2000),
        StatusType.POWER to (1.0 to 1800),
        StatusType.GUTS to (1.0 to 1800),
        StatusType.WISDOM to (1.0 to 1400),
        StatusType.SKILL to (1.0 to 3600),
    )

    val lArcMileEvaluateSetting = mapOf(
        StatusType.SPEED to (1.2 to 2000),
        StatusType.STAMINA to (1.0 to 750),
        StatusType.POWER to (1.0 to 1800),
        StatusType.GUTS to (1.0 to 1200),
        StatusType.WISDOM to (1.0 to 1300),
        StatusType.SKILL to (0.8 to 3600),
    )

    val lArcMiddleEvaluateSetting = mapOf(
        StatusType.SPEED to (1.0 to 2000),
        StatusType.STAMINA to (1.0 to 1250),
        StatusType.POWER to (1.0 to 1800),
        StatusType.GUTS to (0.8 to 1200),
        StatusType.WISDOM to (1.0 to 1300),
        StatusType.SKILL to (0.8 to 3600),
    )

    val lArcLongEvaluateSetting = mapOf(
        StatusType.SPEED to (1.3 to 2000),
        StatusType.STAMINA to (1.3 to 1600),
        StatusType.POWER to (1.0 to 1400),
        StatusType.GUTS to (0.7 to 1000),
        StatusType.WISDOM to (1.0 to 1100),
        StatusType.SKILL to (0.8 to 3600),
    )

    val uafMileEvaluateSetting = mapOf(
        StatusType.SPEED to (1.1 to 2200),
        StatusType.STAMINA to (1.2 to 900),
        StatusType.POWER to (1.1 to 1800),
        StatusType.GUTS to (1.2 to 1200),
        StatusType.WISDOM to (1.1 to 1400),
        StatusType.SKILL to (0.8 to 4000),
    )

    val uafLongEvaluateSetting = mapOf(
        StatusType.SPEED to (1.1 to 2200),
        StatusType.STAMINA to (1.15 to 1600),
        StatusType.POWER to (1.1 to 1500),
        StatusType.GUTS to (1.2 to 1200),
        StatusType.WISDOM to (1.1 to 1400),
        StatusType.SKILL to (0.8 to 4000),
    )

    val cookMileEvaluateSetting = mapOf(
        StatusType.SPEED to (1.1 to 2400),
        StatusType.STAMINA to (1.2 to 900),
        StatusType.POWER to (1.1 to 2200),
        StatusType.GUTS to (1.1 to 2200),
        StatusType.WISDOM to (1.1 to 1400),
        StatusType.SKILL to (0.8 to 5000),
    )

    val noLimitSetting = mapOf(
        StatusType.SPEED to (1.0 to 10000),
        StatusType.STAMINA to (1.0 to 10000),
        StatusType.POWER to (1.0 to 10000),
        StatusType.GUTS to (1.0 to 10000),
        StatusType.WISDOM to (1.0 to 10000),
        StatusType.SKILL to (0.8 to 10000),
    )

    suspend fun runAndEvaluate(
        count: Int,
        scenario: Scenario,
        chara: Chara,
        supportCardList: List<SupportCard>,
        factorList: List<Pair<StatusType, Int>> = emptyList(),
        evaluateSetting: Map<StatusType, Pair<Double, Int>>,
        events: (SimulationState) -> SimulationEvents = { RandomEvents(it) },
        selector: () -> ActionSelector,
    ): Pair<Double, Evaluator> {
        val summaries = run(count, scenario, chara, supportCardList, factorList, events, selector)
        val evaluator = Evaluator(summaries, evaluateSetting, 0.2)
        return (evaluator.upperSum(1.0, evaluateSetting) * 1000).roundToInt() / 1000.0 to evaluator
    }

    suspend fun run(
        count: Int,
        scenario: Scenario,
        chara: Chara,
        supportCardList: List<SupportCard>,
        factorList: List<Pair<StatusType, Int>> = emptyList(),
        events: (SimulationState) -> SimulationEvents = { RandomEvents(it) },
        selector: () -> ActionSelector,
    ): List<Summary> {
        val summaries = mutableListOf<Summary>()
        repeat(count) {
            summaries.add(
                Simulator(
                    scenario,
                    chara,
                    supportCardList,
                    factorList,
                ).simulate(selector(), events)
            )
        }
        return summaries
    }
}