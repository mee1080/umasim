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

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.MechaTuningResult
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState
import io.github.mee1080.utility.mapValuesIf
import io.github.mee1080.utility.replaced

val SimulationState.mechaStatus get() = scenarioStatus as? MechaStatus

fun SimulationState.updateMechaStatus(update: MechaStatus.() -> MechaStatus): SimulationState {
    return copy(scenarioStatus = mechaStatus?.update())
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

data class MechaStatus(
    val learningLevels: Map<StatusType, Int> = trainingType.associateWith { 0 },
    val maxMechaEnergy: Int = 0,
    val chipLevels: Map<MechaChipType, List<Int>> = MechaChipType.entries.associateWith { listOf(0, 0, 0) },
    val linkEffects: MechaLinkEffect = MechaLinkEffect(),
    val overdriveGauge: Int = 0,
    val overdrive: Boolean = false,
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

    val trainingEffects by lazy { trainingType.associateWith { calcTrainingEffect(learningLevels[it]!!) } }

    private fun calcTrainingEffect(learningLevel: Int): Double {
        // TODO
        return 0.0 + learningLevel * 0.7 + linkEffects.learningTrainingEffect * 0.1
    }

    val hintFrequency by lazy { mechaChipHintFrequency[chipLevels[MechaChipType.HEAD]!![1]] }

    val specialityRate by lazy { mechaChipSpecialityRate[chipLevels[MechaChipType.HEAD]!![2]] }

    val friendBonus by lazy { mechaChipFriendBonus[chipLevels[MechaChipType.BODY]!![2]] }

    val skillPt by lazy { mechaChipSkillPt[chipLevels[MechaChipType.LEG]!![2]] }
}

data class MechaLinkEffect(
    val initialMechaEnergy: Int = 0,
    val mechaGearFrequency: Int = 0,
    val initialOverdrive: Int = 0,
    val learningTrainingEffect: Int = 0,
    val initialLearningLevel: Int = 0,
) {
    constructor(charaNames: List<String>) : this(
        Scenario.MECHA.scenarioLink.associateWith { chara -> charaNames.count { it == chara } },
    )

    constructor(charaCounts: Map<String, Int>) : this(
        initialMechaEnergy = listOf("ビワハヤヒデ", "エアシャカール").sumOf { charaCounts[it] ?: 0 },
        mechaGearFrequency = listOf("ビワハヤヒデ", "ナリタタイシン", "タニノギムレット").sumOf {
            charaCounts[it] ?: 0
        },
        initialOverdrive = listOf("ナリタタイシン", "シンボリクリスエス").sumOf { charaCounts[it] ?: 0 },
        learningTrainingEffect = charaCounts["エアシャカール"] ?: 0,
        initialLearningLevel = listOf("シンボリクリスエス", "タニノギムレット").sumOf { charaCounts[it] ?: 0 },
    )
}

enum class MechaChipType(
    val displayName: String,
    val chipNames: List<String>,
) {
    HEAD("頭部", listOf("賢さ研究", "スキルヒント", "得意率アップ")),
    BODY("胸部", listOf("スタミナ研究", "根性研究", "友情強化")),
    LEG("脚部", listOf("スピード研究", "パワー研究", "スキルPt")),
}
