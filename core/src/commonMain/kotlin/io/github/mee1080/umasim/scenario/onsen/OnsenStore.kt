package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType

data class Gensen(
    val name: String,
    val turns: Int,
    val strata: Map<StratumType, Int>,
    val immediateEffect: Status,
    val continuousEffect: GensenContinuousEffect
)

data class GensenContinuousEffect(
    val trainingEffect: Int = 0,
    val failureRateDown: Int = 0,
    val friendshipBonus: Map<StatusType, Int> = emptyMap(),
    val hintRateUp: Int = 0,
    val statBonusOnRaceWin: Int = 0,
    val staminaConsumptionDown: Int = 0,
    val extraSupportInTraining: Boolean = false
)

val gensenData = mapOf(
    "ゆこまの湯" to Gensen(
        name = "ゆこまの湯",
        turns = 3,
        strata = emptyMap(),
        immediateEffect = Status(hp = 35), // and other effects
        continuousEffect = GensenContinuousEffect(trainingEffect = 10, failureRateDown = 20)
    ),
    "疾駆の湯" to Gensen(
        name = "疾駆の湯",
        turns = 3,
        strata = mapOf(StratumType.SAND to 400),
        immediateEffect = Status(hp = 5),
        continuousEffect = GensenContinuousEffect(
            friendshipBonus = mapOf(
                StatusType.SPEED to 15,
                StatusType.POWER to 20
            )
        )
    ),
    "堅忍の湯" to Gensen(
        name = "堅忍の湯",
        turns = 3,
        strata = mapOf(StratumType.SOIL to 400),
        immediateEffect = Status(hp = 5),
        continuousEffect = GensenContinuousEffect(
            friendshipBonus = mapOf(
                StatusType.STAMINA to 20,
                StatusType.GUTS to 20
            )
        )
    ),
    "明晰の湯" to Gensen(
        name = "明晰の湯",
        turns = 3,
        strata = mapOf(StratumType.ROCK to 400),
        immediateEffect = Status(hp = 5),
        continuousEffect = GensenContinuousEffect(
            friendshipBonus = mapOf(StatusType.WISDOM to 20),
            statBonusOnRaceWin = 30
        )
    ),
    "駿閃の古湯" to Gensen(
        name = "駿閃の古湯",
        turns = 24,
        strata = mapOf(StratumType.SAND to 300, StratumType.SOIL to 150),
        immediateEffect = Status(hp = 15),
        continuousEffect = GensenContinuousEffect(friendshipBonus = mapOf(StatusType.SPEED to 25), hintRateUp = 100)
    ),
    "剛脚の古湯" to Gensen(
        name = "剛脚の古湯",
        turns = 24,
        strata = mapOf(StratumType.SAND to 300, StratumType.ROCK to 150),
        immediateEffect = Status(hp = 15),
        continuousEffect = GensenContinuousEffect(
            friendshipBonus = mapOf(
                StatusType.POWER to 40,
                StatusType.GUTS to 40
            )
        )
    ),
    "健壮の古湯" to Gensen(
        name = "健壮の古湯",
        turns = 24,
        strata = mapOf(StratumType.SOIL to 180, StratumType.ROCK to 270),
        immediateEffect = Status(hp = 15),
        continuousEffect = GensenContinuousEffect(
            friendshipBonus = mapOf(StatusType.STAMINA to 40),
            staminaConsumptionDown = 10
        )
    ),
    "天翔の古湯" to Gensen(
        name = "天翔の古湯",
        turns = 24,
        strata = mapOf(StratumType.SOIL to 270, StratumType.ROCK to 180),
        immediateEffect = Status(hp = 15),
        continuousEffect = GensenContinuousEffect(
            friendshipBonus = mapOf(StatusType.WISDOM to 40),
            statBonusOnRaceWin = 60
        )
    ),
    "秘湯ゆこま" to Gensen(
        name = "秘湯ゆこま",
        turns = 48,
        strata = mapOf(StratumType.SAND to 180, StratumType.SOIL to 180, StratumType.ROCK to 180),
        immediateEffect = Status(hp = 30),
        continuousEffect = GensenContinuousEffect(
            friendshipBonus = mapOf(
                StatusType.SPEED to 45,
                StatusType.STAMINA to 60,
                StatusType.POWER to 60,
                StatusType.GUTS to 60,
                StatusType.WISDOM to 60
            ), extraSupportInTraining = true
        )
    ),
    "伝説の秘湯" to Gensen(
        name = "伝説の秘湯",
        turns = 65,
        strata = mapOf(StratumType.SAND to 90, StratumType.SOIL to 90, StratumType.ROCK to 90),
        immediateEffect = Status(),
        continuousEffect = GensenContinuousEffect(statBonusOnRaceWin = 80)
    )
)

val equipmentLevelBonus = listOf(0, 30, 50, 70, 100, 130)

val statToExcavationPower = listOf(
    // 1st, 2nd, 3rd
    listOf(5, 3, 0),     // G
    listOf(8, 5, 3),     // F
    listOf(12, 8, 4),    // E
    listOf(16, 11, 6),   // D
    listOf(20, 14, 8),   // C
    listOf(25, 18, 10),  // B-A
    listOf(30, 22, 12),  // S-SS
    listOf(35, 25, 15)   // UG+
)

val stratumToBaseStats = mapOf(
    StratumType.SAND to listOf(StatusType.SPEED, StatusType.WISDOM, StatusType.STAMINA),
    StratumType.SOIL to listOf(StatusType.GUTS, StatusType.SPEED, StatusType.POWER),
    StratumType.ROCK to listOf(StatusType.POWER, StatusType.WISDOM, StatusType.STAMINA)
)

val ryokanRankBonus = listOf(
    null,
    RyokanRankBonus(specialityRate = 40, superRecoveryHintBonus = 1),
    RyokanRankBonus(specialityRate = 70, superRecoveryHintBonus = 1),
    RyokanRankBonus(specialityRate = 100, superRecoveryHintBonus = 1),
    RyokanRankBonus(specialityRate = 120, superRecoveryHintBonus = 2, superRecoveryGuaranteed = true)
)

data class RyokanRankBonus(
    val specialityRate: Int,
    val superRecoveryHintBonus: Int,
    val superRecoveryGuaranteed: Boolean = false
)

val digBonus = mapOf(
    StratumType.SAND to Status(speed = 2, power = 1, wisdom = 2),
    StratumType.SOIL to Status(speed = 2, guts = 1, power = 2),
    StratumType.ROCK to Status(stamina = 1, power = 2, wisdom = 2)
)
