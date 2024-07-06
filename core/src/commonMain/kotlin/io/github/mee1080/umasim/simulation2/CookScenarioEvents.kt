package io.github.mee1080.umasim.simulation2

import io.github.mee1080.umasim.data.*

class CookScenarioEvents : CommonScenarioEvents() {

    override fun beforeSimulation(state: SimulationState): SimulationState {
        val initialMaterialCount = if (Store.isScenarioLink(Scenario.COOK, state.chara.charaName)) {
            75
        } else 50
        return super.beforeSimulation(state).addGuest().copy(
            cookStatus = CookStatus(materialCount = CookMaterial.entries.associateWith { initialMaterialCount })
        )
    }

    private fun SimulationState.addGuest(): SimulationState {
        val supportNames = member.filter { !it.outingType }.map { it.charaName }.toSet()
        var memberIndex = member.size
        val guestMembers = Store.guestSupportCardList
            .filter { !it.type.outingType && !supportNames.contains(it.chara) }
            .shuffled()
            .take(12 - supportNames.size)
            .map { MemberState(memberIndex++, it, StatusType.NONE, null, CookMemberState) }
        return copy(member = member + guestMembers)
    }

    override fun afterAction(state: SimulationState, selector: ActionSelector): SimulationState {
        val base = super.afterAction(state, selector)
        val cookStatus = base.cookStatus ?: return base
        return when (state.turn) {

            // J12後行動後（試食会1回目）
            24 -> if (cookStatus.cookPoint >= 1000) {
                base.addStatus(Status(5, 5, 5, 5, 5, 40))
                    .updateCookStatus { updateDishRank(1, 1) }
                    .addRelation(5) { it.charaName == "秋川理事長" }
            } else {
                base.addStatus(Status(3, 3, 3, 3, 3, 30))
                    .updateCookStatus { updateDishRank(1, 0) }
            }

            // C6後行動後（試食会2回目）
            36 -> if (cookStatus.cookPoint >= 2500) {
                base.addStatus(Status(10, 10, 10, 10, 10, 50))
                    .updateCookStatus { updateDishRank(1, 2) }
                    .addRelation(5) { it.charaName == "秋川理事長" }
            } else {
                base.addStatus(Status(5, 5, 5, 5, 5, 40))
            }

            // C12後行動後（試食会3回目）
            48 -> if (cookStatus.cookPoint >= 5000) {
                base.addStatus(Status(15, 15, 15, 15, 15, 60))
                    .updateCookStatus { updateDishRank(2, 1) }
                    .addRelation(5) { it.charaName == "秋川理事長" }
            } else {
                base.addStatus(Status(10, 10, 10, 10, 10, 50))
                    .updateCookStatus { updateDishRank(2, 0) }
            }

            // S6後行動後（試食会2回目）
            60 -> if (cookStatus.cookPoint >= 7000) {
                base.addStatus(Status(20, 20, 20, 20, 20, 70))
                    .updateCookStatus { updateDishRank(2, 2) }
                    .addRelation(5) { it.charaName == "秋川理事長" }
            } else {
                base.addStatus(Status(15, 15, 15, 15, 15, 60))
            }

            // C12後行動後（大豊食祭）
            72 -> if (cookStatus.cookPoint >= 10000) {
                base.addStatus(Status(25, 25, 25, 25, 25, 80, skillHint = mapOf("時中の妙" to 1)))
                    .updateCookStatus { updateDishRank(3, if (cookStatus.cookPoint >= 12000) 2 else 1) }
                    .addRelation(5) { it.charaName == "秋川理事長" }
            } else {
                base.addStatus(Status(20, 20, 20, 20, 20, 70))
                    .updateCookStatus { updateDishRank(3, 0) }
            }

            else -> base
        }
    }

    override fun afterSimulation(state: SimulationState): SimulationState {
        val base = super.afterSimulation(state)
        // 全て大満足/超満足前提
        return base.addStatus(Status(60, 60, 60, 60, 60, 150, skillHint = mapOf("私たちの走る道程" to 1)))
    }
}
