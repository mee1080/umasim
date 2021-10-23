package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
import io.github.mee1080.umasim.ai.SimpleActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.turnToString
import io.github.mee1080.umasim.simulation2.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime


fun testAoharuSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        *(speed(4, 3)),
        *(wisdom(4, 2)),
        *(friend(4, 1)),
    )
    val simulator = Simulator(
        Scenario.AOHARU, chara, support, Simulator.Option(
            checkGoalRace = true,
        )
    )
    val result =
        simulator.simulateWithHistory(78, FactorBasedActionSelector2(FactorBasedActionSelector2.aoharuSpeedWisdom))
    result.second.forEachIndexed { index, (action, result, state) ->
        println("${turnToString(state.turn)}: ${action.toShortString()}")
        println(state.status)
        println(state.teamStatusRank.map { "${it.key}:${it.value.rank}" }.joinToString(" "))
        println(state.training.joinToString(" ") { "${it.type}:${it.level}" })
        state.teamMember.forEach {
            val aoharuState = it.scenarioState as AoharuMemberState
            println("${it.name}: ${aoharuState.status.toShortString()}/${aoharuState.maxStatus.toShortString()} ${aoharuState.aoharuTrainingCount}")
        }
        println(result)
        println()
    }
}

fun compareAoharuSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        *(speed(4, 3)),
        *(wisdom(4, 3)),
    )

    val turn = 78
    val testCount = 1
    val selector = { SimpleActionSelector(StatusType.SPEED) }
//    val selector = { FactorBasedActionSelector2(FactorBasedActionSelector2.speedWisdom) }

    runBlocking {
        println(LocalDateTime.now())
        launch(context) {
            val summary = mutableListOf<Summary>()
            val simulator = Simulator(Scenario.URA, chara, support)
            repeat(testCount) {
                summary.add(simulator.simulate(turn, selector(), SimulationEvents(
                    initialStatus = { it.copy(motivation = 2) }
                )))
            }
            println("URA,${Evaluator(summary).toSummaryString()}")
            summary.last().support.forEach { println("${it.name} ${it.state.supportState} ${it.state.scenarioState}") }
        }.join()
        println(LocalDateTime.now())
        launch(context) {
            val summary = mutableListOf<Summary>()
            val simulator = Simulator(Scenario.AOHARU, chara, support)
            repeat(testCount) {
                summary.add(simulator.simulate(turn, selector(), SimulationEvents(
                    initialStatus = { it.copy(motivation = 2) }
                )))
            }
            println("Aoharu,${Evaluator(summary).toSummaryString()}")
            summary.last().support.forEach { println("${it.name} ${it.state.supportState} ${it.state.scenarioState}") }
        }.join()
        println(LocalDateTime.now())
    }
}