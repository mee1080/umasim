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
import io.github.mee1080.umasim.ai.SimpleActionSelector
import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.simulation.*
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.math.roundToInt

val scenario = Scenario.URA
const val THREAD_COUNT = 4
lateinit var context: ExecutorCoroutineDispatcher

fun speed(supportTalent: Int, count: Int) = arrayOf(
    "[迫る熱に押されて]キタサンブラック" to supportTalent,
    "[必殺！Wキャロットパンチ！]ビコーペガサス" to supportTalent,
    "[はやい！うまい！はやい！]サクラバクシンオー" to supportTalent,
).take(count).toTypedArray()

fun speed2(supportTalent: Int, count: Int) = arrayOf(
    "[迫る熱に押されて]キタサンブラック" to supportTalent,
    "[花嫁たるもの！！]カワカミプリンセス" to supportTalent,
    "[夢は掲げるものなのだっ！]トウカイテイオー" to supportTalent,
).take(count).toTypedArray()

fun stamina(supportTalent: Int, count: Int) = arrayOf(
    "[一粒の安らぎ]スーパークリーク" to supportTalent,
    "[その背中を越えて]サトノダイヤモンド" to supportTalent,
    "[『エース』として]メジロマックイーン" to supportTalent,
).take(count).toTypedArray()

fun power(supportTalent: Int, count: Int) = arrayOf(
    "[『愛してもらうんだぞ』]オグリキャップ" to supportTalent,
    "[押して忍べど燃ゆるもの]ヤエノムテキ" to supportTalent,
    "[幸せは曲がり角の向こう]ライスシャワー" to supportTalent,
).take(count).toTypedArray()

fun power2(supportTalent: Int, count: Int) = arrayOf(
    "[『愛してもらうんだぞ』]オグリキャップ" to supportTalent,
    "[鍛えぬくトモ]ミホノブルボン" to supportTalent,
    "[テッペンに立て！]ヒシアマゾン" to supportTalent,
).take(count).toTypedArray()

fun power3(supportTalent: Int, count: Int) = arrayOf(
    "[パッションチャンピオーナ！]エルコンドルパサー" to supportTalent,
    "[『愛してもらうんだぞ』]オグリキャップ" to supportTalent,
    "[幸せは曲がり角の向こう]ライスシャワー" to supportTalent,
).take(count).toTypedArray()

fun guts(supportTalent: Int, count: Int) = arrayOf(
    "[うらら～な休日]ハルウララ" to supportTalent,
    "[飛び出せ、キラメケ]アイネスフウジン" to supportTalent,
    "[バカと笑え]メジロパーマー" to supportTalent,
    "[日本一のステージを]スペシャルウィーク" to supportTalent,
    "[Just keep going.]マチカネタンホイザ" to supportTalent,
).take(count).toTypedArray()

fun wisdom(supportTalent: Int, count: Int) = arrayOf(
    "[感謝は指先まで込めて]ファインモーション" to supportTalent,
    "[明日は全国的に赤でしょう♪]セイウンスカイ" to supportTalent,
    "[その心に吹きすさぶ]メジロアルダン" to supportTalent,
).take(count).toTypedArray()

fun friend(supportTalent: Int, count: Int) = arrayOf(
    "[ようこそ、トレセン学園へ！]駿川たづな" to supportTalent,
).take(count).toTypedArray()

fun openCui(args: Array<String>) {
    context = Executors.newFixedThreadPool(THREAD_COUNT).asCoroutineDispatcher()
//    dataCheck()
//    singleSimulation()
//    calcExpected()

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
//        10, FactorBasedActionSelector.speedPowerMiddle,
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
//        100, FactorBasedActionSelector.speedGuts
//    )

//    doShortSimulation(StatusType.SPEED)
//    doShortSimulation(
//        StatusType.POWER, 0..4, 4, false, 100000, FactorBasedActionSelector.Option(
//            speedFactor = 0.85
//        )
//    )
//    doCharmSimulation()
//    doFailureRateSimulation()
    checkNewSimulator()
    context.close()
}

fun singleSimulation() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック",
        "[必殺！Wキャロットパンチ！]ビコーペガサス",
        "[はやい！うまい！はやい！]サクラバクシンオー",
        "[ようこそ、トレセン学園へ！]駿川たづな",
        "[『愛してもらうんだぞ』]オグリキャップ",
        "[押して忍べど燃ゆるもの]ヤエノムテキ",
//        "[感謝は指先まで込めて]ファインモーション",
    )
    println(chara)
    println(support)
    val selector = FactorBasedActionSelector(
        FactorBasedActionSelector.Option(
            speedFactor = 0.9,
            staminaFactor = 0.8,
            wisdomFactor = 0.5,
        )
    )
    val simulator = Simulator(chara, support, Store.getTrainingList(scenario))
    println(simulator.status)
    Runner.simulate(60, simulator, selector) {
//        if (it.turn == 10) it.condition.add("練習上手○")
    }
    simulator.history.forEachIndexed { index, action -> println("${index + 1}: $action") }
    println(simulator.status)
    val summary = simulator.summary
    println(summary)
}

fun generateOptions(
    base: FactorBasedActionSelector.Option = FactorBasedActionSelector.Option(),
    step: Double = 0.1,
    speed: ClosedRange<Double> = 1.0..1.0,
    stamina: ClosedRange<Double> = 1.0..1.0,
    power: ClosedRange<Double> = 1.0..1.0,
    guts: ClosedRange<Double> = 0.0..0.0,
    wisdom: ClosedRange<Double> = 0.0..0.0,
    skillPt: ClosedRange<Double> = 0.4..0.4,
    hp: ClosedRange<Double> = 0.5..0.5,
    motivation: ClosedRange<Double> = 15.0..15.0,
): Array<FactorBasedActionSelector.Option> {
    val list = mutableListOf<FactorBasedActionSelector.Option>()
    var option = base
    val conv = { v: Double -> (v * 100).roundToInt() / 100.0 }
    (speed step step).map { sp ->
        option = option.copy(speedFactor = conv(sp))
        (stamina step step).map { st ->
            option = option.copy(staminaFactor = conv(st))
            (power step step).map { pw ->
                option = option.copy(powerFactor = conv(pw))
                (guts step step).map { gt ->
                    option = option.copy(gutsFactor = conv(gt))
                    (wisdom step step).map { ws ->
                        option = option.copy(wisdomFactor = conv(ws))
                        (skillPt step step).map { sk ->
                            option = option.copy(skillPtFactor = conv(sk))
                            (hp step step).map { hp ->
                                option = option.copy(hpFactor = conv(hp))
                                (motivation step step).map { mt ->
                                    option = option.copy(motivationFactor = conv(mt))
                                    list.add(option)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return list.toTypedArray()
}

infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    var value = start - step
    return generateSequence {
        value += step
        if (value > endInclusive + step / 1000.0) null else value
    }.asIterable()
}

fun optimizeAI(
    chara: Chara,
    support: List<SupportCard>,
    turn: Int = 55,
    testCount: Int = 100,
    vararg options: FactorBasedActionSelector.Option
) {
    println(chara)
    support.forEach { println(it.name) }
    runBlocking {
        options.mapIndexed { index, option ->
            launch(context) {
                val summary = mutableListOf<Summary>()
                repeat(testCount) {
                    val simulator = Simulator(chara, support, Store.getTrainingList(scenario)).apply {
                        status = status.copy(motivation = 2)
                    }
                    summary.add(
                        Runner.simulate(
                            turn,
                            simulator,
                            FactorBasedActionSelector(option)
                        )
                    )
                }
                val optionStr =
                    "${option.speedFactor},${option.staminaFactor},${option.powerFactor},${option.gutsFactor},${option.wisdomFactor},${option.skillPtFactor},${option.hpFactor},${option.motivationFactor}"
                println("$index,\"$optionStr\",,${Evaluator(summary).toSummaryString()}")
            }
        }.forEach {
            it.join()
        }
    }
}

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
    doSimulation(chara, defaultSupport, target, 55, testCount, {
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

fun dataCheck() {
    val total = 10000
    Store.supportList.filter { it.rarity >= 2 && it.talent == 4 }.forEach { card ->
        if (card.type != StatusType.FRIEND) {
            val support = Support(0, card)
            val specialtyCount = (0 until total)
                .map { support.selectTraining() }
                .count { it == card.type }
            val specialtyRate = specialtyCount.toDouble() / total
            val specialtyStr = "${card.status.specialtyRate}/${card.unique.specialtyRate} $specialtyRate"

            val hintCount = (0 until total)
                .map { support.checkHint() }
                .count { it }
            val hintRate = hintCount.toDouble() / total
            val hintStr = "${card.status.hintFrequency}/${card.unique.hintFrequency} $hintRate"

            println("${card.name} $specialtyStr $hintStr")
        }
    }
    Store.getTrainingList(Scenario.URA).forEach { println(it) }
    Store.getTrainingList(Scenario.AOHARU).forEach { println(it) }
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

fun calcExpected() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック" to 4,
        "[必殺！Wキャロットパンチ！]ビコーペガサス" to 4,
        "[はやい！うまい！はやい！]サクラバクシンオー" to 4,
        "[『愛してもらうんだぞ』]オグリキャップ" to 4,
        "[押して忍べど燃ゆるもの]ヤエノムテキ" to 4,
        "[ようこそ、トレセン学園へ！]駿川たづな" to 4,
    ).mapIndexed { index, supportCard -> Support(index, supportCard).apply { friendTrainingEnabled = true } }
    println(chara)
    support.forEach { println(it) }
    val result =
        Calculator.calcExpectedTrainingStatus(chara, Store.getTraining(scenario, StatusType.SPEED), 5, 2, support)
    println(result.first)
    result.second.forEach { println("${(it.first * 10000).roundToInt() / 100.0}% : ${it.second}") }
    println(result.second.sumOf { it.first })
}

fun checkNewSimulator() {
    val chara = Store.getChara("ハルウララ", 5, 5)
    val support = Store.getSupportByName(
        "[迫る熱に押されて]キタサンブラック" to 4,
        "[必殺！Wキャロットパンチ！]ビコーペガサス" to 4,
        "[はやい！うまい！はやい！]サクラバクシンオー" to 4,
        "[『愛してもらうんだぞ』]オグリキャップ" to 4,
        "[押して忍べど燃ゆるもの]ヤエノムテキ" to 4,
        "[ようこそ、トレセン学園へ！]駿川たづな" to 4,
    )

    val turn = 60
    val testCount = 100000
    val selector = { SimpleActionSelector(StatusType.SPEED) }

    runBlocking {
        launch(context) {
            val summary = mutableListOf<Summary>()
            repeat(testCount) {
                val simulator = Simulator(chara, support, Store.getTrainingList(scenario)).apply {
                    status = status.copy(motivation = 2)
                }
                summary.add(Runner.simulate(turn, simulator, selector()))
//                simulator.history.forEach { println(it) }
//                simulator.history.forEach {
//                    if (it is Action.Training) {
//                        println(it.support.joinToString("/") { "${it.card.name}=${it.friendTraining}" })
//                    }
//                }
            }
            println("old,${Evaluator(summary).toSummaryString()}")
        }.join()
        launch(context) {
            val summary = mutableListOf<io.github.mee1080.umasim.simulation2.Summary>()
            repeat(testCount) {
                val simulator = io.github.mee1080.umasim.simulation2.Simulator(Scenario.URA, chara, support).apply {
                    state = state.copy(
                        status = state.status.copy(motivation = 2)
                    )
                }
                summary.add(io.github.mee1080.umasim.simulation2.Runner.simulate(turn, simulator, selector()))
//                simulator.history.forEach { println(it.first) }
//                simulator.history.forEach {
//                    val action = it.first
//                    if (action is io.github.mee1080.umasim.simulation2.Action.Training) {
//                        println(action.member.joinToString("/") { "${it.name}=${it.isFriendTraining(action.type)} ${it.supportState?.relation}" })
//                    }
//                }
            }
            println("new,${io.github.mee1080.umasim.simulation2.Evaluator(summary).toSummaryString()}")
        }.join()
    }
}