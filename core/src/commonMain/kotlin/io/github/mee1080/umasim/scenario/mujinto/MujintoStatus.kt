package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.trainingType
import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.ScenarioMemberState
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

fun SimulationState.updateMujintoStatus(update: MujintoStatus.() -> MujintoStatus): SimulationState {
    val mujintoStatus = this.mujintoStatus ?: return this
    return copy(scenarioStatus = mujintoStatus.update())
}

data class MujintoFacility(
    val type: StatusType,
    val level: Int,
    val size: Int,
    val cost: Int,
    val statusBonus: Int = 0,
    val specialTurnTrainingEffect: Int = 0,
    val speedTrainingEffect: Int = 0,
    val staminaTrainingEffect: Int = 0,
    val powerTrainingEffect: Int = 0,
    val gutsTrainingEffect: Int = 0,
    val wisdomTrainingEffect: Int = 0
) {
    // According to memo: Lv1-3: 1 mass, Lv4: 2 mass, Lv5: 3 mass. Special is always 3 mass?
    // This might be relevant for UI or complex construction logic, but for now, level is enough.
}

data class MujintoEvaluationBonus(
    val trainingEffect: Int,
)

data class MujintoStatus(
    val facilityLevel: Map<StatusType, Int> = (trainingType + StatusType.NONE).associateWith { 0 },
    val facilityPlan: List<MujintoFacility> = emptyList(),
    val pioneerPoint: Int = 0,
    val mujintoTrainingTicket: Int = 0,
    val evaluationBonus: List<MujintoEvaluationBonus> = emptyList(),
    val nextTurnSpecialtyBuff: Int = 0,
) : ScenarioStatus {
    fun addPioneerPoint(point: Int): MujintoStatus {
        // TODO 施設、チケット獲得
        return copy(pioneerPoint = pioneerPoint + point)
    }
}

// Scenario-specific state for support cards, especially for Tucker Bligh
data class MujintoMemberState(
    val exampleField: Int = 0 // Placeholder for Tucker Bligh specific mechanics
    // TODO: Define fields relevant to Tucker Bligh's unique interactions if any,
    // e.g., tracking her "得意率アップ" buff for other cards.
) : ScenarioMemberState(Scenario.MUJINTO) {
    override fun addRelation(relation: Int): ScenarioMemberState {
        // TODO: Implement if Tucker Bligh or other cards have special relation gain mechanics
        return this
    }
}
