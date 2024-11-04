package io.github.mee1080.umasim.web.state

import io.github.mee1080.umasim.data.StatusType
import io.github.mee1080.umasim.scenario.cook.CookMaterial
import io.github.mee1080.umasim.scenario.cook.CookStatus
import io.github.mee1080.umasim.scenario.cook.cookDishData

data class CookState(
    val cookPoint: Int = 0,
    val phase: Int = -1,
    val dishRank: Int = 0,
    val materialLevel: Int = 0,
) {
    private val baseCookStatus = CookStatus(
        cookPoint = cookPoint,
        materialLevel = if (phase == 3) {
            CookMaterial.entries.associateWith { if (it.ordinal < materialLevel) 5 else 1 }
        } else {
            CookMaterial.entries.associateWith { materialLevel }
        },
    )

    val specialityRate = baseCookStatus.cookPointEffect.specialityRate

    fun toCookStatus(trainingType: StatusType) = baseCookStatus.copy(
        activatedDish = cookDishData.firstOrNull {
            it.phase == phase && (phase == 0 || it.rank == dishRank) && it.trainingTarget.contains(trainingType)
        },
    )
}