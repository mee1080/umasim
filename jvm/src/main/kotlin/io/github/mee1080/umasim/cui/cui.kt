/*
 * Copyright 2021 mee1080
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

import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.Store

val scenario = Scenario.URA

fun openCui(args: Array<String>) {
//    dataCheck()
//    singleSimulation()
//    calcExpected()
//    checkNewSimulator()
//    testAoharuSimulation()
//    compareAoharuSimulation()
//    compareExpectedBasedAI()

    optimizeAI(
        Scenario.URA,
        Store.getChara("ハルウララ", 5, 5),
        Store.getSupportByName(
            *(speed(4, 3)),
            *(power(4, 2)),
            *(friend(4, 1)),
        )
    )

    // 短距離スピパワ
//    optimizeAI(
//        Store.getChara("ハルウララ", 5, 5), Store.getSupportByName(
//            *(speed(4, 3)),
//            *(power(4, 2)),
//            *(friend(4, 1)),
//        ), options = generateOptions(
//            base = FactorBasedActionSelector.Option(),
//            step = 0.05,
//            speed = 0.9..1.0,
//            stamina = 0.8..1.0,
//            power = 0.8..1.0,
//            hp = 0.6..0.7,
//        ), testCount = 1000
//    )
//    doShortSimulation(
//        StatusType.POWER, 0..4, 4, false,
//        100000, FactorBasedActionSelector.speedPower
//    )
//    (1..31 step 10).forEach { charm ->
//        println("愛嬌ターン $charm")
//        doShortSimulation(
//            StatusType.SPEED, 4..4, 4, false,
//            100000, FactorBasedActionSelector.speedPower
//        ) {
//            if (it.turn == charm + 1) {
//                it.condition.add("愛嬌○")
//            }
//        }
//    }
    // 中距離スピパワ
//    optimizeAI(
//        Store.getChara("[ぶっとび☆さまーナイト]マルゼンスキー", 5, 5), Store.getSupportByName(
//            *(speed(4, 3)),
//            *(power3(4, 3)),
//        ), options = generateOptions(
//            base = FactorBasedActionSelector.Option(),
//            step = 0.1,
//            speed = 0.6..0.8,
//            stamina = 1.0..1.2,
//            power = 1.0..1.2,
//            hp = 0.5..0.7,
//        ), testCount = 1000
//    )
//    doShortSimulation(
//        StatusType.POWER, 0..4, 4, false,
//        100000, FactorBasedActionSelector.speedPowerMiddle,
//        needFriend = false,
//        chara = Store.getChara("[ぶっとび☆さまーナイト]マルゼンスキー", 5, 5),
//    )

    // 短距離スピ賢
//    optimizeAI(
//        Store.getChara("ハルウララ", 5, 5),
//        Store.getSupportByName(
//            *(speed(4, 2)),
//            *(wisdom(4, 2)),
//            *(friend(4, 1)),
//            "[押して忍べど燃ゆるもの]ヤエノムテキ" to 4,
//        ),
//        options = generateOptions(
//            step = 0.1,
//            speed = 0.9..1.1,
//            stamina = 0.8..1.0,
//            power = 0.8..1.0,
//            guts = 0.8..0.8,
//            wisdom = 0.6..0.8,
//            hp = 0.5..0.7,
//        ),
//        testCount = 1000, turn = 60,
//    )
//    doShortSimulation(
//        StatusType.WISDOM, 0..4, 4, true,
//        100000, FactorBasedActionSelector.speedWisdomPower
//    )
//    (1..31 step 10).forEach { charm ->
//        println("愛嬌ターン $charm")
//        doShortSimulation(
//            StatusType.SPEED, 4..4, 4, true,
//            100000, FactorBasedActionSelector.speedWisdom
//        ) {
//            if (it.turn == charm + 1) {
//                it.condition.add("愛嬌○")
//            }
//        }
//    }

    // 長距離スピスタ
//    optimizeAI(
//        Store.getChara("ゴールドシップ", 5, 5), Store.getSupportByName(
//            *(speed(4, 3)),
//            *(stamina(4, 3)),
//        ), options = generateOptions(
//            step = 0.1,
//            speed = 1.0..1.2,
//            stamina = 1.0..1.2,
//            power = 0.4..0.6,
//            guts = 0.4..0.6,
//            hp = 0.6..0.8,
//        ), testCount = 1000
//    )
//    doLongSimulation(
//        StatusType.STAMINA, 0..4, 4,
//        100000, option = FactorBasedActionSelector.speedStamina
//    )

    // マイルパワ賢
//    optimizeAI(
//        Store.getChara("スマートファルコン", 5, 5), Store.getSupportByName(
//            *(power2(4, 3)),
//            *(wisdom(4, 3)),
//        ), options = generateOptions(
//            step = 0.1,
//            speed = 0.7..0.9,
//            stamina = 1.0..1.2,
//            power = 1.1..1.3,
//            wisdom = 0.4..0.6,
//            hp = 0.5..0.7,
//        ), turn = 60, testCount = 500
//    )
//    doPowerWisdomSimulation(
//        StatusType.POWER, 0..4, 4,
//        100000, FactorBasedActionSelector.powerWisdom
//    )

    // 短距離根性
//    optimizeAI(
//        Store.getChara("ハルウララ", 5, 5), Store.getSupportByName(
//            *(speed2(4, 2)),
//            *(guts(4, 4)),
//        ), options = generateOptions(
//            base = FactorBasedActionSelector.Option(),
//            step = 0.1,
//            speed = 1.0..1.2,
//            stamina = 0.8..0.9,
//            power = 0.8..1.0,
//            guts = 0.8..1.0,
//            hp = 0.6..0.7,
//        ), testCount = 1000
//    )
//    doShortSimulation(
//        StatusType.GUTS, 0..4, 4, false,
//        100000, FactorBasedActionSelector.speedGuts
//    )

//    doShortSimulation(StatusType.SPEED)
//    doShortSimulation(
//        StatusType.POWER, 0..4, 4, false, 100000, FactorBasedActionSelector.Option(
//            speedFactor = 0.85
//        )
//    )
//    doCharmSimulation()
//    doFailureRateSimulation()
}