package io.github.mee1080.umasim.scenario.mujinto

import io.github.mee1080.umasim.scenario.Scenario
import io.github.mee1080.umasim.simulation2.ScenarioMemberState
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.umasim.simulation2.SimulationState

// Helper function to update MujintoStatus
fun SimulationState.updateMujintoStatus(update: MujintoStatus.() -> MujintoStatus): SimulationState {
    val mujintoStatus = this.mujintoStatus ?: return this
    return copy(scenarioStatus = mujintoStatus.update())
}

// Enum for Facility Types
enum class FacilityType {
    SPEED,
    STAMINA,
    POWER,
    GUTS,
    WISDOM,
    SPECIAL // For special effect facilities
}

// Data class for a single Facility
data class Facility(
    val type: FacilityType,
    var level: Int = 0,
    var constructionProgress: Int = 0 // Could represent accumulated Development Points for this facility
) {
    // According to memo: Lv1-3: 1 mass, Lv4: 2 mass, Lv5: 3 mass. Special is always 3 mass?
    // This might be relevant for UI or complex construction logic, but for now, level is enough.
}

// Main Scenario Status for Mujinto
data class MujintoStatus(
    val facilities: Map<FacilityType, Facility> = FacilityType.entries.associateWith { Facility(it) },
    var developmentPoints: Int = 0,
    var islandTrainingTickets: Int = 0, // "特別な期間を除き1枚のみ所有可能" - max 1 unless special event
    var evaluationMeetingCount: Int = 0, // To track how many evaluation meetings have occurred
    val eventFlags: Set<String> = emptySet(), // For one-time events like ticket awards
    val tuckerSpecialtyBuffNextTurn: Boolean = false, // Flag for Tucker's post-training buff
    val tuckerSpecialtyBuffNextTurnAmount: Int = 0, // Amount for Tucker's outing buff
    // TODO: Add other specific status fields based on mujinto_memo.md as details become clearer
    // e.g., specific buffs from facilities, status of "建設計画" (construction plan order), active training buffs
) : ScenarioStatus {
    // Placeholder for facility effects
    fun getFacilitySpeedBonus(): Int {
        val speedFacility = facilities[FacilityType.SPEED] ?: return 0
        // Example: "スピードLv1: 島トレ・島スピードにスピボ1", "島スピードのトレ効果5%"
        // This needs more detailed logic based on how "島トレ効果" is applied.
        // For now, a simple bonus.
        return when (speedFacility.level) {
            1 -> 5 // Placeholder value
            2 -> 10 // Placeholder value
            3 -> 15 // Placeholder value
            4 -> 20 // Placeholder value
            5 -> 25 // Placeholder value
            else -> 0
        }
    }
    // TODO: Add similar functions for other facility types and effects
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
