package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.FactorBasedActionSelector2
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
        Scenario.AOHARU, chara, support, factor(StatusType.STAMINA, 4) + factor(StatusType.POWER, 2)
    )
    val selector = FactorBasedActionSelector2(FactorBasedActionSelector2.aoharuSpeedWisdom)
//    val selector = NextStateBasedActionSelector(
//        NextStateBasedActionSelector.Option(
//            speedFactor = 0.8,
//            staminaFactor = 1.0,
//            powerFactor = 0.8,
//            wisdomFactor = 1.2,
//            hpFactor = 0.7,z
//        )
//    )
    val result = runBlocking {
        simulator.simulateWithHistory(
            selector
        ) { ApproximateSimulationEvents() }
    }
    result.second.forEachIndexed { index, history ->
        println("${turnToString(history.beforeActionState.turn)}: ${history.action.toShortString()}")
        println(history.beforeActionState.status)
        println(history.beforeActionState.teamStatusRank.map { "${it.key}:${it.value.rank}" }.joinToString(" "))
        println(history.beforeActionState.training.joinToString(" ") { "${it.type}:${it.level}" })
        history.beforeActionState.teamMember.forEach {
            val aoharuState = it.scenarioState as AoharuMemberState
            println("${it.name}: ${aoharuState.status.toShortString()}/${aoharuState.maxStatus.toShortString()} ${aoharuState.aoharuTrainingCount}")
        }
        println(result)
        println()
    }
    println(result.first)
    println(FactorBasedActionSelector2.aoharuSpeedWisdom.aoharuSettingToString())
}

fun compareAoharuSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        *(speed(4, 3)),
        *(wisdom(4, 3)),
    )

    val testCount = 1000
//    val selector = { SimpleActionSelector(StatusType.SPEED) }
    val selector = { FactorBasedActionSelector2(FactorBasedActionSelector2.speedWisdom) }

    runBlocking {
        println(LocalDateTime.now())
        launch(context) {
            val summary = mutableListOf<Summary>()
            val simulator = Simulator(Scenario.URA, chara, support)
            repeat(testCount) {
                summary.add(simulator.simulate(selector()) {
                    SimulationEvents(initialStatus = { it.copy(motivation = 2) })
                })
            }
            println("URA,${Evaluator(summary).toSummaryString()}")
//            summary.last().support.forEach { println("${it.name} ${it.state.supportState} ${it.state.scenarioState}") }
        }.join()
        println(LocalDateTime.now())
        launch(context) {
            val summary = mutableListOf<Summary>()
            val simulator = Simulator(Scenario.AOHARU, chara, support)
            repeat(testCount) {
                summary.add(simulator.simulate(selector()) {
                    SimulationEvents(initialStatus = { it.copy(motivation = 2) })
                })
            }
            println("Aoharu,${Evaluator(summary).toSummaryString()}")
//            summary.last().support.forEach { println("${it.name} ${it.state.supportState} ${it.state.scenarioState}") }
        }.join()
        println(LocalDateTime.now())
    }
}

fun compareExpectedBasedAI() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        *(speed(4, 3)),
        *(power(4, 2)),
        *(wisdom(4, 1)),
    )

    val turn = 78
    val testCount = 100
//    val selector = { SimpleActionSelector(StatusType.SPEED) }
    val option = Simulator.Option()

    runBlocking {
        listOf(0.0, 0.2, 0.4, 0.6, 0.8).forEach { factor ->
            println(LocalDateTime.now())
            launch(context) {
                val summary = mutableListOf<Summary>()
                val simulator = Simulator(Scenario.URA, chara, support)
                val selector = {
                    FactorBasedActionSelector2(
                        FactorBasedActionSelector2.speedWisdom.copy(
                            expectedStatusFactor = factor
                        )
                    )
                }
                repeat(testCount) {
                    summary.add(simulator.simulate(selector()) {
                        SimulationEvents(initialStatus = { it.copy(motivation = 2) }
                        )
                    })
                }
                println("$factor,${Evaluator(summary).toSummaryString()}")
//            summary.last().support.forEach { println("${it.name} ${it.state.supportState} ${it.state.scenarioState}") }
            }.join()
        }
        println(LocalDateTime.now())
    }
}

fun compareNextStateBasedAI() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        *(speed(4, 3)),
        *(power(4, 2)),
        *(wisdom(4, 1)),
    )

    val testCount = 100
//    val selector = { SimpleActionSelector(StatusType.SPEED) }
    val option = Simulator.Option()

    runBlocking {
        listOf(0.0, 0.2, 0.4, 0.6, 0.8).forEach { factor ->
            println(LocalDateTime.now())
            launch(context) {
                val summary = mutableListOf<Summary>()
                val simulator = Simulator(Scenario.URA, chara, support)
                val selector = {
                    FactorBasedActionSelector2(
                        FactorBasedActionSelector2.speedWisdom.copy(
                            expectedStatusFactor = factor
                        )
                    )
                }
                repeat(testCount) {
                    summary.add(simulator.simulate(selector()) {
                        SimulationEvents(initialStatus = { it.copy(motivation = 2) })
                    })
                }
                println("$factor,${Evaluator(summary).toSummaryString()}")
//            summary.last().support.forEach { println("${it.name} ${it.state.supportState} ${it.state.scenarioState}") }
            }.join()
        }
        println(LocalDateTime.now())
    }
}