/*
 * Copyright 2024 mee1080
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
package io.github.mee1080.umasim.scenario.mecha

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.MechaTuningResult
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState
import io.github.mee1080.umasim.simulation2.addStatus
import io.github.mee1080.utility.mapValuesIf
import io.github.mee1080.utility.replaced

fun SimulationState.updateMechaStatus(update: MechaStatus.() -> MechaStatus): SimulationState {
    val mechaStatus = mechaStatus ?: return this
    return copy(scenarioStatus = mechaStatus.update())
}

fun SimulationState.applyMechaOverdrive(): SimulationState {
    val mechaStatus = mechaStatus ?: return this
    return updateMechaStatus { applyOverdrive() }
        .addStatus(Status(hp = mechaStatus.odHpGain, motivation = mechaStatus.odMotivationGain))
}

fun MechaStatus.applyOverdrive() = copy(
    overdriveGauge = overdriveGauge - 3,
    overdrive = true,
)

fun MechaStatus.applyTuning(result: MechaTuningResult) = copy(
    chipLevels = chipLevels.mapValuesIf({ it.key == result.type }) {
        it.replaced(result.index, it[result.index] + 1)
    }
)

fun MechaStatus.resetTuning() = copy(
    chipLevels = chipLevels.mapValues { listOf(0, 0, 0) }
)

fun MechaStatus.updateTurn(turn: Int) = copy(
    gearFactorValue = when {
        turn > 72 -> 3000
        turn > 60 -> 2500
        turn > 48 -> 2000
        turn > 36 -> 1600
        turn > 24 -> 1000
        turn > 12 -> 600
        else -> 300
    },
    overdrive = false,
)

data class MechaStatus(
    val linkEffects: MechaLinkEffect = MechaLinkEffect(),
    val learningLevels: Map<StatusType, Int> = trainingType.associateWith { linkEffects.initialLearningLevel },
    val maxMechaEnergy: Int = 0,
    val chipLevels: Map<MechaChipType, List<Int>> = MechaChipType.entries.associateWith { listOf(0, 0, 0) },
    val gearExists: Map<StatusType, Boolean> = trainingType.associateWith { false },
    val gearFactorValue: Int = 300,
    val overdriveGauge: Int = linkEffects.initialOverdrive,
    val overdrive: Boolean = false,
    val ugeHistory: List<Int> = emptyList(),
) : ScenarioStatus {
    val learningBonus by lazy {
        mapOf(
            StatusType.SPEED to mechaChipLearningBonus[chipLevels[MechaChipType.LEG]!![0]],
            StatusType.STAMINA to mechaChipLearningBonus[chipLevels[MechaChipType.BODY]!![0]],
            StatusType.POWER to mechaChipLearningBonus[chipLevels[MechaChipType.LEG]!![1]],
            StatusType.GUTS to mechaChipLearningBonus[chipLevels[MechaChipType.BODY]!![1]],
            StatusType.WISDOM to mechaChipLearningBonus[chipLevels[MechaChipType.HEAD]!![0]],
        )
    }

    val learningTrainingFactors by lazy { trainingType.associateWith { calcLearningTrainingFactor(learningLevels[it]!!) } }

    fun gearFactor(type: StatusType) = if (gearExists[type] == true) gearFactorValue else 0

    private fun calcLearningTrainingFactor(learningLevel: Int): Int {
        return if (learningLevel <= 1) 0 else if (linkEffects.hasLearningTrainingFactor) {
            900 + 9 * learningLevel
        } else {
            600 + 6 * learningLevel
        }
    }

    val hintFrequency by lazy { mechaChipHintFrequency[chipLevels[MechaChipType.HEAD]!![1]] }

    val specialityRate by lazy { mechaChipSpecialityRate[chipLevels[MechaChipType.HEAD]!![2]] }

    val friendBonus by lazy { mechaChipFriendBonus[chipLevels[MechaChipType.BODY]!![2]] }

    val skillPt by lazy { mechaChipSkillPt[chipLevels[MechaChipType.LEG]!![2]] }

    val totalLearningLevel by lazy { learningLevels.values.sum() }

    val odLevels by lazy { chipLevels.mapValues { it.value.sum() / 3 } }

    val odGearAll by lazy { odLevels[MechaChipType.HEAD]!! >= 1 }

    fun odStatusBonus(type: StatusType) = when (type) {
        StatusType.SPEED -> odSpeedBonus
        StatusType.STAMINA -> odStaminaBonus
        StatusType.POWER -> odPowerBonus
        StatusType.GUTS -> odGutsBonus
        StatusType.WISDOM -> odWisdomBonus
        StatusType.SKILL -> odSkillPtBonus
        else -> 0
    }

    val odSpeedBonus by lazy { calcOdStatusBonus(MechaChipType.LEG) }
    val odStaminaBonus by lazy { calcOdStatusBonus(MechaChipType.BODY) }
    val odPowerBonus by lazy { calcOdStatusBonus(MechaChipType.LEG) }
    val odGutsBonus by lazy { calcOdStatusBonus(MechaChipType.BODY) }
    val odWisdomBonus by lazy { calcOdStatusBonus(MechaChipType.HEAD) }

    val odLearningLevelBonus by lazy { mechaOdLearningLevelBonus[odLevels[MechaChipType.HEAD]!!] }

    val odHpCostDown by lazy { mechaOdHpCostDown[odLevels[MechaChipType.HEAD]!!] }

    val odHintAll by lazy { odLevels[MechaChipType.HEAD]!! >= 5 }

    val allSupportHint get() = overdrive && odHintAll

    val odMemberCountBonus by lazy { mechaOdMemberCountBonus[odLevels[MechaChipType.BODY]!!] }

    val odAddSupport by lazy { odLevels[MechaChipType.BODY]!! >= 5 }

    val odRelationBonus by lazy { mechaOdRelationBonus[odLevels[MechaChipType.LEG]!!] }

    val trainingRelationBonus get() = if (overdrive) odRelationBonus else 0

    val odHpGain by lazy { mechaOdHpGain[odLevels[MechaChipType.LEG]!!] }

    val odMotivationGain by lazy { mechaOdMotivationGain[odLevels[MechaChipType.LEG]!!] }

    val odSkillPtBonus by lazy { if (odLevels[MechaChipType.LEG]!! >= 5) calcOdStatusBonus(150) else 0 }

    private fun calcOdStatusBonus(type: MechaChipType): Int {
        return calcOdStatusBonus(mechaOdStatusBonusDivider[odLevels[type]!!])
    }

    private fun calcOdStatusBonus(divider: Int) = if (divider == 0) 0 else 3 * (totalLearningLevel / divider + 1)
}

data class MechaLinkEffect(
    val initialMechaEnergyCount: Int = 0,
    val mechaGearFrequencyCount: Int = 0,
    val initialOverdriveCount: Int = 0,
    val learningTrainingEffectCount: Int = 0,
    val initialLearningLevelCount: Int = 0,
) {
    constructor(charaNames: List<String>) : this(
        Scenario.MECHA.scenarioLink.associateWith { charaNames.contains(it) },
    )

    constructor(charaExists: Map<String, Boolean>) : this(
        initialMechaEnergyCount = listOf("ビワハヤヒデ", "エアシャカール").count { charaExists[it]!! },
        mechaGearFrequencyCount = listOf("ビワハヤヒデ", "ナリタタイシン", "タニノギムレット").count {
            charaExists[it]!!
        },
        initialOverdriveCount = listOf("ナリタタイシン", "シンボリクリスエス").count { charaExists[it]!! },
        learningTrainingEffectCount = if (charaExists["エアシャカール"]!!) 1 else 0,
        initialLearningLevelCount = listOf("シンボリクリスエス", "タニノギムレット").count { charaExists[it]!! },
    )

    val gearFrequency = mechaGearFrequencyCount * 10 // TODO

    val hasLearningTrainingFactor = learningTrainingEffectCount > 0

    val initialLearningLevel = if (initialLearningLevelCount == 0) 1 else initialLearningLevelCount * 20

    val initialOverdrive = initialOverdriveCount * 3
}

enum class MechaChipType(
    val displayName: String,
    val chipNames: List<String>,
) {
    HEAD("頭部", listOf("賢さ研究", "スキルヒント", "得意率アップ")),
    BODY("胸部", listOf("スタミナ研究", "根性研究", "友情強化")),
    LEG("脚部", listOf("スピード研究", "パワー研究", "スキルPt")),
}
