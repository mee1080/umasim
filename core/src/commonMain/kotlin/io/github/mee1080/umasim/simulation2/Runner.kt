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
        StatusType.STAMINA to (1.4 to 700),
        StatusType.POWER to (1.0 to 1500),
        StatusType.GUTS to (0.9 to 1200),
        StatusType.WISDOM to (0.9 to 1200),
        StatusType.SKILL to (0.6 to 3200),
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

    fun runAndEvaluate(
        count: Int,
        scenario: Scenario,
        chara: Chara,
        supportCardList: List<SupportCard>,
        factorList: List<Pair<StatusType, Int>> = emptyList(),
        evaluateSetting: Map<StatusType, Pair<Double, Int>>,
        turn: Int = 78,
        events: (SimulationState) -> SimulationEvents = { RandomEvents(it) },
        selector: () -> ActionSelector,
    ): Pair<Double, Evaluator> {
        val summaries = run(count, scenario, chara, supportCardList, factorList, turn, events, selector)
        val evaluator = Evaluator(summaries, evaluateSetting, 0.2)
        return (evaluator.upperSum(0.2, evaluateSetting) * 1000).roundToInt() / 1000.0 to evaluator
    }

    fun run(
        count: Int,
        scenario: Scenario,
        chara: Chara,
        supportCardList: List<SupportCard>,
        factorList: List<Pair<StatusType, Int>> = emptyList(),
        turn: Int = 78,
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
                ).simulate(turn, selector(), events)
            )
        }
        return summaries
    }
}