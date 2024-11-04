package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.gm.Founder
import io.github.mee1080.umasim.scenario.gm.GmStatus
import io.github.mee1080.umasim.scenario.gm.Knowledge
import io.github.mee1080.utility.replaced

data class GmState(
    val knowledgeTable: List<Knowledge?> = List(14) { null },
    val wisdom: Founder? = null,
    val wisdomLevel: Map<Founder, Int> = Founder.entries.associateWith { 0 }
) {
    fun toGmStatus() = GmStatus(
        knowledgeTable1 = knowledgeTable.subList(0, 8).filterNotNull(),
        knowledgeTable2 = knowledgeTable.subList(8, 12).filterNotNull(),
        knowledgeTable3 = knowledgeTable.subList(12, 14).filterNotNull(),
        activeWisdom = wisdom,
        wisdomLevel = wisdomLevel,
    )

    fun updateType(index: Int, value: StatusType?): GmState {
        val knowledge = if (value == null) null else {
            val bonus = knowledgeTable[index]?.bonus ?: if (index >= 8) 2 else 1
            Knowledge(Founder.Red, value, bonus)
        }
        return copy(knowledgeTable = knowledgeTable.replaced(index) { knowledge })
    }

    fun updateBonus(index: Int, value: Int): GmState {
        val type = knowledgeTable[index]?.type ?: return this
        val knowledge = Knowledge(Founder.Red, type, value)
        return copy(knowledgeTable = knowledgeTable.replaced(index) { knowledge })
    }

    fun clearKnowledge(): GmState {
        return copy(knowledgeTable = List(14) { null })
    }

    fun updateWisdomLevel(target: Founder, value: Int): GmState {
        val newWisdomLevel = wisdomLevel.mapValues { if (it.key == target) value else it.value }
        return copy(wisdomLevel = newWisdomLevel)
    }
}