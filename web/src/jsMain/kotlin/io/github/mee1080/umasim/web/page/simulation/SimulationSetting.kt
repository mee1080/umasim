package io.github.mee1080.umasim.web.page.simulation

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.legend.*
import io.github.mee1080.utility.replaced

val emptyBuff = LegendBuff("指定なし", "", LegendMember.Blue, 0, LegendBuffEffect())

val legendBuffList1 = (listOf(emptyBuff) + legendBuffList.filter { it.rank == 1 }).map { it to it.toSelection() }
val legendBuffList2 = (listOf(emptyBuff) + legendBuffList.filter { it.rank <= 2 }).map { it to it.toSelection() }
val legendBuffList3 = (listOf(emptyBuff) + legendBuffList).map { it to it.toSelection() }

val legendBuffTemplates = mapOf(
    "青導き" to listOf(
        "オーラ", "協力申請", "高潔な矜持", "共に頂へ",
        "怪物チャンスマイル♪", "Dear friend", "慈愛の微笑み", "Off we go",
        "愛し子よ、共に栄光へ", "高潔なる魂", "百折不撓",
    ),
    "緑導き" to listOf(
        "交渉術", "観察眼", "高潔な矜持", "素敵なハーモニー",
        "絆が織りなす光", "雲海蒼天", "集いし理想", "高潔なる魂",
        "百折不撓", "君となら、もっと！", "一緒に輝きましょう！",
    ),
    "赤導き" to listOf(
        "トーク術", "交渉術", "素敵なハーモニー", "極限の集中",
        "絆が奏でるハーモニー", "怪物チャンスマイル♪", "絆が織りなす光", "集いし理想",
        "高潔なる魂", "百折不撓", "飽くなき挑戦心",
    ),
    "人数増加" to listOf(
        null, null, "リズムを合わせて", null,
        "共に切り開く未来", "トレーニングの約束", "ユニゾンパフォーマンス", null,
        null, null, null,
    ),
    "ヒント増加" to listOf(
        null, null, "未来を見据えて", "ヒラメキの連鎖",
        "心眼", "英気を養う", null, null,
        null, null, null,
    ),
).mapValues { (_, list) ->
    list.map { if (it == null) emptyBuff else getLegendBuff(it)!! }.map { it to it.toSelection() }
}

private fun LegendBuff.toSelection(): String {
    if (this == emptyBuff) return name
    val effects = buildList {
        if (effect.friendBonus > 0) add("友情${effect.friendBonus}")
        if (effect.motivationBonus > 0) add("やる気${effect.motivationBonus}")
        if (effect.trainingBonus > 0) add("トレ効果${effect.trainingBonus}")
        if (effect.hintCount > 0) add("ヒント数${effect.hintCount}")
        if (effect.hintFrequency > 0) add("ヒント率${effect.hintFrequency}")
        if (effect.specialtyRate > 0) add("得意率${effect.specialtyRate}")
        if (effect.hpCost > 0) add("体力消費${effect.hpCost}")
        if (effect.relationBonus > 0) add("絆上昇量${effect.relationBonus}")
        if (effect.motivationUp > 0) add("やる気上昇${effect.motivationUp}")
        if (effect.positionRate > 0) add("配置率${effect.positionRate}")
        if (effect.addMember > 0) add("人数追加${effect.addMember}")
        if (effect.forceHint > 0) add("強制ヒント${effect.forceHint}")
        if (effect.relationUp > 0) add("絆上昇${effect.relationUp}")
    }
    return "${member.color}☆$rank $name(${condition?.let { "${it.shortName}: " } ?: ""}${effects.joinToString()})"
}

data class SimulationSetting(
    val factorList: List<Pair<StatusType, Int>> = listOf(
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
        StatusType.SPEED to 3, StatusType.POWER to 3, StatusType.POWER to 3,
    ),
    val legendBuffList: List<Pair<LegendBuff, String>> = List(11) { emptyBuff to emptyBuff.toSelection() },
) {
    fun setFactor(index: Int, type: StatusType, level: Int): SimulationSetting {
        return copy(
            factorList = factorList.replaced(index, type to level)
        )
    }

    fun setLegendBuff(index: Int, buff: Pair<LegendBuff, String>): SimulationSetting {
        return copy(
            legendBuffList = legendBuffList.replaced(index, buff)
        )
    }

    val legendBuffListOutput get() = legendBuffList.map { if (it.first === emptyBuff) null else it.first }
}
