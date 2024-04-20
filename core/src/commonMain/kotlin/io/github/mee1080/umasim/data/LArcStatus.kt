/*
 * Copyright 2023 mee1080
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
package io.github.mee1080.umasim.data

import io.github.mee1080.umasim.simulation2.MemberState
import io.github.mee1080.umasim.simulation2.ScenarioMemberState
import io.github.mee1080.utility.replaceItem
import io.github.mee1080.utility.replaced

private val baseTrainingFactorMap = (0..200).associateWith {
    when {
        it >= 100 -> it / 10 + 20
        it >= 25 -> it / 5 + 10
        it >= 20 -> 13
        it >= 15 -> 10
        it >= 10 -> 8
        it >= 5 -> 5
        else -> 0
    }
}

enum class LArcAptitude(
    val displayName: String,
    private val level2Cost: Int,
    private val level3Cost: Int,
    val getLevel: LArcStatus.() -> Int,
    val addLevel: LArcStatus.() -> LArcStatus,
) {
    OverseasTurfAptitude("海外洋芝適性", 50, 200, { overseasTurfAptitude }, {
        copy(overseasTurfAptitude = overseasTurfAptitude + 1)
    }),
    LongchampAptitude("ロンシャン適性", 50, 200, { longchampAptitude }, {
        copy(longchampAptitude = longchampAptitude + 1)
    }),
    LifeRhythm("生活リズム", 100, 200, { lifeRhythm }, {
        copy(lifeRhythm = lifeRhythm + 1)
    }),
    NutritionManagement("栄養管理", 100, 200, { nutritionManagement }, {
        copy(nutritionManagement = nutritionManagement + 1)
    }),
    FrenchSkill("フランス語力", 100, 200, { frenchSkill }, {
        copy(frenchSkill = frenchSkill + 1)
    }),
    OverseasExpedition("海外遠征", 100, 200, { overseasExpedition }, {
        copy(overseasExpedition = overseasExpedition + 1)
    }),
    StrongHeart("強心臓", 200, 300, { strongHeart }, {
        copy(strongHeart = strongHeart + 1)
    }),
    MentalStrength("精神力", 200, 300, { mentalStrength }, {
        copy(mentalStrength = mentalStrength + 1)
    }),
    HopeOfLArc("L’Arcの希望", 200, 0, { hopeOfLArc }, {
        copy(hopeOfLArc = hopeOfLArc + 1)
    }),
    ConsecutiveVictories("凱旋門賞連覇の夢", 150, 0, { consecutiveVictories }, {
        copy(consecutiveVictories = consecutiveVictories + 1)
    });

    val maxLevel get() = if (level3Cost == 0) 2 else 3

    fun getCost(level: Int) = when (level) {
        2 -> level2Cost
        3 -> level3Cost
        else -> 0
    }

    fun getCost(status: LArcStatus) = getCost(status.getLevel() + 1)
}

data class LArcStatus(
    val supporterPt: Int = 0,
    val memberSupporterPt: Int = 0,
    val aptitudePt: Int = 0,
    val ssMatchCount: Int = 0,
    val totalSSMatchCount: Int = 0,
    val isSSSMatch: Boolean? = null,
    val lastExpectationEvent: Int = 0,

    val overseasTurfAptitude: Int = 0,
    val longchampAptitude: Int = 0,
    val lifeRhythm: Int = 0,
    val nutritionManagement: Int = 0,
    val frenchSkill: Int = 0,
    val overseasExpedition: Int = 0,
    val strongHeart: Int = 0,
    val mentalStrength: Int = 0,
    val hopeOfLArc: Int = 0,
    val consecutiveVictories: Int = 0,

    val ssMatchMember: Set<MemberState> = emptySet(),
) {
    fun toShortString() =
        "LArcStatus(SupporterPt=$supporterPt/$memberSupporterPt, expectationLevel=$expectationLevel aptitudePt=$aptitudePt, ssMatch=$ssMatchCount/$totalSSMatchCount/$isSSSMatch, aptitude=$overseasTurfAptitude/$longchampAptitude/$lifeRhythm/$nutritionManagement/$frenchSkill/$overseasExpedition/$strongHeart/$mentalStrength/$hopeOfLArc/$consecutiveVictories)"

    val expectationLevel get() = (supporterPt + memberSupporterPt) / 1700

    private val baseTrainingFactor =
        (baseTrainingFactorMap[expectationLevel] ?: 0) + (if (hopeOfLArc >= 1) 5 else 0)

    fun getStatusBonus(type: StatusType): Int {
        return when (type) {
            StatusType.SPEED -> if (nutritionManagement >= 1) 3 else 0
            StatusType.STAMINA -> (if (longchampAptitude >= 1) 3 else 0) + (if (strongHeart >= 1) 3 else 0)
            StatusType.POWER -> if (lifeRhythm >= 1) 3 else 0
            StatusType.GUTS -> (if (overseasTurfAptitude >= 1) 3 else 0) + (if (mentalStrength >= 1) 3 else 0)
            StatusType.WISDOM -> if (frenchSkill >= 1) 3 else 0
            StatusType.SKILL -> if (overseasExpedition >= 3) 20 else (if (overseasExpedition >= 1) 10 else 0)
            else -> 0
        }
    }

    fun getTrainingFactor(trainingType: StatusType, overseas: Boolean): Int {
        return baseTrainingFactor + if (overseas) {
            when (trainingType) {
                StatusType.SPEED -> if (nutritionManagement >= 3) 50 else 0
                StatusType.STAMINA -> if (longchampAptitude >= 3) 50 else 0
                StatusType.POWER -> if (lifeRhythm >= 3) 50 else 0
                StatusType.GUTS -> if (overseasTurfAptitude >= 3) 50 else 0
                StatusType.WISDOM -> if (frenchSkill >= 3) 50 else 0
                else -> 0
            }
        } else 0
    }

    val friendFactor get() = if (mentalStrength >= 3) 20 else 0

    fun hpCost(overseas: Boolean) = if (overseas && strongHeart >= 3) 20 else 0

    fun addAptitude(aptitude: LArcAptitude): LArcStatus {
        val cost = aptitude.getCost(this)
        if (cost == 0) return this
        return aptitude.addLevel(this).copy(
            aptitudePt = aptitudePt - cost
        )
    }
}

enum class StarEffect(val displayName: String) {
    Status("ステータス"), SkillHint("スキルヒント"), SkillPt("スキルPt"),
    AptitudePt("適性Pt"), StarGauge("スターゲージ"), MaxHp("体力最大値+回復"),
    Motivation("やる気+回復"), Hp("体力回復"),
    Aikyou("愛嬌○"), GoodTraining("練習上手○"),
}

private val initialMemberStatus = arrayOf(
    1520 to Status(150, 150, 150, 150, 150),
    1420 to Status(140, 140, 140, 140, 140),
    1320 to Status(130, 130, 130, 130, 130),
    1120 to Status(120, 120, 120, 120, 120),
    1020 to Status(110, 110, 110, 110, 110),
    920 to Status(100, 100, 100, 100, 100),
    720 to Status(94, 94, 94, 94, 94),
    720 to Status(88, 88, 88, 88, 88),
    620 to Status(82, 82, 82, 82, 82),
    520 to Status(76, 76, 76, 76, 76),
    520 to Status(70, 70, 70, 70, 70),
    420 to Status(68, 68, 68, 68, 68),
    420 to Status(66, 66, 66, 66, 66),
    320 to Status(64, 64, 64, 64, 64),
    320 to Status(62, 62, 62, 62, 62),
)

private val linkMemberStarEffect = mapOf(
    "ゴールドシップ" to StarEffect.Status,
    "エルコンドルパサー" to StarEffect.AptitudePt,
    "マンハッタンカフェ" to StarEffect.MaxHp,
    "ナカヤマフェスタ" to StarEffect.SkillPt,
    "サトノダイヤモンド" to StarEffect.Motivation,
    "シリウスシンボリ" to StarEffect.StarGauge,
)

data class LArcMemberState(
    val status: Status = Status(),
    val supporterPt: Int = 0,
    val initialRank: Int = 0,
    val starType: StatusType = StatusType.NONE,
    val specialStarEffect: StarEffect = StarEffect.Status,
    val nextStarEffect: List<StarEffect> = emptyList(),
    val starLevel: Int = 1,
    val starGauge: Int = 0,
) : ScenarioMemberState {
    override fun toShortString() =
        "LArcMemberState(status=${status.toShortString()}, supporterPt=$supporterPt, initialRank=$initialRank, starGauge=$starGauge/Lv$starLevel/$starType/$specialStarEffect"

    override val scenarioLink get() = Store.getScenarioLink(Scenario.LARC)

    fun initialize(initialRank: Int, charaName: String): LArcMemberState {
        val initialStatus = initialMemberStatus[initialRank - 1]
        val specialStarEffect = linkMemberStarEffect[charaName] ?: when (initialRank) {
            7, 13 -> StarEffect.SkillPt
            8 -> StarEffect.Motivation
            9 -> StarEffect.Hp
            10 -> StarEffect.StarGauge
            else -> StarEffect.AptitudePt
        }
        val nextStarEffectBase = listOf(
            specialStarEffect,
            StarEffect.Status,
            StarEffect.SkillHint
        ).shuffled()
        val nextStarEffect = when (charaName) {
            "ゴールドシップ" -> nextStarEffectBase.replaced(0) { StarEffect.Aikyou }
            "エルコンドルパサー" -> nextStarEffectBase.replaceItem(StarEffect.Status, StarEffect.GoodTraining)
            else -> nextStarEffectBase
        }
        return copy(
            status = initialStatus.second,
            supporterPt = initialStatus.first,
            initialRank = initialRank,
            starType = trainingType[(initialRank - 1) % 5],
            specialStarEffect = specialStarEffect,
            nextStarEffect = nextStarEffect,
        )
    }
}
