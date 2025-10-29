package io.github.mee1080.umasim.scenario.onsen

import io.github.mee1080.umasim.data.SupportCard
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

val SimulationState.onsenStatus: OnsenStatus?
    get() = scenarioStatus as? OnsenStatus

fun SimulationState.updateOnsenStatus(update: OnsenStatus.() -> OnsenStatus): SimulationState {
    val onsenStatus = this.onsenStatus ?: return this
    return copy(scenarioStatus = onsenStatus.update())
}

enum class GensenType {
    SHIKKU, // 疾駆
    KENNIN, // 堅忍
    MEISEKI, // 明晰
}

enum class EquipmentType {
    HOLE_DIGGER, // ホールディガー (砂)
    EARTH_DRILL, // アースドリル (土)
    METAL_CROWN, // メタルクラウン (岩)
}

data class Gensen(
    val level: Int = 0,
    val progress: Int = 0,
)

data class OnsenStatus(
    val gensen: Map<GensenType, Gensen> = mapOf(
        GensenType.SHIKKU to Gensen(),
        GensenType.KENNIN to Gensen(),
        GensenType.MEISEKI to Gensen(),
    ),
    val equipmentLevel: Map<EquipmentType, Int> = mapOf(
        EquipmentType.HOLE_DIGGER to 1,
        EquipmentType.EARTH_DRILL to 1,
        EquipmentType.METAL_CROWN to 1,
    ),
    val innRank: Int = 1,
    val bathTickets: Int = 0,
    val isSuperRecoveryConfirmed: Boolean = false,
    val activeGensen: GensenType? = null,
    val sandPower: Int = 0,
    val earthPower: Int = 0,
    val rockPower: Int = 0,
) : ScenarioStatus {

    constructor(support: List<SupportCard>) : this(
        sandPower = support.sumOf {
            when (it.chara) {
                "トウカイテイオー" -> 10
                else -> 0
            }
        },
        earthPower = support.sumOf {
            when (it.chara) {
                "ミホノブルボン" -> 10
                "トランセンド" -> 10
                else -> 0
            }
        },
        rockPower = support.sumOf {
            when (it.chara) {
                "ホッコータルマエ" -> 10
                "ワンダーアキュート" -> 10
                else -> 0
            }
        }
    )

    val excavatedGensenCount: Int by lazy {
        gensen.values.sumOf { it.level }
    }

    fun updateInnRank(): OnsenStatus {
        val newRank = when (excavatedGensenCount) {
            in 1..2 -> 2
            in 3..4 -> 3
            5 -> 4
            6 -> 5
            else -> if (excavatedGensenCount > 6) 6 else 1
        }
        return copy(innRank = newRank)
    }
}
