package io.github.mee1080.umasim.web.page.simulation

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.legend.LegendBuff
import io.github.mee1080.umasim.scenario.legend.LegendBuffEffect
import io.github.mee1080.umasim.scenario.legend.LegendMember
import io.github.mee1080.umasim.scenario.legend.legendBuffList
import io.github.mee1080.utility.replaced

val emptyBuff = LegendBuff("指定なし", "", LegendMember.Blue, 0, LegendBuffEffect())

val legendBuffList1 = (listOf(emptyBuff) + legendBuffList.filter { it.rank == 1 }).map { it to it.toSelection() }
val legendBuffList2 = (listOf(emptyBuff) + legendBuffList.filter { it.rank <= 2 }).map { it to it.toSelection() }
val legendBuffList3 = (listOf(emptyBuff) + legendBuffList).map { it to it.toSelection() }

private fun LegendBuff.toSelection(): String {
    if (this == emptyBuff) return name
    val effects = buildList {
        if (effect.friendBonus > 0) add("友情")
        if (effect.motivationBonus > 0) add("やる気")
        if (effect.trainingBonus > 0) add("トレ効果")
        if (effect.hintCount > 0) add("ヒント数")
        if (effect.hintFrequency > 0) add("ヒント率")
        if (effect.specialtyRate > 0) add("得意率")
        if (effect.hpCost > 0) add("体力消費")
        if (effect.relationBonus > 0) add("絆上昇量")
        if (effect.motivationUp > 0) add("やる気上昇")
        if (effect.positionRate > 0) add("配置率")
        if (effect.addMember > 0) add("人数追加")
        if (effect.forceHint > 0) add("強制ヒント")
        if (effect.relationUp > 0) add("絆上昇")
    }
    return "${member.color}☆$rank $name(${condition?.let { "${it.shortName}: " } ?: ""}${effects.joinToString()})"
}

data class SimulationSetting(
    val factorList: List<Pair<StatusType, Int>> = listOf(
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
        StatusType.SPEED to 3, StatusType.SPEED to 3, StatusType.SPEED to 3,
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
