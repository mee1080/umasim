package io.github.mee1080.umasim.cui

import io.github.mee1080.umasim.ai.LArcActionSelector
import io.github.mee1080.umasim.data.Scenario
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.Store
import io.github.mee1080.umasim.simulation2.Runner

fun simulateLarc() {
    doSimulation2(
        Scenario.LARC,
        Store.getChara("[うららん一等賞♪]ハルウララ", 5, 5),
        Store.getSupportByName(
            "[大望は飛んでいく]エルコンドルパサー",
            "[The frontier]ジャングルポケット",
//            "[見習い魔女と長い夜]スイープトウショウ",
            "[ハネ退け魔を退け願い込め]スペシャルウィーク",
            "[君と見る泡沫]マンハッタンカフェ",
            "[L'aubeは迫りて]佐岳メイ",
        ).toTypedArray(),
        targetStatus = StatusType.SPEED, rarity = 2..3, talent = 0..4,
//        Store.getSupportByName(*((0..4).map { "[大望は飛んでいく]エルコンドルパサー" to it }.toTypedArray())),
//        Store.getSupportByName("[おセンチ注意報♪]マルゼンスキー" to 4),
        factor = factor(StatusType.STAMINA, 5) + factor(StatusType.WISDOM, 1),
        testCount = 50000,
        option = LArcActionSelector.speed3Stamina1Wisdom1Long,
        evaluateSetting = Runner.lArcLongEvaluateSetting,
    )
}