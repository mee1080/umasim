package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*
import io.github.mee1080.umasim.util.applyIf

class GmScenarioEvents : CommonScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        return super.beforeSimulation(state).copy(
            goalRace = state.goalRace.dropLast(3) +
                    RaceEntry(78, "グランドマスターズ", 0, 30000, RaceGrade.FINALS, 1200, RaceGround.UNKNOWN, "")
        )
    }

    override fun beforeAction(state: SimulationState): SimulationState {
        return super.beforeAction(state).updateTrainingLevel()
    }

    private fun SimulationState.updateTrainingLevel(): SimulationState {
        val gmStatus = gmStatus ?: return this
        val specialRacePoint = when {
            turn <= 24 -> 0
            turn <= 48 -> 20
            turn <= 72 -> 40
            else -> 60
        }
        val newTraining = training.map {
            val value = it.count * 5 + gmStatus.fragmentCount[it.type]!! * 3 + specialRacePoint
            val level = when {
                value < 30 -> 1
                value < 60 -> 2
                value < 90 -> 3
                value < 120 -> 4
                else -> 5
            }
            if (level != it.level) it.copy(level = level) else it
        }
        return copy(training = newTraining)
    }

    override fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        return when (base.turn) {
            // 導入
            2 -> base.copy(gmStatus = GmStatus())

            24 -> base.applyRace(SpecialRace.GUR)

            48 -> base.applyRace(SpecialRace.WBC)

            72 -> base.applyRace(SpecialRace.SWBC)

            // スキル獲得イベント（ステ上昇のみ反映）
            77 -> if (state.gmStatus!!.wisdomLevel.maxOf { it.value } >= 4) {
                base.updateStatus { it + Status(10, 10, 10, 10, 10, 40) }
            } else base

            else -> base
        }
    }

    private enum class SpecialRace(
        statusValue: Int,
        skillPt: Int,
        val fanCount: Int,
        val updateLevel: Int,
        val updateStatusCount: Int,
        val updateSkillPt: Int,
    ) {
        GUR(10, 50, 750, 1, 1, 20),
        WBC(15, 60, 10000, 2, 2, 30),
        SWBC(20, 70, 20000, 3, 3, 40),
        ;

        val baseStatus = Status(statusValue, statusValue, statusValue, statusValue, statusValue, skillPt)
    }

    private fun SimulationState.applyRace(race: SpecialRace): SimulationState {
        val gmStatus = gmStatus ?: return this
        // FIXME 青叡智のスキル獲得は未反映
        val baseStatus = race.baseStatus
            .applyIf(gmStatus.wisdomLevel[Founder.Red]!! > race.updateLevel) {
                copy(skillPt = skillPt + race.updateSkillPt)
            }
            .applyIf(gmStatus.wisdomLevel[Founder.Yellow]!! > race.updateLevel) {
                add(*(randomTrainingType(race.updateStatusCount).map { it to 10 }).toTypedArray())
            }
        val raceBonusStatus = baseStatus.multiplyToInt(totalRaceBonus)
        return updateStatus {
            it + applyScenarioRaceBonus(raceBonusStatus) + Status(fanCount = raceFanCount(race.fanCount))
        }
    }

    override fun onTurnEnd(state: SimulationState): SimulationState {
        val base = super.onTurnEnd(state)
        val gmStatus = base.gmStatus ?: return base
        return state.copy(gmStatus = gmStatus.turnChange())
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        // エンディング、良鬼は未反映
        return super.afterSimulation(state).updateStatus {
            it + Status(20, 20, 20, 20, 20, 60)
        }
    }
}
