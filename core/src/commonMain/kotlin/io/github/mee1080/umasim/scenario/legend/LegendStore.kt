/*
 * Copyright 2025 mee1080
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
package io.github.mee1080.umasim.scenario.legend

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase

internal val legendTrainingData = listOf(
    TrainingBase(StatusType.SPEED, 1, 520, Status(11, 0, 2, 0, 0, 7, -20)),
    TrainingBase(StatusType.SPEED, 2, 524, Status(12, 0, 2, 0, 0, 7, -21)),
    TrainingBase(StatusType.SPEED, 3, 528, Status(13, 0, 2, 0, 0, 7, -22)),
    TrainingBase(StatusType.SPEED, 4, 532, Status(14, 0, 3, 0, 0, 7, -24)),
    TrainingBase(StatusType.SPEED, 5, 536, Status(15, 0, 4, 0, 0, 7, -26)),
    TrainingBase(StatusType.STAMINA, 1, 507, Status(0, 8, 0, 6, 0, 7, -21)),
    TrainingBase(StatusType.STAMINA, 2, 511, Status(0, 9, 0, 6, 0, 7, -22)),
    TrainingBase(StatusType.STAMINA, 3, 515, Status(0, 10, 0, 6, 0, 7, -23)),
    TrainingBase(StatusType.STAMINA, 4, 519, Status(0, 11, 0, 7, 0, 7, -25)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(0, 12, 0, 8, 0, 7, -27)),
    TrainingBase(StatusType.POWER, 1, 516, Status(0, 4, 10, 0, 0, 7, -21)),
    TrainingBase(StatusType.POWER, 2, 520, Status(0, 4, 11, 0, 0, 7, -22)),
    TrainingBase(StatusType.POWER, 3, 524, Status(0, 4, 12, 0, 0, 7, -23)),
    TrainingBase(StatusType.POWER, 4, 528, Status(0, 5, 13, 0, 0, 7, -25)),
    TrainingBase(StatusType.POWER, 5, 532, Status(0, 6, 14, 0, 0, 7, -27)),
    TrainingBase(StatusType.GUTS, 1, 532, Status(2, 0, 2, 10, 0, 7, -21)),
    TrainingBase(StatusType.GUTS, 2, 536, Status(2, 0, 2, 11, 0, 7, -22)),
    TrainingBase(StatusType.GUTS, 3, 540, Status(2, 0, 2, 12, 0, 7, -23)),
    TrainingBase(StatusType.GUTS, 4, 544, Status(3, 0, 2, 13, 0, 7, -25)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(3, 0, 3, 14, 0, 7, -27)),
    TrainingBase(StatusType.WISDOM, 1, 320, Status(3, 0, 0, 0, 7, 5, 5)),
    TrainingBase(StatusType.WISDOM, 2, 321, Status(3, 0, 0, 0, 8, 5, 5)),
    TrainingBase(StatusType.WISDOM, 3, 322, Status(3, 0, 0, 0, 9, 5, 5)),
    TrainingBase(StatusType.WISDOM, 4, 323, Status(4, 0, 0, 0, 10, 5, 5)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(5, 0, 0, 0, 11, 5, 5)),
)

private val legendBuffList = listOf(
    LegendBuff("オーラ", "得意率アップ 30", LegendMember.Blue, 1, LegendBuffEffect(specialtyRate = 30)),
    LegendBuff("学びの姿勢", "ヒント発生率アップ 80%", LegendMember.Blue, 1, LegendBuffEffect(hintFrequency = 80)),
    LegendBuff("社交術", "絆ゲージ上昇量アップ 2", LegendMember.Blue, 1, LegendBuffEffect(relationBonus = 2)),
    LegendBuff("最初の一歩", "やる気効果アップ 15%", LegendMember.Blue, 1, LegendBuffEffect(motivationBonus = 15)),
    LegendBuff("強者の求心力", "得意率アップ 60", LegendMember.Blue, 2, LegendBuffEffect(specialtyRate = 60)),
    LegendBuff("たゆまぬ鍛錬", "やる気効果アップ 30%", LegendMember.Blue, 2, LegendBuffEffect(motivationBonus = 30)),
    LegendBuff("時には苛烈に", "トレーニング効果アップ 5%", LegendMember.Blue, 2, LegendBuffEffect(trainingBonus = 5)),
    LegendBuff(
        "共に頂へ",
        "やる気効果アップ 15%。一緒にトレーニングするサポートの人数×8%さらにアップ",
        LegendMember.Blue,
        2,
        LegendBuffEffect(motivationBonus = 15),
        effectByMemberCount = LegendBuffEffect(motivationBonus = 8),
    ),
    LegendBuff(
        "高潔な矜持",
        "やる気が絶好調以上の時、やる気効果アップ 50%",
        LegendMember.Blue,
        2,
        LegendBuffEffect(motivationBonus = 50),
        condition = LegendBuffCondition.Motivation,
    ),
    LegendBuff(
        "極限の集中",
        "やる気が絶好調以上の時、ヒント発生率アップ 200%",
        LegendMember.Blue,
        2,
        LegendBuffEffect(hintFrequency = 200),
        condition = LegendBuffCondition.Motivation,
    ),
    LegendBuff(
        "Dear friend",
        "友情トレーニング成功時、やる気+1 3ターン後、再度使用可能",
        LegendMember.Blue,
        3,
        LegendBuffEffect(motivationUp = 1),
        condition = LegendBuffCondition.AfterFriendTraining,
    ),
    LegendBuff(
        "慈愛の微笑み",
        "休憩時、やる気+1次のトレーニングの友情ボーナス 60%",
        LegendMember.Blue,
        3,
        LegendBuffEffect(motivationUp = 1, friendBonus = 60),
        condition = LegendBuffCondition.AfterRest,
    ),

    LegendBuff(
        "心眼",
        "やる気が絶好調以上の時、いずれか1人のウマ娘のヒントイベントが必ず発生する",
        LegendMember.Blue,
        3,
        LegendBuffEffect(forceHint = 1),
        condition = LegendBuffCondition.Motivation,
    ),
    LegendBuff(
        "英気を養う",
        "休憩時、次のトレーニングのトレーニング効果アップ 60%また、ヒントイベントで獲得するヒント+1",
        LegendMember.Blue,
        3,
        LegendBuffEffect(trainingBonus = 60, hintLevel = 1),
        condition = LegendBuffCondition.AfterRest,
    ),

    LegendBuff(
        "愛し子よ、共に栄光へ",
        "やる気が絶好調以上の時、友情ボーナス 15%（やる気が超絶好調の時は20%さらにアップ）",
        LegendMember.Blue,
        3,
        LegendBuffEffect(friendBonus = 15),
        effectBySpecialState = LegendBuffEffect(friendBonus = 35),
        condition = LegendBuffCondition.Motivation,
    ),
    LegendBuff(
        "Off we go",
        "休憩時、次のトレーニングのやる気効果アップ 200%また、体力消費量ダウン 100%",
        LegendMember.Blue,
        3,
        LegendBuffEffect(motivationBonus = 200, hpCost = 100),
        condition = LegendBuffCondition.AfterRest,
    ),

    LegendBuff(
        "高潔なる魂",
        "やる気が絶好調以上の時、やる気効果アップ 120%",
        LegendMember.Blue,
        3,
        LegendBuffEffect(motivationBonus = 120),
        condition = LegendBuffCondition.Motivation,
    ),
    LegendBuff(
        "歴史に名を残す覚悟",
        "やる気が絶好調以上の時、得意率アップ 100",
        LegendMember.Blue,
        3,
        LegendBuffEffect(specialtyRate = 100),
        condition = LegendBuffCondition.Motivation,
    ),
    LegendBuff(
        "絆が織りなす光",
        "やる気が絶好調以上の時、友情ボーナス 25%",
        LegendMember.Blue,
        3,
        LegendBuffEffect(friendBonus = 25),
        condition = LegendBuffCondition.Motivation,
    ),
    LegendBuff("協力申請", "得意率アップ 30", LegendMember.Green, 1, LegendBuffEffect(specialtyRate = 30)),
    LegendBuff("観察眼", "ヒント発生率アップ 80%", LegendMember.Green, 1, LegendBuffEffect(hintFrequency = 80)),
    LegendBuff("交渉術", "絆ゲージ上昇量アップ 2", LegendMember.Green, 1, LegendBuffEffect(relationBonus = 2)),
    LegendBuff(
        "リーダーシップ",
        "トレーニング効果アップ 3%",
        LegendMember.Green,
        1,
        LegendBuffEffect(trainingBonus = 3),
    ),
    LegendBuff("気配り上手", "得意率アップ 60", LegendMember.Green, 2, LegendBuffEffect(specialtyRate = 60)),
    LegendBuff(
        "奮励努力",
        "トレーニング効果アップ 3%やる気効果アップ 15%",
        LegendMember.Green,
        2,
        LegendBuffEffect(trainingBonus = 3, motivationBonus = 15),
    ),

    LegendBuff(
        "手法の改善提案",
        "トレーニング効果アップ 5%",
        LegendMember.Green,
        2,
        LegendBuffEffect(trainingBonus = 5),
    ),
    LegendBuff(
        "切磋琢磨",
        "トレーニング効果アップ 2%一緒にトレーニングするサポートの人数×2%さらにアップ",
        LegendMember.Green,
        2,
        LegendBuffEffect(trainingBonus = 2),
        effectByMemberCount = LegendBuffEffect(trainingBonus = 2),
    ),
    LegendBuff(
        "衰えぬ情熱",
        "トレーニング成功時、次のトレーニングのトレーニング効果アップ 7%",
        LegendMember.Green,
        2,
        LegendBuffEffect(trainingBonus = 7),
        condition = LegendBuffCondition.AfterTraining,
    ),
    LegendBuff(
        "未来を見据えて",
        "友情トレーニング成功時、次のトレーニングのヒントイベントで獲得するヒント+1",
        LegendMember.Green,
        2,
        LegendBuffEffect(hintLevel = 1),
        condition = LegendBuffCondition.AfterFriendTraining,
    ),
    LegendBuff(
        "磨励自彊",
        "トレーニング成功時、次のトレーニングの体力消費量ダウン 15%",
        LegendMember.Green,
        3,
        LegendBuffEffect(hpCost = 15),
        condition = LegendBuffCondition.AfterTraining,
    ),
    LegendBuff("君となら、もっと！", "友情ボーナス 22%", LegendMember.Green, 3, LegendBuffEffect(friendBonus = 22)),
    LegendBuff(
        "飽くなき挑戦心",
        "トレーニング成功時、次のトレーニングのトレーニング効果アップ 25%",
        LegendMember.Green,
        3,
        LegendBuffEffect(trainingBonus = 25),
        condition = LegendBuffCondition.AfterTraining,
    ),
    LegendBuff("日進月歩", "トレーニング効果アップ 20%", LegendMember.Green, 3, LegendBuffEffect(trainingBonus = 20)),
    LegendBuff(
        "雲海蒼天",
        "トレーニング成功時、次のトレーニングのトレーニング効果アップ 15%（挑戦ゾーン中は15%さらにアップ）",
        LegendMember.Green,
        3,
        LegendBuffEffect(trainingBonus = 15),
        effectBySpecialState = LegendBuffEffect(trainingBonus = 30),
        condition = LegendBuffCondition.AfterTraining,
    ),
    LegendBuff(
        "共に切り開く未来",
        "トレーニング成功時、次にトレーニングを行うまで追加で3人トレーニングに現れる。2ターン後、再度使用可能",
        LegendMember.Green,
        3,
        LegendBuffEffect(addMember = 3),
        condition = LegendBuffCondition.AfterTraining,
    ),
    LegendBuff(
        "百折不撓",
        "トレーニング成功時、次のトレーニングのやる気効果アップ 120%",
        LegendMember.Green,
        3,
        LegendBuffEffect(motivationBonus = 120),
        condition = LegendBuffCondition.AfterTraining,
    ),
    LegendBuff(
        "革命の青写真",
        "トレーニング成功時、次にトレーニングを行うまでヒント発生率アップ 250%",
        LegendMember.Green,
        3,
        LegendBuffEffect(hintFrequency = 250),
        condition = LegendBuffCondition.AfterTraining,
    ),
    LegendBuff(
        "集いし理想",
        "トレーニング成功時、次のトレーニングの友情ボーナス 25%",
        LegendMember.Green,
        3,
        LegendBuffEffect(friendBonus = 25),
        condition = LegendBuffCondition.AfterTraining,
    ),
    LegendBuff("アピール上手", "得意率アップ 30", LegendMember.Red, 1, LegendBuffEffect(specialtyRate = 30)),
    LegendBuff(
        "トレンドチェック",
        "ヒント発生率アップ 80%",
        LegendMember.Red,
        1,
        LegendBuffEffect(hintFrequency = 80),
    ),
    LegendBuff("トーク術", "絆ゲージ上昇量アップ 2", LegendMember.Red, 1, LegendBuffEffect(relationBonus = 2)),
    LegendBuff("アイドルステップ", "友情ボーナス 5%", LegendMember.Red, 1, LegendBuffEffect(friendBonus = 5)),
    LegendBuff("個性を伸ばして", "得意率アップ 60", LegendMember.Red, 2, LegendBuffEffect(specialtyRate = 60)),
    LegendBuff(
        "溢れるバイタリティ",
        "友情ボーナス 3%やる気効果アップ 15%",
        LegendMember.Red,
        2,
        LegendBuffEffect(friendBonus = 3, motivationBonus = 15),
    ),

    LegendBuff(
        "レッスンのコツ",
        "トレーニング効果アップ 5%",
        LegendMember.Red,
        2,
        LegendBuffEffect(trainingBonus = 5),
    ),
    LegendBuff(
        "素敵なハーモニー",
        "友情ボーナス 3%。トレーニング成功時、友情トレーニングが発生しているサポートの人数×3％さらにアップ",
        LegendMember.Red,
        2,
        LegendBuffEffect(friendBonus = 3),
        effectByMemberCount = LegendBuffEffect(friendBonus = 3),
    ),
    LegendBuff(
        "リズムを合わせて",
        "友情トレーニング成功時、次にトレーニングを行うまで追加で1人トレーニングに現れる。2ターン後、再度使用可能",
        LegendMember.Red,
        2,
        LegendBuffEffect(addMember = 1),
        condition = LegendBuffCondition.AfterFriendTraining,
    ),
    LegendBuff(
        "ヒラメキの連鎖",
        "3人以上のサポートと一緒にトレーニング成功時次のトレーニングのヒントイベントで獲得するヒント+1",
        LegendMember.Red,
        2,
        LegendBuffEffect(hintLevel = 1),
        condition = LegendBuffCondition.AfterSupportCount(3),
    ),
    LegendBuff(
        "心繋がるパフォーマンス",
        "3人以上のサポートと一緒にトレーニング成功時絆ゲージ+3。次のトレーニングの友情ボーナス 10%",
        LegendMember.Red,
        3,
        LegendBuffEffect(relationUp = 3, friendBonus = 10),
        condition = LegendBuffCondition.AfterSupportCount(3),
    ),

    LegendBuff(
        "トレーニングの約束",
        "休憩時、次のトレーニングのトレーニング効果アップ 15%また、追加で3人トレーニングに現れる",
        LegendMember.Red,
        3,
        LegendBuffEffect(addMember = 3, trainingBonus = 15),
        condition = LegendBuffCondition.AfterRest,
    ),

    LegendBuff(
        "ユニゾンパフォーマンス",
        "3人以上のサポートと一緒にトレーニング成功時、次にトレーニングを行うまで追加で1人トレーニングに現れる",
        LegendMember.Red,
        3,
        LegendBuffEffect(addMember = 1),
        condition = LegendBuffCondition.AfterSupportCount(3),
    ),
    LegendBuff("一緒に輝きましょう！", "友情ボーナス 22%", LegendMember.Red, 3, LegendBuffEffect(friendBonus = 22)),
    LegendBuff(
        "絆が奏でるハーモニー",
        "トレーニング効果アップ 7%。一緒にトレーニングしてくれるサポートの人数×7%さらにアップ",
        LegendMember.Red,
        3,
        LegendBuffEffect(trainingBonus = 7),
        effectByMemberCount = LegendBuffEffect(trainingBonus = 7),
    ),
    LegendBuff(
        "溢れる魅力",
        "3人以上のサポートと一緒にトレーニング成功時次にトレーニングを行うまでサポートの出現率アップ 25",
        LegendMember.Red,
        3,
        LegendBuffEffect(positionRate = 25),
        effectByMemberCount = LegendBuffEffect(positionRate = 3),
        condition = LegendBuffCondition.AfterSupportCount(3),
    ),
    LegendBuff(
        "怪物チャンスマイル♪",
        "友情トレーニング成功時、次のトレーニングのやる気効果アップ 150%",
        LegendMember.Red,
        3,
        LegendBuffEffect(motivationBonus = 150),
        condition = LegendBuffCondition.AfterFriendTraining,
    ),
    LegendBuff(
        "アピール大成功！",
        "友情トレーニング成功時、次にトレーニングを行うまで得意率アップ 100",
        LegendMember.Red,
        3,
        LegendBuffEffect(specialtyRate = 100),
        condition = LegendBuffCondition.AfterFriendTraining,
    ),
    LegendBuff(
        "国民的アイドルウマ娘",
        "5人以上のサポートと一緒にトレーニング成功時、次のトレーニングの友情ボーナスとトレーニング効果アップ 20%",
        LegendMember.Red,
        3,
        LegendBuffEffect(friendBonus = 20, trainingBonus = 20),
        condition = LegendBuffCondition.AfterSupportCount(5),
    ),
)

val legendBuffData = legendBuffList.groupBy { it.member }.mapValues { (_, value) -> value.groupBy { it.rank } }

fun getLegendBuff(name: String) = legendBuffList.find { it.name == name }

val bestFriendSupportTrainingBonus = listOf(0, 2, 3, 5, 6, 7, 8, 10, 10, 10)

val bestFriendGuestTrainingBonus = listOf(0, 10, 12, 14, 16, 18, 20, 22, 24, 25)

val bestFriendGuestFriendBonus = listOf(0, 15, 16, 17, 18, 19, 20, 20, 20, 20)
