package io.github.mee1080.umasim.scenario.gm

import io.github.mee1080.umasim.data.Status
import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.data.randomSelectPercent
import io.github.mee1080.umasim.data.trainingTypeOrSkill
import io.github.mee1080.umasim.simulation2.ScenarioStatus
import io.github.mee1080.utility.mapValuesIf
import io.github.mee1080.utility.replaced

enum class Founder(val charaName: String, val colorName: String) {
    Red("ダーレーアラビアン", "赤"),
    Blue("ゴドルフィンバルブ", "青"),
    Yellow("バイアリーターク", "黄"),
    ;

    val longName = "${charaName}（${colorName}）"

    companion object {
        fun fromColor(colorName: String) = entries.firstOrNull { it.colorName == colorName }
    }
}

data class GmStatus(
    val knowledgeTable1: List<Knowledge> = emptyList(),
    val knowledgeTable2: List<Knowledge> = emptyList(),
    val knowledgeTable3: List<Knowledge> = emptyList(),
    val waitingWisdom: Founder? = null,
    val activeWisdom: Founder? = null,
    val wisdomLevel: Map<Founder, Int> = Founder.entries.associateWith { 0 },
    val fragmentCount: Map<StatusType, Int> = trainingTypeOrSkill.associateWith { 0 },
) : ScenarioStatus {
    fun toShortString() = buildString {
        append(knowledgeTable1.joinToString { it.toShortString() })
        append(" : ")
        append(knowledgeTable2.joinToString { it.toShortString() })
        append(" : ")
        append(knowledgeTable3.joinToString { it.toShortString() })
        append(" : ")
        append(waitingWisdom ?: activeWisdom)
        append(" : Lv=")
        append(wisdomLevel.map { "${it.key}${it.value}" }.joinToString())
        append(" : Count=")
        append(fragmentCount.map { "${it.key}${it.value}" }.joinToString())
    }

    val trainingLevelUp get() = activeWisdom == Founder.Red

    val hintFrequencyUp get() = activeWisdom == Founder.Blue

    val allFriend get() = activeWisdom == Founder.Yellow

    val knowledgeFragmentCount get() = knowledgeTable1.size

    fun getStatusBonus(type: StatusType): Int {
        return knowledgeTable1.sumOf { it.getStatusBonus(type) } +
                knowledgeTable2.sumOf { it.getStatusBonus(type) } +
                knowledgeTable3.sumOf { it.getStatusBonus(type) }
    }

    fun addKnowledge(knowledge: Knowledge): GmStatus {
        if (waitingWisdom != null) return this
        val newFragmentCount = fragmentCount.replaced(knowledge.type) { it + 1 }
        val newKnowledgeTable1 = knowledgeTable1 + knowledge
        var newKnowledgeTable2 = knowledgeTable2
        var newKnowledgeTable3 = knowledgeTable3
        var newWisdom = waitingWisdom
        if (newKnowledgeTable1.size % 2 == 0) {
            val left1 = newKnowledgeTable1[newKnowledgeTable1.size - 2]
            val right1 = newKnowledgeTable1[newKnowledgeTable1.size - 1]
            newKnowledgeTable2 = knowledgeTable2 + Knowledge(
                left1.founder,
                randomSelectPercent(0.8, left1.type, right1.type),
                if (left1.founder == right1.founder) 2 else 3,
            )
            if (newKnowledgeTable2.size % 2 == 0) {
                val left2 = newKnowledgeTable2[newKnowledgeTable2.size - 2]
                val right2 = newKnowledgeTable2[newKnowledgeTable2.size - 1]
                newKnowledgeTable3 = knowledgeTable3 + Knowledge(
                    left2.founder,
                    randomSelectPercent(0.8, left2.type, right2.type),
                    if (left2.founder == right2.founder) 2 else 3,
                )
                if (newKnowledgeTable3.size == 2) {
                    val left3 = newKnowledgeTable3[0].founder
                    val right3 = newKnowledgeTable3[1].founder
                    newWisdom = if (left3 == right3) left3 else {
                        val leftCount = newKnowledgeTable1.count { it.founder == left3 }
                        val rightCount = newKnowledgeTable1.count { it.founder == right3 }
                        when {
                            leftCount > rightCount -> left3
                            leftCount == rightCount -> randomSelectPercent(0.5, left3, right3)
                            else -> right3
                        }
                    }
                }
            }
        }
        return copy(
            knowledgeTable1 = newKnowledgeTable1,
            knowledgeTable2 = newKnowledgeTable2,
            knowledgeTable3 = newKnowledgeTable3,
            waitingWisdom = newWisdom,
            fragmentCount = newFragmentCount,
        )
    }

    fun activateWisdom(): Pair<GmStatus, Status> {
        val newStatus = if (waitingWisdom == null) this else copy(
            waitingWisdom = null,
            activeWisdom = waitingWisdom,
            wisdomLevel = wisdomLevel.mapValuesIf({ it.key == waitingWisdom && it.value < 5 }) { it + 1 },
        )
        val effect = if (waitingWisdom == Founder.Red) {
            Status(hp = 50, motivation = 3)
        } else Status()
        return newStatus to effect
    }

    fun turnChange(): GmStatus {
        return if (activeWisdom == null) this else copy(
            knowledgeTable1 = emptyList(),
            knowledgeTable2 = emptyList(),
            knowledgeTable3 = emptyList(),
            activeWisdom = null,
        )
    }

    val wisdomTrainingFactor
        get() = wisdomLevel
            .map { gmWisdomLevelEffect[it.key]!![it.value] }
            .sumOf { it.trainingFactor }

    val wisdomHpCost get() = gmWisdomLevelEffect[Founder.Red]!![wisdomLevel[Founder.Red]!!].hpCost

    val wisdomHintFrequency get() = gmWisdomLevelEffect[Founder.Blue]!![wisdomLevel[Founder.Blue]!!].hintFrequency

    val wisdomTrainingEventFrequency get() = gmWisdomLevelEffect[Founder.Blue]!![wisdomLevel[Founder.Blue]!!].trainingEventFrequency

    val wisdomSupportEventEffect get() = gmWisdomLevelEffect[Founder.Yellow]!![wisdomLevel[Founder.Yellow]!!].supportEventEffect

    val wisdomSupportEventFrequency get() = gmWisdomLevelEffect[Founder.Yellow]!![wisdomLevel[Founder.Yellow]!!].supportEventFrequency
}

data class Knowledge(
    val founder: Founder,
    val type: StatusType,
    val bonus: Int = 1,
) {
    fun getStatusBonus(targetType: StatusType) = if (targetType == type) bonus else 0

    fun toShortString() = "$founder/$type/$bonus"
}

data class WisdomLevelEffect(
    val trainingFactor: Int = 0,
    val hpCost: Int = 0,
    val hintFrequency: Int = 0,
    val trainingEventFrequency: Int = 0,
    val supportEventEffect: Int = 0,
    val supportEventFrequency: Int = 0,
)