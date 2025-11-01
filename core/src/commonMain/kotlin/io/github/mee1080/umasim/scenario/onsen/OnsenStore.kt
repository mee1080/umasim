package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType

enum class StratumType {
    SAND,
    SOIL,
    ROCK
}

data class Gensen(
    val name: String,
    val turn: Int,
    val strata: List<Pair<StratumType, Int>>,
    val immediateEffectHp: Int,
    val continuousEffect: GensenContinuousEffect,
) {
    val totalProgress = strata.sumOf { it.second }
    override fun hashCode() = name.hashCode()
    override fun equals(other: Any?) = name == (other as? Gensen)?.name
}

data class GensenContinuousEffect(
    val trainingEffect: Int = 0,
    val failureRateDown: Int = 0,
    val friendBonus: Map<StatusType, Int> = emptyMap(),
    val hintRateUp: Int = 0,
    val goalBonus: Int = 0,
    val hpCost: Int = 0,
    val extraSupportInTraining: Boolean = false,
) {
    operator fun plus(other: GensenContinuousEffect): GensenContinuousEffect {
        return GensenContinuousEffect(
            trainingEffect = trainingEffect + other.trainingEffect,
            failureRateDown = failureRateDown + other.failureRateDown,
            friendBonus = buildMap {
                putAll(friendBonus)
                other.friendBonus.forEach {
                    put(it.key, it.value + getOrElse(it.key) { 0 })
                }
            },
            hintRateUp = hintRateUp + other.hintRateUp,
            goalBonus = goalBonus + other.goalBonus,
            hpCost = hpCost + other.hpCost,
            extraSupportInTraining = extraSupportInTraining || other.extraSupportInTraining
        )
    }
}

val gensenData = mapOf(
    "ゆこまの湯" to Gensen(
        name = "ゆこまの湯",
        turn = 3,
        strata = emptyList(),
        immediateEffectHp = 35,
        continuousEffect = GensenContinuousEffect(trainingEffect = 10, failureRateDown = 20)
    ),
    "疾駆の湯" to Gensen(
        name = "疾駆の湯",
        turn = 3,
        strata = listOf(StratumType.SAND to 400),
        immediateEffectHp = 5,
        continuousEffect = GensenContinuousEffect(
            friendBonus = mapOf(
                StatusType.SPEED to 15,
                StatusType.POWER to 20
            )
        )
    ),
    "堅忍の湯" to Gensen(
        name = "堅忍の湯",
        turn = 3,
        strata = listOf(StratumType.SOIL to 400),
        immediateEffectHp = 5,
        continuousEffect = GensenContinuousEffect(
            friendBonus = mapOf(
                StatusType.STAMINA to 20,
                StatusType.GUTS to 20
            )
        )
    ),
    "明晰の湯" to Gensen(
        name = "明晰の湯",
        turn = 3,
        strata = listOf(StratumType.ROCK to 400),
        immediateEffectHp = 5,
        continuousEffect = GensenContinuousEffect(
            friendBonus = mapOf(StatusType.WISDOM to 20),
            goalBonus = 30
        )
    ),
    "駿閃の古湯" to Gensen(
        name = "駿閃の古湯",
        turn = 24,
        strata = listOf(StratumType.SAND to 300, StratumType.SOIL to 150),
        immediateEffectHp = 15,
        continuousEffect = GensenContinuousEffect(friendBonus = mapOf(StatusType.SPEED to 25), hintRateUp = 100)
    ),
    "剛脚の古湯" to Gensen(
        name = "剛脚の古湯",
        turn = 24,
        strata = listOf(StratumType.SAND to 300, StratumType.ROCK to 150),
        immediateEffectHp = 15,
        continuousEffect = GensenContinuousEffect(
            friendBonus = mapOf(
                StatusType.POWER to 40,
                StatusType.GUTS to 40
            )
        )
    ),
    "健壮の古湯" to Gensen(
        name = "健壮の古湯",
        turn = 24,
        strata = listOf(StratumType.SOIL to 180, StratumType.ROCK to 270),
        immediateEffectHp = 15,
        continuousEffect = GensenContinuousEffect(
            friendBonus = mapOf(StatusType.STAMINA to 40),
            hpCost = 10
        )
    ),
    "天翔の古湯" to Gensen(
        name = "天翔の古湯",
        turn = 24,
        strata = listOf(StratumType.SOIL to 270, StratumType.ROCK to 180),
        immediateEffectHp = 15,
        continuousEffect = GensenContinuousEffect(
            friendBonus = mapOf(StatusType.WISDOM to 40),
            goalBonus = 60
        )
    ),
    "秘湯ゆこま" to Gensen(
        name = "秘湯ゆこま",
        turn = 48,
        strata = listOf(StratumType.SAND to 180, StratumType.SOIL to 180, StratumType.ROCK to 180),
        immediateEffectHp = 30,
        continuousEffect = GensenContinuousEffect(
            friendBonus = mapOf(
                StatusType.SPEED to 45,
                StatusType.STAMINA to 60,
                StatusType.POWER to 60,
                StatusType.GUTS to 60,
                StatusType.WISDOM to 60
            ),
            extraSupportInTraining = true
        )
    ),
    "伝説の秘湯" to Gensen(
        name = "伝説の秘湯",
        turn = 65,
        strata = listOf(StratumType.SAND to 90, StratumType.SOIL to 90, StratumType.ROCK to 90),
        immediateEffectHp = 0,
        continuousEffect = GensenContinuousEffect(goalBonus = 80)
    )
)

val equipmentLevelBonus = listOf(0, 30, 50, 70, 100, 130)

val statusToDigPower = listOf(
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

val factorToDigPower = listOf(10, 6, 3)

val stratumToStatus = mapOf(
    StratumType.SAND to listOf(StatusType.SPEED, StatusType.WISDOM, StatusType.STAMINA),
    StratumType.SOIL to listOf(StatusType.GUTS, StatusType.SPEED, StatusType.POWER),
    StratumType.ROCK to listOf(StatusType.POWER, StatusType.WISDOM, StatusType.STAMINA)
)

val ryokanRankBonus = listOf(
    RyokanRankBonus(specialityRate = 0, superRecoveryHintBonus = 0),
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
    StratumType.SAND to Status(speed = 2, stamina = 1, wisdom = 2, hp = -3),
    StratumType.SOIL to Status(speed = 2, power = 1, guts = 2, hp = -3),
    StratumType.ROCK to Status(stamina = 1, power = 2, wisdom = 2, hp = -3)
)

val onsenTicketOnDig = listOf(0, 1, 1, 2)
