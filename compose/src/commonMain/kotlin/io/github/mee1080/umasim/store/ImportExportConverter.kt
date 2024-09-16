package io.github.mee1080.umasim.store

import io.github.mee1080.umasim.race.calc2.NOT_SELECTED
import io.github.mee1080.umasim.race.calc2.UmaStatus
import io.github.mee1080.umasim.race.data.FitRank
import io.github.mee1080.umasim.race.data2.SkillData
import io.github.mee1080.umasim.race.data2.skillData2

object ImportExportConverter {

    private val charaSet = skillData2.mapNotNull { it.holder }.distinct().toSet()

    private val skillMap = skillData2.groupBy { it.name }

    private val fitRankMap = FitRank.entries.associateBy { it.name }

    private const val CANDIDATE_THRESHOLD = 0.3

    private const val DEBUG = false

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
            val skill = findSkill(it)
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

    private fun findSkill(input: String): List<SkillData>? {
        val matched = skillMap[input]
        if (matched != null) return matched
        val candidate = skillMap.keys.minBy { normalizedLevenshteinDistance(it, input) }
        val distance = normalizedLevenshteinDistance(candidate, input)
        if (DEBUG) println("input: $input candidate: $candidate distance: $distance")
        return if (distance > CANDIDATE_THRESHOLD) null else skillMap[candidate]
    }

    private fun normalizedLevenshteinDistance(s1: String, s2: String): Double {
        return levenshteinDistance(s1, s2).toDouble() / maxOf(s1.length, s2.length)
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val len1 = s1.length
        val len2 = s2.length

        val dp = Array(len1 + 1) { IntArray(len2 + 1) }
        for (i in 0..len1) {
            dp[i][0] = i
        }
        for (j in 0..len2) {
            dp[0][j] = j
        }

        for (i in 1..len1) {
            for (j in 1..len2) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,       // 削除
                    dp[i][j - 1] + 1,       // 挿入
                    dp[i - 1][j - 1] + cost // 置換
                )
            }
        }

        return dp[len1][len2]
    }

}