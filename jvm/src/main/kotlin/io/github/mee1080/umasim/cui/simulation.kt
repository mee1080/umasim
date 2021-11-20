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

import io.github.mee1080.umasim.ai.FactorBasedActionSelector
import io.github.mee1080.umasim.data.Chara
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.simulation.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import kotlin.math.max


fun doShortSimulation(
    targetStatus: StatusType,
    talent: IntRange = 4..4,
    supportTalent: Int = 4,
    needsWisdom: Boolean = false,
    testCount: Int = 100000,
    option: FactorBasedActionSelector.Option = FactorBasedActionSelector.Option(),
    turnEvent: ((simulator: Simulator) -> Unit)? = null,
    needFriend: Boolean = true,
    chara: Chara = Store.getChara("ハルウララ", 5, 5),
) {
    println(chara)

    val turn = 60
    val defaultSupport = when (targetStatus) {
        StatusType.SPEED -> if (needsWisdom) Store.getSupportByName(
            *(speed(supportTalent, 2)),
            *(wisdom(supportTalent, 2)),
            *(friend(supportTalent, 1)),
        ) else Store.getSupportByName(
            *(speed(supportTalent, 2)),
            *(power(supportTalent, 2)),
            *(friend(supportTalent, 1)),
        )
        StatusType.POWER -> if (needsWisdom) Store.getSupportByName(
            *(speed(supportTalent, 2)),
            *(wisdom(supportTalent, 2)),
            *(friend(supportTalent, 1)),
        ) else if (needFriend) Store.getSupportByName(
            *(speed(supportTalent, 3)),
            *(power(supportTalent, 1)),
            *(friend(supportTalent, 1)),
        ) else Store.getSupportByName(
            *(speed(supportTalent, 3)),
            *(power(supportTalent, 2)),
        )
        StatusType.GUTS -> if (needsWisdom) Store.getSupportByName(
            *(speed2(supportTalent, 2)),
            *(guts(supportTalent, 2)),
            *(wisdom(supportTalent, 1)),
        ) else Store.getSupportByName(
            *(speed2(supportTalent, 2)),
            *(guts(supportTalent, 3)),
        )
        StatusType.WISDOM -> if (needsWisdom) Store.getSupportByName(
            *(speed(supportTalent, 2)),
            *(wisdom(supportTalent, 1)),
            *(friend(supportTalent, 1)),
            "[押して忍べど燃ゆるもの]ヤエノムテキ" to supportTalent,
        ) else Store.getSupportByName(
            *(speed(supportTalent, 3)),
            *(power(supportTalent, 2)),
        )
        else -> Store.getSupportByName(
            *(speed(supportTalent, 3)),
            *(power(supportTalent, 1)),
            *(friend(supportTalent, 1)),
        )
    }.toTypedArray()
    defaultSupport.forEach { println(it.name) }

    val target = Store.supportList.filter {
        talent.contains(it.talent) && it.rarity >= 2 && (it.type == targetStatus)
    }
    doSimulation(
        chara,
        defaultSupport,
        target,
        turn,
        testCount,
        { FactorBasedActionSelector(option) },
        turnEvent,
    )
}

fun doLongSimulation(
    targetStatus: StatusType,
    talent: IntRange = 4..4,
    supportTalent: Int = 4,
    testCount: Int = 100000,
    option: FactorBasedActionSelector.Option = FactorBasedActionSelector.Option(
        speedFactor = 1.2,
        staminaFactor = 1.3,
        powerFactor = 0.6,
        gutsFactor = 0.3,
        wisdomFactor = 0.0,
        hpFactor = 0.5,
    )
) {
    val chara = Store.getChara("ゴールドシップ", 5, 5)
    println(chara)

    val defaultSupport = when (targetStatus) {
        StatusType.SPEED -> Store.getSupportByName(
            *(speed(supportTalent, 2)),
            *(stamina(supportTalent, 3)),
        )
        else -> Store.getSupportByName(
            *(speed(supportTalent, 3)),
            *(stamina(supportTalent, 2)),
        )
    }.toTypedArray()
    defaultSupport.forEach { println(it.name) }

    val target = Store.supportList.filter {
        talent.contains(it.talent) && it.rarity >= 2 && (it.type == targetStatus)
    }
    doSimulation(chara, defaultSupport, target, 60, testCount, {
        FactorBasedActionSelector(option)
    })
}

fun doPowerWisdomSimulation(
    targetStatus: StatusType,
    talent: IntRange = 4..4,
    supportTalent: Int = 4,
    testCount: Int = 100000,
    option: FactorBasedActionSelector.Option
) {
    val chara = Store.getChara("スマートファルコン", 5, 5)
    println(chara)

    val defaultSupport = when (targetStatus) {
        StatusType.WISDOM -> Store.getSupportByName(
            *(power2(supportTalent, 3)),
            *(wisdom(supportTalent, 2)),
        )
        else -> Store.getSupportByName(
            *(power2(supportTalent, 2)),
            *(wisdom(supportTalent, 3)),
        )
    }.toTypedArray()
    defaultSupport.forEach { println(it.name) }

    val target = Store.supportList.filter {
        talent.contains(it.talent) && it.rarity >= 2 && (it.type == targetStatus)
    }
    doSimulation(chara, defaultSupport, target, 60, testCount, {
        FactorBasedActionSelector(option)
    })
}

fun doSimulation(
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    targetStatus: StatusType,
    talent: IntRange = 4..4,
    turn: Int,
    testCount: Int,
    option: FactorBasedActionSelector.Option,
) {
    doSimulation(
        chara,
        defaultSupport,
        Store.supportList.filter {
            talent.contains(it.talent) && it.rarity >= 2 && (it.type == targetStatus)
        },
        turn,
        testCount,
        { FactorBasedActionSelector(option) },
    )
}

fun doSimulation(
    chara: Chara,
    defaultSupport: Array<SupportCard>,
    target: List<SupportCard>,
    turn: Int,
    testCount: Int,
    selector: () -> ActionSelector,
    turnEvent: ((simulator: Simulator) -> Unit)? = null
) {
    println("start ${LocalDateTime.now()}")
    runBlocking {
        target.map { card ->
            launch(context) {
                val useSupport = listOf(*defaultSupport, card)
                val summary = mutableListOf<Summary>()
                repeat(testCount) {
                    val simulator = Simulator(chara, useSupport, Store.getTrainingList(scenario)).apply {
                        status = status.copy(motivation = 2)
                    }
                    summary.add(Runner.simulate(turn, simulator, selector(), turnEvent))
                }
                println("${card.id},${card.name},${card.talent},${Evaluator(summary).toSummaryString()}")
            }
        }.forEach {
            it.join()
        }
    }
    println("finished ${LocalDateTime.now()}")
}

fun doCharmSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック" to 4,
        "[必殺！Wキャロットパンチ！]ビコーペガサス" to 1,
        "[はやい！うまい！はやい！]サクラバクシンオー" to 1,
        "[ロード・オブ・ウオッカ]ウオッカ" to 1,
        "[押して忍べど燃ゆるもの]ヤエノムテキ" to 1,
//        "[ようこそ、トレセン学園へ！]駿川たづな",
//        "[一粒の安らぎ]スーパークリーク",
//        "[その背中を越えて]サトノダイヤモンド",
//        "[『エース』として]メジロマックイーン",
    ).toTypedArray()
    println(chara)
    println(support)

    arrayOf(
        "[まだ小さな蕾でも]ニシノフラワー",
        "[見習い魔女と長い夜]スイープトウショウ",
    ).map { Store.getSupportByName(it to 1).first() }.forEach { card ->
        Array(11) { it * 5 + 1 }.forEach { charmTurn ->
            val summary = mutableListOf<Summary>()
            var restRelation = 0
            val testCount = 10000
            repeat(testCount) {
                val simulator = Simulator(chara, listOf(card, *support), Store.getTrainingList(scenario)).apply {
                    status = status.copy(motivation = 2)
                }
                summary.add(Runner.simulate(55, simulator, FactorBasedActionSelector()) { sim ->
                    if (sim.turn == charmTurn + 1) {
                        sim.condition.add("愛嬌○")
                        restRelation += sim.status.supportRelation.values.sumOf { max(0, 80 - it) }
                    }
                })
            }
            println("${card.name},${charmTurn},${restRelation / 6.0 / testCount},${Evaluator(summary).toSummaryString()}")
        }
    }
}

fun doFailureRateSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック" to 4,
        "[必殺！Wキャロットパンチ！]ビコーペガサス" to 4,
        "[はやい！うまい！はやい！]サクラバクシンオー" to 4,
        "[『愛してもらうんだぞ』]オグリキャップ" to 4,
        "[押して忍べど燃ゆるもの]ヤエノムテキ" to 4,
        "[ようこそ、トレセン学園へ！]駿川たづな" to 4,
//        "[一粒の安らぎ]スーパークリーク" to 4,
//        "[その背中を越えて]サトノダイヤモンド" to 4,
//        "[『エース』として]メジロマックイーン" to 4,
    )
    println(chara)
    println(support)

    val testCount = 100000
    arrayOf(0.5, 0.4).forEach { hpFactor ->
        Array(11) { it * 5 + 1 }.forEach { eventTurn ->
            val summary = mutableListOf<Summary>()
            repeat(testCount) {
                val simulator = Simulator(chara, support, Store.getTrainingList(scenario)).apply {
                    status = status.copy(motivation = 2)
                }
                summary.add(
                    Runner.simulate(
                        55, simulator, FactorBasedActionSelector(
                            FactorBasedActionSelector.Option(hpFactor = hpFactor)
                        )
                    ) { sim ->
                        if (sim.turn == eventTurn + 1) {
                            sim.condition.add("練習上手○")
                        }
                    })
            }
            println("[迫る熱に押されて]キタサンブラック,$hpFactor,${eventTurn},${Evaluator(summary).toSummaryString()}")
        }
    }
}