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

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation2.*

fun testExpected() {
    val chara = Store.getChara("[初うらら♪さくさくら]ハルウララ", 5, 5)
    val scenario = Scenario.GRAND_LIVE
    val training = Store.getTrainingInfo(scenario).mapValues { it.value.base.last() }
    val motivation = 2
    val teamJoinCount = 0
    val member = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック",
        "[Q≠0]アグネスタキオン",
        "[必殺！Wキャロットパンチ！]ビコーペガサス",
        "[一粒の安らぎ]スーパークリーク",
        "[感謝は指先まで込めて]ファインモーション",
        "[テイオー・オー・オー！！！]トウカイテイオー",
    ).mapIndexed { index, supportCard ->
        MemberState(
            index,
            supportCard,
            StatusType.NONE,
            SupportState(relation = 100, hintIcon = false, passionTurn = 0, friendCount = 5, outingEnabled = false),
            GrandLiveMemberState,
        )
    } + createTeamMemberState(teamJoinCount, scenario)
    val fanCount = 200000
    val currentStatus = Status()
    val totalRelation = 600
    val liveStatus = object : TrainingLiveStatus {
        override val friendTrainingUp = 30
        override val specialityRateUp = 20
        override fun trainingUp(type: StatusType): Int {
            return 3
        }
    }

    val expectedCalcInfo = ExpectedCalculator.ExpectedCalcInfo(
        chara, training, motivation, member, scenario, fanCount, currentStatus, totalRelation,
        0, 0, liveStatus, null,
    )
    val start = System.currentTimeMillis()
    val expected = ExpectedCalculator(expectedCalcInfo).calc()
//    val expected = ExpectedCalculator(expectedCalcInfo, listOf(StatusType.SPEED, StatusType.WISDOM)).calc()
    println("time: ${System.currentTimeMillis() - start} ms")
    println(expected.toString())
    println(expected.statusTotal + expected.skillPt)

    println()

    println("test speed only")
    val current = ExpectedCalculator(expectedCalcInfo, listOf(StatusType.SPEED)).calc()
    println(current.toString())
    println(current.statusTotal + current.skillPt)

    val calcInfo = Calculator.CalcInfo(
        chara, training[StatusType.SPEED]!!, motivation, member, scenario,
        member.distinctBy { it.card.type }.size, fanCount, currentStatus, totalRelation,
        0, 0, liveStatus, null,
    )
    val old = Calculator.calcExpectedTrainingStatus(calcInfo)
    println(old.first.toString())
    println(old.first.statusTotal + old.first.skillPt)
}