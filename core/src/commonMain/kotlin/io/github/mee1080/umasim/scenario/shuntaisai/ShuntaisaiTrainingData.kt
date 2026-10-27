package io.github.mee1080.umasim.scenario.shuntaisai

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.TrainingBase

val shuntaisaiTrainingData: List<TrainingBase> = listOf(
    TrainingBase(StatusType.SPEED, 1, 520, Status(12, 0, 2, 0, 0, 7, -20)),
    TrainingBase(StatusType.SPEED, 2, 524, Status(13, 0, 2, 0, 0, 7, -21)),
    TrainingBase(StatusType.SPEED, 3, 528, Status(14, 0, 2, 0, 0, 7, -22)),
    TrainingBase(StatusType.SPEED, 4, 532, Status(15, 0, 3, 0, 0, 7, -24)),
    TrainingBase(StatusType.SPEED, 5, 536, Status(16, 0, 3, 0, 0, 9, -25)),
    TrainingBase(StatusType.STAMINA, 1, 507, Status(0, 10, 0, 3, 0, 7, -20)),
    TrainingBase(StatusType.STAMINA, 2, 511, Status(0, 11, 0, 4, 0, 7, -21)),
    TrainingBase(StatusType.STAMINA, 3, 515, Status(0, 12, 0, 4, 0, 7, -22)),
    TrainingBase(StatusType.STAMINA, 4, 519, Status(0, 13, 0, 5, 0, 7, -24)),
    TrainingBase(StatusType.STAMINA, 5, 523, Status(0, 14, 0, 6, 0, 10, -25)),
    TrainingBase(StatusType.POWER, 1, 516, Status(0, 6, 9, 0, 0, 7, -21)),
    TrainingBase(StatusType.POWER, 2, 520, Status(0, 6, 10, 0, 0, 7, -22)),
    TrainingBase(StatusType.POWER, 3, 524, Status(0, 6, 11, 0, 0, 7, -23)),
    TrainingBase(StatusType.POWER, 4, 528, Status(0, 6, 12, 0, 0, 7, -25)),
    TrainingBase(StatusType.POWER, 5, 532, Status(0, 6, 14, 0, 0, 10, -25)),
    TrainingBase(StatusType.GUTS, 1, 532, Status(2, 0, 2, 12, 0, 7, -21)),
    TrainingBase(StatusType.GUTS, 2, 536, Status(2, 0, 2, 13, 0, 7, -22)),
    TrainingBase(StatusType.GUTS, 3, 540, Status(2, 0, 2, 14, 0, 7, -23)),
    TrainingBase(StatusType.GUTS, 4, 544, Status(3, 0, 2, 15, 0, 7, -25)),
    TrainingBase(StatusType.GUTS, 5, 548, Status(3, 0, 2, 15, 0, 10, -25)),
    TrainingBase(StatusType.WISDOM, 1, 320, Status(2, 0, 0, 0, 8, 6, 5)),
    TrainingBase(StatusType.WISDOM, 2, 321, Status(2, 0, 0, 0, 9, 6, 5)),
    TrainingBase(StatusType.WISDOM, 3, 322, Status(2, 0, 0, 0, 10, 6, 5)),
    TrainingBase(StatusType.WISDOM, 4, 323, Status(3, 0, 0, 0, 11, 6, 5)),
    TrainingBase(StatusType.WISDOM, 5, 324, Status(3, 0, 0, 0, 12, 8, 5)),
)

/**
 * 演目稽古の基本上昇量（通常トレーニングLvごと）
 *
 * * トレーニングLv1（ジュニア）は通常トレーニングLv5と同じ
 * * トレーニングLv2以上（クラシック）は、演目Lv3～5で同じ値を確認
 *   * スピードはトレーニングLv3の値のみ確認
 *   * 賢さはトレーニングLv2と3（Lv2は推定）で同じ値
 * * TODO: トレーニングLvではなく時期（ジュニア/クラシック/シニア）で変化している可能性あり、トレーニングLv3以上とシニアは未確認
 * * 体力消費は未確認のため通常トレーニングLv5と同じ
 */
val shuntaisaiPracticeTrainingData: Map<StatusType, List<TrainingBase>> = listOf(
    StatusType.SPEED to listOf(
        Status(16, 0, 3, 0, 0, 9, -25),
        Status(21, 0, 3, 0, 0, 10, -25),
    ),
    StatusType.STAMINA to listOf(
        Status(0, 14, 0, 6, 0, 10, -25),
        Status(0, 20, 0, 8, 0, 12, -25),
    ),
    StatusType.POWER to listOf(
        Status(0, 6, 14, 0, 0, 10, -25),
        Status(0, 8, 20, 0, 0, 12, -25),
    ),
    StatusType.GUTS to listOf(
        Status(3, 0, 2, 15, 0, 10, -25),
        Status(3, 0, 2, 23, 0, 12, -25),
    ),
    StatusType.WISDOM to listOf(
        Status(3, 0, 0, 0, 12, 8, 5),
        Status(4, 0, 0, 0, 14, 9, 5),
    ),
).associate { (type, statusList) ->
    val baseLv5 = shuntaisaiTrainingData.first { it.type == type && it.level == 5 }
    type to statusList.mapIndexed { index, status ->
        TrainingBase(type, index + 1, baseLv5.failureRate, status)
    }
}

/**
 * 演目稽古の基本上昇量
 *
 * @param trainingLevel 通常トレーニングのLv
 * @param performanceLevel 演目Lv（表示上のトレーニングLv）
 */
fun shuntaisaiPracticeTraining(type: StatusType, trainingLevel: Int, performanceLevel: Int): TrainingBase {
    val list = shuntaisaiPracticeTrainingData.getValue(type)
    return list[(trainingLevel - 1).coerceIn(0, list.lastIndex)].copy(level = performanceLevel)
}
