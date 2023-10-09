/*
 * Copyright 2022 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.GrandLiveFactorBasedActionSelector
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.ApproximateSimulationEvents
import io.github.mee1080.umasim.simulation2.LessonClearCalculator
import io.github.mee1080.umasim.simulation2.LessonProvider
import io.github.mee1080.umasim.simulation2.Simulator
import kotlinx.coroutines.runBlocking

fun testLessonClear() {
    val calculator = LessonClearCalculator(
        liveTechniqueCategoryRate[LessonPeriod.Classic]!!,
        liveTechniqueLesson[LessonPeriod.Classic]!!,
    ) { _, restPerformance ->
        restPerformance.countOver(16) * 1000 + restPerformance.totalValue
    }
    val performance = Performance(78, 28, 17, 13, 11)
    val step = 4

    val time = System.currentTimeMillis()
    val result = calculator.calc(performance, step, 0.0001)
    println("calc: ${System.currentTimeMillis() - time} ms")
    println(result.contentToString())
}

fun testProvideLesson() {
    val songs = mutableListOf<SongLesson>()
    for (period in LivePeriod.values()) {
        println(period.name)
        songs.clear()
        for (count in 0 until 40) {
            val lesson = LessonProvider.provide(period, count, songs)
            (lesson[1] as? SongLesson)?.let { songs += it }
            println("$count: ${lesson.joinToString { it.displayName }}")
        }
    }
}

fun singleGrandLiveSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック",
        "[Q≠0]アグネスタキオン",
        "[感謝は指先まで込めて]ファインモーション",
        "[Dear Mr. C.B.]ミスターシービー",
        "[嗚呼華麗ナル一族]ダイイチルビー",
        "[from the GROUND UP]ライトハロー",
    )
    println(chara)
    println(support)
    val selector = GrandLiveFactorBasedActionSelector.speed2Power1Wisdom2Friend1.generateSelector()
    val result = runBlocking {
        Simulator(
            Scenario.GRAND_LIVE,
            chara,
            support,
            factor(StatusType.STAMINA, 2) + factor(StatusType.POWER, 4)
        ).simulateWithHistory(
            selector,
        ) { ApproximateSimulationEvents() }
    }
    result.second.forEachIndexed { index, history ->
        println("${index + 1}:")
        println(" ${history.beforeActionState.status}")
        println(" ${history.beforeActionState.member.joinToString { "${it.charaName}=${it.relation}" }}")
        history.beforeActionState.liveStatus?.let { liveStatus ->
            println(" ${history.beforeActionState.status.performance}")
            println(" ${liveStatus.lessonSelection.joinToString { it.displayName }}")
            println(" ${liveStatus.learnedLesson.reversed().joinToString { it.displayName }}")
        }
        println(" ${history.action.toShortString()}")
        println(" ${history.actionResult}")
    }
    println(result.first)
    result.second.last().beforeActionState.liveStatus?.learnedLesson?.forEach { println(it.displayName) }
    println(result.first.status)
    result.second.last().beforeActionState.liveStatus?.learnedLesson?.filter { it is SongLesson }
        ?.map { it.displayName }?.sorted()
        ?.forEach { println(it) }
}