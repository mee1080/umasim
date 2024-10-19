package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import io.github.mee1080.utility.sumMapOf

class UafScenarioEvents : CommonScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        val base = super.beforeSimulation(state)
        val newMember = if (base.member.any { it.charaName == "都留岐涼花" }) base.member else {
            base.member + MemberState(
                index = base.member.size,
                card = Store.getSupportByName("[プロデューサー]都留岐涼花", 0),
                StatusType.NONE,
                null,
                UafMemberState,
            )
        }
        return base.copy(member = newMember, uafStatus = UafStatus())
    }

    override fun beforeAction(state: SimulationState): SimulationState {
        val base = super.beforeAction(state)
        val uafStatus = state.uafStatus ?: return base
        val newUafStatus = when (state.turn) {
            1, 13, 25, 37, 49, 61, 73 -> uafStatus.copy(consultCount = 3)
            else -> uafStatus
        }.randomizeTraining()
        return base.copy(uafStatus = newUafStatus)
    }

    override fun beforePredict(state: SimulationState): SimulationState {
        val base = super.beforePredict(state)
        val uafStatus = state.uafStatus ?: return base
        val levelUp = UafAthleticsLevelCalculator.calcLevelUp(base.member, uafStatus.levelUpBonus)
        return base.copy(uafStatus = uafStatus.copy(athleticsLevelUp = levelUp))
    }

    override suspend fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (state.turn) {

            // J12後行動後：UAF TEST STAGE
            24 -> base.applyUaf(
                10, 5, 40, 1000,
                3, 30, 100,
            )

            // C6後行動後：UAF TRIALS 1st STAGE
            36 -> base.applyUaf(
                20, 10, 50, 5000,
                5, 40, 500,
            )

            // C12後行動後：UAF TRIALS 2nd STAGE
            48 -> base.applyUaf(
                30, 15, 60, 15000,
                10, 50, 1500,
            )

            // C18後行動後：UAF TRIALS 3rd STAGE
            60 -> base.applyUaf(
                40, 20, 70, 20000,
                15, 60, 2000, "バーニングソウル",
            )

            // S9後行動後：やる気+1、決死の覚悟1
            66 -> base.addStatus(
                Status(motivation = 1, skillHint = mapOf("決死の覚悟" to 1))
            )

            // C24後行動後：UAF TRIALS 4th STAGE
            72 -> base.applyUaf(
                50, 25, 80, 22000,
                20, 70, 5000, "全身全霊",
            )

            else -> base
        }
    }

    private fun SimulationState.applyUaf(
        needWinCount: Int, winStatus: Int, winSkillPt: Int, winFan: Int,
        loseStatus: Int, loseSkillPt: Int, loseFan: Int, winSkill: String? = null,
    ): SimulationState {
        val uafStatus = uafStatus ?: return this
        val winCount = uafStatus.athleticsLevel.entries.groupBy { it.key.genre }
            .mapValues { levels -> levels.value.count { it.value >= needWinCount } }
        val isWin = winCount.values.sum() >= 12
        val linkBonus = if (scenario.scenarioLink.contains(chara.charaName)) 3 else 0
        val status = if (isWin) winStatus + linkBonus else loseStatus
        val skillPt = if (isWin) winSkillPt else loseSkillPt
        val fan = if (isWin) winFan else loseFan
        val skill = if (isWin && winSkill != null) mapOf(winSkill to 1) else emptyMap()
        val newFestivalWinCount = sumMapOf(uafStatus.festivalWinCount, winCount)
        val newFestivalBonus = newFestivalWinCount.values.sumOf {
            when {
                it >= 20 -> 17
                it >= 15 -> 12
                it >= 10 -> 7
                it >= 5 -> 3
                it >= 1 -> 1
                else -> 0
            }.toInt()
        }

        return addStatus(
            Status(
                speed = status,
                stamina = status,
                power = status,
                guts = status,
                wisdom = status,
                skillPt = skillPt,
                hp = 15,
                motivation = 1,
                fanCount = fan,
                skillHint = skill,
            )
        ).copy(
            uafStatus = uafStatus.copy(
                festivalWinCount = newFestivalWinCount,
                festivalBonus = newFestivalBonus,
            )
        )
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        val base = super.afterSimulation(state)
        val uafStatus = state.uafStatus ?: return base
        val totalWinCount = uafStatus.festivalWinCount.values.sum()
        return when {
            totalWinCount == 75 -> base.addAllStatus(55, 140, mapOf("爆熱のキラメキ！" to 3))
            // TODO 条件未確定
            totalWinCount >= 60 -> base.addAllStatus(30, 90, mapOf("爆熱のキラメキ！" to 1))
            else -> base.addAllStatus(20, 70, mapOf("バーニングソウル" to 1))
        }
    }
}
