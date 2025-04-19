package io.github.mee1080.umasim.store

import io.github.mee1080.umasim.race.calc2.NOT_SELECTED
import io.github.mee1080.umasim.race.calc2.UmaStatus
import io.github.mee1080.umasim.race.data.FitRank
import io.github.mee1080.umasim.race.data2.SkillData
import io.github.mee1080.umasim.race.data2.findSkills
import io.github.mee1080.umasim.race.data2.skillData2

object ImportExportConverter {

    private val charaSet = skillData2.mapNotNull { it.holder }.distinct().toSet()

    private val fitRankMap = FitRank.entries.associateBy { it.name }

    fun exportChara(chara: UmaStatus): String {
        return buildList {
            with(chara) {
                add(charaName)
                add(speed)
                add(stamina)
                add(power)
                add(guts)
                add(wisdom)
                add(surfaceFit)
                add(distanceFit)
                add(styleFit)
                hasSkills.forEach {
                    add(it.name)
                }
            }
        }.joinToString(",")
    }

    fun importChara(data: String): UmaStatus {
        var charaName: String? = null
        val statusValues = mutableListOf<Int>()
        val skills = mutableListOf<SkillData>()
        val fitRanks = mutableListOf<FitRank>()
        data.split("[\\n\\t,:：　/]+".toRegex()).forEach {
            if (it.isEmpty()) return@forEach
            val value = it.toIntOrNull()
            if (value != null) {
                statusValues += value
                return@forEach
            }
            val fitRank = fitRankMap[it]
            if (fitRank != null) {
                fitRanks += fitRank
                return@forEach
            }
            if (charaSet.contains(it)) {
                charaName = it
                return@forEach
            }
            val skill = findSkills(it)
            if (skill != null) {
                if (charaName != null && skill.size >= 2 && skill[0].holder != null && skill[0].holder != charaName) {
                    skills += skill[1]
                } else {
                    skills += skill[0]
                    if (charaName == null) {
                        charaName = skill[0].holder
                    }
                }
                return@forEach
            }
        }
        return UmaStatus(
            charaName = charaName ?: NOT_SELECTED,
            speed = statusValues.getOrElse(0) { 1000 },
            stamina = statusValues.getOrElse(1) { 1000 },
            power = statusValues.getOrElse(2) { 1000 },
            guts = statusValues.getOrElse(3) { 1000 },
            wisdom = statusValues.getOrElse(4) { 1000 },
            surfaceFit = fitRanks.getOrElse(0) { FitRank.A },
            distanceFit = fitRanks.getOrElse(1) { FitRank.S },
            styleFit = fitRanks.getOrElse(2) { FitRank.A },
            hasSkills = skills
        )
    }
}