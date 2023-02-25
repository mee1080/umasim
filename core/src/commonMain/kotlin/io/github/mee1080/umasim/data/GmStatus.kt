package io.github.mee1080.umasim.data

import kotlin.random.Random

enum class Founder(val charaName: String, val colorName: String) {
    Red("ダーレーアラビアン", "赤"),
    Blue("ゴドルフィンバルブ", "青"),
    Yellow("バイアリーターク", "黄"),
    ;

    val longName = "${charaName}（${colorName}）"

    companion object {
        fun fromColor(colorName: String) = values().firstOrNull { it.colorName == colorName }
    }
}

data class GmStatus(
    val knowledgeTable1: List<Knowledge> = emptyList(),
    val knowledgeTable2: List<Knowledge> = emptyList(),
    val knowledgeTable3: List<Knowledge> = emptyList(),
    val waitingWisdom: Founder? = null,
    val activeWisdom: Founder? = null,
    val wisdomLevel: Map<Founder, Int> = Founder.values().associateWith { 0 }
) {
    val trainingLevelUp get() = activeWisdom == Founder.Red

    val hintFrequencyUp get() = activeWisdom == Founder.Blue

    val allFriend get() = activeWisdom == Founder.Yellow

    fun getStatusBonus(type: StatusType): Int {
        return knowledgeTable1.sumOf { it.getStatusBonus(type) } +
                knowledgeTable2.sumOf { it.getStatusBonus(type) } +
                knowledgeTable3.sumOf { it.getStatusBonus(type) }
    }

    fun addKnowledge(knowledge: Knowledge): GmStatus {
        // TODO 色と種別の引継ぎ条件調査
        if (waitingWisdom != null) return this
        val newKnowledgeTable1 = knowledgeTable1 + knowledge
        var newKnowledgeTable2 = knowledgeTable2
        var newKnowledgeTable3 = knowledgeTable3
        var newWisdom = waitingWisdom
        if (newKnowledgeTable1.size % 2 == 0) {
            newKnowledgeTable2 = knowledgeTable2 + Knowledge(
                newKnowledgeTable1[newKnowledgeTable1.size - 2].founder,
                newKnowledgeTable1[newKnowledgeTable1.size - Random.nextInt(1, 3)].type,
                2,
            )
            if (newKnowledgeTable2.size % 2 == 0) {
                newKnowledgeTable3 = knowledgeTable3 + Knowledge(
                    newKnowledgeTable2[newKnowledgeTable2.size - 2].founder,
                    newKnowledgeTable2[newKnowledgeTable2.size - Random.nextInt(1, 3)].type,
                    2,
                )
                if (newKnowledgeTable3.size == 2) {
                    newWisdom = newKnowledgeTable3[0].founder
                }
            }
        }
        return copy(
            knowledgeTable1 = newKnowledgeTable1,
            knowledgeTable2 = newKnowledgeTable2,
            knowledgeTable3 = newKnowledgeTable3,
            waitingWisdom = newWisdom,
        )
    }

    fun activateWisdom(): Pair<GmStatus, Status> {
        val newStatus = if (waitingWisdom == null) this else copy(
            waitingWisdom = null,
            activeWisdom = waitingWisdom,
            wisdomLevel = wisdomLevel.mapValues { if (it.key == waitingWisdom) it.value + 1 else it.value },
        )
        val effect = if (waitingWisdom == Founder.Red) {
            Status(hp = 50, motivation = 1)
        } else Status()
        return newStatus to effect
    }

    fun turnChange(): GmStatus {
        return if (activeWisdom == null) this else copy(
            knowledgeTable1 = emptyList(),
            knowledgeTable2 = emptyList(),
            knowledgeTable3 = emptyList(),
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
    val bonus: Int,
) {
    fun getStatusBonus(targetType: StatusType) = if (targetType == type) bonus else 0
}

data class WisdomLevelEffect(
    val trainingFactor: Int = 0,
    val hpCost: Int = 0,
    val hintFrequency: Int = 0,
    val trainingEventFrequency: Int = 0,
    val supportEventEffect: Int = 0,
    val supportEventFrequency: Int = 0,
)